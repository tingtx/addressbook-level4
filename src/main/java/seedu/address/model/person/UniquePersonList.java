package seedu.address.model.person;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.group.Group;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.exceptions.UnrecognisedParameterException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see Person#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyPerson> mappedList = EasyBind.map(internalList, (person) -> person);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(ReadOnlyPerson toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicatePersonException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(ReadOnlyPerson toAdd) throws DuplicatePersonException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(new Person(toAdd));
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if the replacement is equivalent to another existing person in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     */
    public void setPerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.equals(editedPerson) && internalList.contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, new Person(editedPerson));
    }

    //@@author tingtx

    /**
     * Group the person {@code target} in the list to {@code group}.
     *
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    public void groupPerson(Person target, Group group) throws PersonNotFoundException {
        requireNonNull(group);
        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        target.setGroup(group);

    }
    //@@author

    /**
     * Removes the equivalent person from the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public boolean remove(ReadOnlyPerson toRemove) throws PersonNotFoundException {
        requireNonNull(toRemove);
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new PersonNotFoundException();
        }
        return personFoundAndDeleted;
    }

    //@@author tingtx

    /**
     * Order the list.
     */
    public void orderBy(String parameter) throws UnrecognisedParameterException {
        Comparator<Person> orderByName = (Person a, Person b) -> a.getName().toString()
                .compareToIgnoreCase(b.getName().toString());
        Comparator<Person> orderByAddress = (Person a, Person b) -> a.getAddress().toString()
                .compareToIgnoreCase(b.getAddress().toString());
        Comparator<Person> orderByBirthday = comparing(a -> a.getBirthday().getReformatDate(),
                nullsLast(naturalOrder()));
        Comparator<Person> orderByTag = (Person a, Person b) -> a.getTags().toString()
                .compareToIgnoreCase(b.getTags().toString());

        switch (parameter) {
            case "NAME":
                internalList.sort(orderByName);
                break;

            case "ADDRESS":
                internalList.sort(orderByAddress);
                break;

            case "BIRTHDAY":
                internalList.sort(orderByBirthday);
                break;

            case "TAG":
                internalList.sort(orderByTag);
                break;

            case "NAME ADDRESS":
                internalList.sort(orderByName.thenComparing(orderByAddress));
                break;

            case "ADDRESS NAME":
                internalList.sort(orderByName.thenComparing(orderByTag));
                break;

            case "TAG NAME":
                internalList.sort(orderByTag.thenComparing(orderByName));
                break;

            case "NAME TAG":
                internalList.sort(orderByName.thenComparing(orderByTag));
                break;

            case "NAME BIRTHDAY":
                internalList.sort(orderByName.thenComparing(orderByBirthday));
                break;

            case "BIRTHDAY NAME":
                internalList.sort(orderByBirthday.thenComparing(orderByName));
                break;

            case "ADDRESS BIRTHDAY":
                internalList.sort(orderByAddress.thenComparing(orderByBirthday));
                break;

            case "BIRTHDAY ADDRESS":
                internalList.sort(orderByBirthday.thenComparing(orderByAddress));
                break;

            case "BIRTHDAY TAG":
                internalList.sort(nullsLast(orderByBirthday.thenComparing(orderByTag)));
                break;

            case "TAG BIRTHDAY":
                internalList.sort(orderByTag.thenComparing(orderByBirthday));
                break;

            default:
                throw new UnrecognisedParameterException();
        }

    }
    //@@author

    public void setPersons(UniquePersonList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPersons(List<? extends ReadOnlyPerson> persons) throws DuplicatePersonException {
        final UniquePersonList replacement = new UniquePersonList();
        for (final ReadOnlyPerson person : persons) {
            replacement.add(new Person(person));
        }
        setPersons(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyPerson> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
