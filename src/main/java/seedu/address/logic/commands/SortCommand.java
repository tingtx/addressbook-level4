package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;

public class SortCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sort";
    public static final String COMMAND_ALIAS = "s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sort the Address Book based on a specified parameter. "
            + "This will affect the indices of the persons and the commands using this number, e.g. delete command.\n"
            + "Parameters: PARAMETER (NAME, DOB)\n"
            + "Example: " + COMMAND_WORD + " NAME";

    public static final String MESSAGE_SORT_SUCCESS = "Address Book has been sorted";

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        return new CommandResult(MESSAGE_SORT_SUCCESS);
    }
}
