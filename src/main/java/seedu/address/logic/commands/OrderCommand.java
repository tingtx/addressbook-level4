package seedu.address.logic.commands;

import seedu.address.model.person.exceptions.UnrecognisedParameterException;

/**
 * Order the list according to a parameter
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Order the Address Book based on one/multiple parameter(s).\n"
            + "Parameters:  NAME, ADDRESS, TAG\n"
            + "Example: " + COMMAND_WORD + " NAME ADDRESS";

    public static final String MESSAGE_SORT_SUCCESS = "Address Book has been sorted by ";
    public static final String MESSAGE_SORT_WRONG_PARAMETER = "The parameter can only contain Name, Address, Tag";

    private String orderParameter;

    public OrderCommand(String orderParameter) {
        this.orderParameter = orderParameter;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        try {
            model.orderList(orderParameter);
            return new CommandResult(MESSAGE_SORT_SUCCESS + orderParameter);
        } catch (UnrecognisedParameterException upe) {
            return new CommandResult(MESSAGE_SORT_WRONG_PARAMETER);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OrderCommand // instanceof handles nulls
                && this.orderParameter.equals(((OrderCommand) other).orderParameter)); // state check
    }

    public String getOrderParameter() {
        return orderParameter;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
