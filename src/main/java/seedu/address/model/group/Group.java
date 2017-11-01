//@@author tingtx
package seedu.address.model.group;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's group in the address book.
 */
public class Group {

    public static final String MESSAGE_GROUP_CONSTRAINTS =
            "Group name can take any values";

    public final String value;

    public Group(String group) {
        requireNonNull(group);
        this.value = group;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Group //instanceof handles null
                && this.value.equals(((Group) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
