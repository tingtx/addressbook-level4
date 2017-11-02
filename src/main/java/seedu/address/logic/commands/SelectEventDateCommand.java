package seedu.address.logic.commands;

import seedu.address.model.event.Datetime;

public class SelectEventDateCommand extends Command {

    public static final String COMMAND_WORD = "selecteventdate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Get all events according to the datetime given "
            + "Parameters: edt/DATETIME...\n"
            + "Example: " + COMMAND_WORD + " edt/13-05-2017";

    private final String targetDate;

    public SelectEventDateCommand(String targetDate) {
        this.targetDate = targetDate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredEventList(predicate);
        return new CommandResult(getMessageForEventListShownSummary(model.getFilteredEventList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindEventCommand // instanceof handles nulls
                && this.predicate.equals(((FindEventCommand) other).predicate)); // state check
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
