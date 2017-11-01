package seedu.address.logic.commands;

import seedu.address.model.person.exceptions.UnrecognisedParameterException;

/**
 * Order the list according to a parameter
 */
public class OrderEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "orderevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Order the Event Book based on one/multiple parameter(s).\n"
            + "Parameters:  TITLE, LOCATION, DATETIME\n"
            + "Example: " + COMMAND_WORD + " TITLE";

    public static final String MESSAGE_SORT_SUCCESS = "Event Book has been sorted by ";
    public static final String MESSAGE_SORT_WRONG_PARAMETER =
            "The parameter can only contain Title, Location, Datetime";

    private String orderParameter;

    public OrderEventCommand(String orderParameter) {
        this.orderParameter = orderParameter;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        try {
            model.orderEventList(orderParameter);
            return new CommandResult(MESSAGE_SORT_SUCCESS + orderParameter);
        } catch (UnrecognisedParameterException upe) {
            return new CommandResult(MESSAGE_SORT_WRONG_PARAMETER);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OrderEventCommand // instanceof handles nulls
                && this.orderParameter.equals(((OrderEventCommand) other).orderParameter)); // state check
    }

    public String getOrderParameter() {
        return orderParameter;
    }
}
