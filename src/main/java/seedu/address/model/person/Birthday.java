package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's birthday in the address book.
 */

public class Birthday {
    public static final String MESSAGE_BIRTHDAY_CONSTRAINTS =
            "Birthday can only contain numbers and forward slashes, and in the form dd-mm-yyyy";
    public static final String BIRTHDAY_VALIDATION_REGEX = "\\d{2}-\\d{2}-\\d{4}";
    public final String value;

    /**
     * Validates given birthday.
     *
     * @throws IllegalValueException if given birthday string is invalid.
     */
    public Birthday(String birthday) throws IllegalValueException {
        requireNonNull(birthday);
        String trimmedBirthday = birthday.trim();
        if (!isValidBirthday(trimmedBirthday)) {
            throw new IllegalValueException(MESSAGE_BIRTHDAY_CONSTRAINTS);
        }
        this.value = trimmedBirthday;
    }

    /**
     * Returns true if a given string is a valid person birthday.
     */
    public static boolean isValidBirthday(String test) {
        return test.isEmpty() || test.matches(BIRTHDAY_VALIDATION_REGEX);
    }

    public String getReformatDate() {
        if (value.isEmpty()) {
            return null;
        }
        return new StringBuilder().append(value.substring(6, 10)).append(value.substring(3, 5))
                .append(value.substring(0, 2)).toString();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Birthday // instanceof handles nulls
                && this.value.equals(((Birthday) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

