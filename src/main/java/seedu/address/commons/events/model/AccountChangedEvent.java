package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyAccount;

public class AccountChangedEvent extends BaseEvent {

    public final ReadOnlyAccount data;

    public AccountChangedEvent(ReadOnlyAccount data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Accounts List Changed!";
    }
}
