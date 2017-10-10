package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

import java.util.List;

public class OrderCommand extends UndoableCommand {

    public String orderParameter;
    public static final String COMMAND_WORD = "order";
    public static final String COMMAND_ALIAS = "o";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Order the Address Book based on a specified parameter. "
            + "This will affect the indices of the persons and the commands using this number, e.g. delete command.\n"
            + "Parameters: PARAMETER (NAME, ADDRESS)\n"
            + "Example: " + COMMAND_WORD + " NAME";

    public static final String MESSAGE_SORT_SUCCESS = "Address Book has been sorted";

    public OrderCommand(String orderParameter){
        this.orderParameter = orderParameter;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        model.orderList(orderParameter);
        return new CommandResult(MESSAGE_SORT_SUCCESS);
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
