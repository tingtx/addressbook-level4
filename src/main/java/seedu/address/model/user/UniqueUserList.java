package seedu.address.model.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.fxmisc.easybind.EasyBind;
import seedu.address.model.user.exceptions.DuplicateUserException;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class UniqueUserList {
    private final ObservableList<User> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyUser> mappedList = EasyBind.map(internalList, (user) -> user);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(ReadOnlyUser toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicateUserException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(ReadOnlyUser toAdd) throws DuplicateUserException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateUserException();
        }
        internalList.add(new User(toAdd));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyUser> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    public void setUsers(List<? extends ReadOnlyUser> users) throws DuplicateUserException {
        final UniqueUserList replacement = new UniqueUserList();
        for (final ReadOnlyUser user : users) {
            replacement.add(new User(user));
        }
        setUsers(replacement);
    }

    public void setUsers(UniqueUserList replacement) throws DuplicateUserException {
        this.internalList.setAll(replacement.internalList);
    }
}
