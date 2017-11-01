package seedu.address.model.user.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

public class DuplicateUserException extends DuplicateDataException {
    public DuplicateUserException() {
        super("User exists");
    }
}
