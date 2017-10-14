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
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AccountChangedEvent;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.*;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.exceptions.UnrecognisedParameterException;
import seedu.address.model.tag.Tag;
import seedu.address.model.user.ReadOnlyUser;
import seedu.address.model.user.exceptions.DuplicateUserException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<ReadOnlyPerson> filteredPersons;
    private final ArrayList<ArrayList<String>> viewAliases;

    private final Account account;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs, ReadOnlyAccount account) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs
                + " and account " + account);

        this.addressBook = new AddressBook(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.account = new Account(account);

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

        //Order Command
        commandList.add(new ArrayList<String>(Arrays.asList("Order", OrderCommand.getCommandWord())));

        //Lock Command
        commandList.add(new ArrayList<String>(Arrays.asList("Lock", LockCommand.getCommandWord())));

        //Login Command
        commandList.add(new ArrayList<String>(Arrays.asList("Log in", LoginCommand.getCommandWord())));

        //View Alias Command
        commandList.add(new ArrayList<String>(Arrays.asList("View Alias", ViewAliasCommand.getCommandWord())));

        viewAliases = commandList;

    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new Account());
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

    @Override
    public ReadOnlyAccount getAccount() {
        return account;
    }

    /**
     * Raises an event to indicate the model has changed
     */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    /**
     * Raises an event to indicate the model has changed
     */
    private void indicateAccountChanged() {
        raise(new AccountChangedEvent(account));
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
    public String getAliasForCommand(String commandName) {
        return "Not set";
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

    //===================== Account Operations =========================

    @Override
    public void persistUserAccount(ReadOnlyUser user) throws DuplicateUserException {
        account.addUser(user);
        indicateAccountChanged();
    }

    @Override
    public boolean isExistingUser() {
        return false;
    }

    @Override
    public byte[] retrieveDigestFromStorage() {
        return new byte[0];
    }

    @Override
    public String retrieveSaltFromStorage(String userId) {
        return null;
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
