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
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.EventBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.ViewAliasCommand;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final EventBook eventBook;
    private final FilteredList<ReadOnlyPerson> filteredPersons;
    private final FilteredList<ReadOnlyEvent> filteredEvents;
    private final ArrayList<ArrayList<String>> viewAliases;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyEventBook eventBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, eventBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + ", event book: " +  eventBook +
                " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.eventBook = new EventBook(eventBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredEvents = new FilteredList<>(this.eventBook.getEventList());

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

        //Redo Command
        commandList.add(new ArrayList<String>(Arrays.asList("Redo", RedoCommand.getCommandWord())));

        //Select Command
        commandList.add(new ArrayList<String>(Arrays.asList("Select", SelectCommand.getCommandWord())));

        //Undo Command
        commandList.add(new ArrayList<String>(Arrays.asList("Undo", UndoCommand.getCommandWord())));

        //View Alias Command
        commandList.add(new ArrayList<String>(Arrays.asList("View Alias", ViewAliasCommand.getCommandWord())));

        //Add Event Command
        commandList.add(new ArrayList<String>(Arrays.asList("addevent", AddEventCommand.getCommandWord())));

        viewAliases = commandList;

    }

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
//    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
//        super();
//        requireAllNonNull(addressBook, userPrefs);
//
//        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);
//
//        this.addressBook = new AddressBook(addressBook);
//        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
//
//
//        ArrayList<ArrayList<String>> commandList = new ArrayList<ArrayList<String>>();
//
//        //Add Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Add", AddCommand.getCommandWord())));
//
//        //Clear Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Clear", ClearCommand.getCommandWord())));
//
//        //Delete Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Delete", DeleteCommand.getCommandWord())));
//
//        //Edit Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Edit", EditCommand.getCommandWord())));
//
//        //Exit Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Exit", ExitCommand.getCommandWord())));
//
//        //Find Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Find", FindCommand.getCommandWord())));
//
//        //Help Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Help", HelpCommand.getCommandWord())));
//
//        //History Command
//        commandList.add(new ArrayList<String>(Arrays.asList("History", HistoryCommand.getCommandWord())));
//
//        //List Command
//        commandList.add(new ArrayList<String>(Arrays.asList("List", ListCommand.getCommandWord())));
//
//        //Redo Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Redo", RedoCommand.getCommandWord())));
//
//        //Select Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Select", SelectCommand.getCommandWord())));
//
//        //Undo Command
//        commandList.add(new ArrayList<String>(Arrays.asList("Undo", UndoCommand.getCommandWord())));
//
//        //View Alias Command
//        commandList.add(new ArrayList<String>(Arrays.asList("View Alias", ViewAliasCommand.getCommandWord())));
//
//        viewAliases = commandList;
//
//    }

    public ModelManager() {
        this(new AddressBook(), new EventBook(), new UserPrefs());
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
    //========================================================================================================

    @Override
    public void resetEventData(ReadOnlyEventBook newData) {
        eventBook.resetData(newData);
        indicateEventBookChanged();
    }

    @Override
    public ReadOnlyEventBook getEventBook() {
        return eventBook;
    }

    /**
     * Raises an event to indicate the model has changed
     */
    private void indicateEventBookChanged() {
        raise(new EventBookChangedEvent(eventBook));
    }

    @Override
    public void deleteEvent(ReadOnlyEvent target) throws EventNotFoundException {
        eventBook.removeEvent(target);
        indicateEventBookChanged();
    }

    @Override
    public void addEvent(ReadOnlyEvent event) throws DuplicateEventException {
        eventBook.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        indicateEventBookChanged();
    }

    @Override
    public void updateEvent(ReadOnlyEvent target, ReadOnlyEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireAllNonNull(target, editedEvent);

        eventBook.updateEvent(target, editedEvent);
        indicateEventBookChanged();
    }

    //=========== Filtered Event List Accessors =============================================================
    @Override
    public ObservableList<ReadOnlyEvent> getFilteredEventList() {
        return FXCollections.unmodifiableObservableList(filteredEvents);
    }

    @Override
    public void updateFilteredEventList(Predicate<ReadOnlyEvent> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
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
