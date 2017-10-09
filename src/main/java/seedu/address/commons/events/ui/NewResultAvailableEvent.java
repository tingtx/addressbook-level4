package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

/**
 * Indicates that a new result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final String message;
    public boolean isError = false;

    public NewResultAvailableEvent(String message) {
        this.message = message;
        if (message.equals(MESSAGE_UNKNOWN_COMMAND)){
            isError = true;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
