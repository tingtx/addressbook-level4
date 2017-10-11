package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.UnrecognisedParameterException;

/**
 * Order the list according to a parameter
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";
    public static final String COMMAND_ALIAS = "o";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Order the Address Book based on a parameter. "
            + "This will affect the indices of persons and the commands using this number, e.g. delete command.\n"
            + "Parameters: PARAMETER (NAME, ADDRESS)\n"
            + "Example: " + COMMAND_WORD + " NAME";

    public static final String MESSAGE_SORT_SUCCESS = "Address Book has been sorted";
    private static final String MESSAGE_SORT_WRONG_PARAMTER = "The parameter can only be either Name or Address";

    private String orderParameter;

    public OrderCommand(String orderParameter) {
        this.orderParameter = orderParameter;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.orderList(orderParameter);
            return new CommandResult(MESSAGE_SORT_SUCCESS);
        } catch (UnrecognisedParameterException upe) {
            return new CommandResult(MESSAGE_SORT_WRONG_PARAMTER);
        }
    }

    public String getOrderParameter() {
        return orderParameter;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
