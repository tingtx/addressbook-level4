package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that a new result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final String message;
    public boolean isErrorCommad = false;

    public NewResultAvailableEvent(String message) {
        this.message = message;
        if (message.equals("Unknown Command")){
            isErrorCommad = true;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
