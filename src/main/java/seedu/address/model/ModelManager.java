package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.AliasSettings;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetAliasCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.ViewAliasCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.alias.Alias;
import seedu.address.model.alias.exceptions.DuplicateAliasException;
import seedu.address.model.alias.exceptions.UnknownCommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.exceptions.UnrecognisedParameterException;
import seedu.address.model.tag.Tag;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<ReadOnlyPerson> filteredPersons;
    private final ArrayList<ArrayList<String>> viewAliases;
    private UserPrefs userPref;
    private Storage userStorage;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPref = userPrefs;
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());


        ArrayList<ArrayList<String>> commandList = new ArrayList<ArrayList<String>>();

        //Add Command
        commandList.add(new ArrayList<String>(Arrays.asList("Add", AddCommand.getCommandWord())));

        //Clear Command
        commandList.add(new ArrayList<String>(Arrays.asList("Clear", ClearCommand.getCommandWord())));

        //Delete Command
        commandList.add(new ArrayList<String>(Arrays.asList("Delete", DeleteCommand.getCommandWord())));

        //Edit Command
        commandList.add(new ArrayList<String>(Arrays.asList("Edit", EditCommand.getCommandWord())));

        //Exit Command
        commandList.add(new ArrayList<String>(Arrays.asList("Exit", ExitCommand.getCommandWord())));

        //Find Command
        commandList.add(new ArrayList<String>(Arrays.asList("Find", FindCommand.getCommandWord())));

        //Help Command
        commandList.add(new ArrayList<String>(Arrays.asList("Help", HelpCommand.getCommandWord())));

        //History Command
        commandList.add(new ArrayList<String>(Arrays.asList("History", HistoryCommand.getCommandWord())));

        //List Command
        commandList.add(new ArrayList<String>(Arrays.asList("List", ListCommand.getCommandWord())));

        //Order Command
        commandList.add(new ArrayList<String>(Arrays.asList("Order", OrderCommand.getCommandWord())));

        //Redo Command
        commandList.add(new ArrayList<String>(Arrays.asList("Redo", RedoCommand.getCommandWord())));

        //Remark Command
        commandList.add(new ArrayList<String>(Arrays.asList("Remark", RemarkCommand.getCommandWord())));

        //Select Command
        commandList.add(new ArrayList<String>(Arrays.asList("Select", SelectCommand.getCommandWord())));

        //Set Alias Command
        commandList.add(new ArrayList<String>(Arrays.asList("Set Alias", SetAliasCommand.getCommandWord())));

        //Undo Command
        commandList.add(new ArrayList<String>(Arrays.asList("Undo", UndoCommand.getCommandWord())));

        //View Alias Command
        commandList.add(new ArrayList<String>(Arrays.asList("View Alias", ViewAliasCommand.getCommandWord())));

        viewAliases = commandList;

    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /**
     * Raises an event to indicate the model has changed
     */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void orderList(String parameter) throws UnrecognisedParameterException {
        addressBook.orderList(parameter);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    @Override
    public void deleteTag(Tag tag) throws PersonNotFoundException, DuplicatePersonException {
        for (int i = 0; i < addressBook.getPersonList().size(); i++) {
            ReadOnlyPerson oldPerson = addressBook.getPersonList().get(i);

            Person newPerson = new Person(oldPerson);
            Set<Tag> newTags = newPerson.getTags();
            newTags.remove(tag);
            newPerson.setTags(newTags);

            addressBook.updatePerson(oldPerson, newPerson);
        }
    }

    public ArrayList<ArrayList<String>> getCommands() {
        return viewAliases;
    }

    @Override
    public String getAliasForCommand(String command) {
        AliasSettings aliasSettings = userPref.getAliasSettings();

        if (command.equals(AddCommand.getCommandWord())) {
            return aliasSettings.getAddCommand().getAlias();
        }
        else if (command.equals(ClearCommand.getCommandWord())) {
            return aliasSettings.getClearCommand().getAlias();
        }
        else if (command.equals(DeleteCommand.getCommandWord())) {
            return aliasSettings.getDeleteCommand().getAlias();
        }
        else if (command.equals(EditCommand.getCommandWord())) {
            return aliasSettings.getEditCommand().getAlias();
        }
        else if (command.equals(ExitCommand.getCommandWord())) {
            return aliasSettings.getExitCommand().getAlias();
        }
        else if (command.equals(FindCommand.getCommandWord())) {
            return aliasSettings.getFindCommand().getAlias();
        }
        else if (command.equals(HelpCommand.getCommandWord())) {
            return aliasSettings.getHelpCommand().getAlias();
        }
        else if (command.equals(HistoryCommand.getCommandWord())) {
            return aliasSettings.getHistoryCommand().getAlias();
        }
        else if (command.equals(ListCommand.getCommandWord())) {
            return aliasSettings.getListCommand().getAlias();
        }
        else if (command.equals(OrderCommand.getCommandWord())) {
            return aliasSettings.getOrderCommand().getAlias();
        }
        else if (command.equals(RedoCommand.getCommandWord())) {
            return aliasSettings.getRedoCommand().getAlias();
        }
        else if (command.equals(RemarkCommand.getCommandWord())) {
            return aliasSettings.getRemarkCommand().getAlias();
        }
        else if (command.equals(SelectCommand.getCommandWord())) {
            return aliasSettings.getSelectCommand().getAlias();
        }
        else if (command.equals(SetAliasCommand.getCommandWord())) {
            return aliasSettings.getSetAliasCommand().getAlias();
        }
        else if (command.equals(UndoCommand.getCommandWord())) {
            return aliasSettings.getUndoCommand().getAlias();
        }
        else if (command.equals(ViewAliasCommand.getCommandWord())) {
            return aliasSettings.getViewAliasCommand().getAlias();
        }
        else {
            return "Not Set";
        }
    }

    @Override
    public void setAlias(String commandName, String alias) throws DuplicateAliasException, UnknownCommandException {
        try {
            this.userPref.setAlias(commandName, alias);
        } catch (DuplicateAliasException e) {
            throw e;
        } catch (UnknownCommandException e) {
            throw e;
        }
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyPerson} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredPersons.setPredicate(null);
    }

    @Override
    public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredPersons.equals(other.filteredPersons);
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) throws IllegalValueException {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
