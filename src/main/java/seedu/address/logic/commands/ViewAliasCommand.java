package seedu.address.logic.commands;


import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ViewAliasRequestEvent;

/**
 * Adds a person to the address book.
 */
public class ViewAliasCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "viewalias";
    public static final String COMMAND_ALIAS = "va";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all aliases.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Opened alias window.";

    @Override
    public CommandResult executeUndoableCommand() {
        EventsCenter.getInstance().post(new ViewAliasRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);

    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
