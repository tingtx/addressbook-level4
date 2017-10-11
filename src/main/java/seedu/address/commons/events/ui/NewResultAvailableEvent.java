package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

/**
 * Indicates that a new result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final String message;
    private boolean isErrorCommand = false;

    public NewResultAvailableEvent(String message) {
        this.message = message;
        if (message.equals(MESSAGE_UNKNOWN_COMMAND)){
            isErrorCommand = true;
        }
    }

    public boolean getIsErrorCommand(){
        return isErrorCommand;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
