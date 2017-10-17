package seedu.address.model.alias;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.logic.commands.Command;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Alias {

    private ObjectProperty<String> aliasCommand;
    private ObjectProperty<String> aliasString;

    /**
     * Every field must be present and not null.
     */
    public Alias(String command, String alias) {
        requireAllNonNull(command, alias);
        this.aliasCommand = new SimpleObjectProperty<>(command);
        this.aliasString = new SimpleObjectProperty<>(alias);
    }

    public void setAlias(String alias) {
        this.aliasString.set(requireNonNull(alias));
    }

    public String getAlias() {
        return aliasString.get();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Alias// instanceof handles nulls
                && this.aliasString == ((Alias) other).aliasString);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(aliasCommand, aliasString);
    }

}
