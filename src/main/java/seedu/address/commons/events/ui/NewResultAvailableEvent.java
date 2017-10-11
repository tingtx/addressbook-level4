package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that a new result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final String message;
    private boolean isErrorCommand = false;

    public NewResultAvailableEvent(String message) {
        this.message = message;
        if (message.equals("Unknown Command")) {
            isErrorCommand = true;
        }
    }

    public boolean getIsErrorCommand() {
        return isErrorCommand;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
