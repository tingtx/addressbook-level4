package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.alias.Alias;
import seedu.address.model.alias.exceptions.DuplicateAliasException;

public class SetAliasCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "setalias";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets an alias for a command. "
            + "Parameters: "
            + PREFIX_ALIAS + "ALIAS";

    public static final String MESSAGE_SUCCESS = "Alias has been set.";
    public static final String MESSAGE_DUPLICATE_ALIAS = "This alias has already been set for another command.";

    private final Command commandAdd;
    private final String toAdd;

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.setAlias(commandAdd, toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateAliasException e) {
            throw new CommandException(MESSAGE_DUPLICATE_ALIAS);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetAliasCommand // instanceof handles nulls
                && toAdd.equals(((SetAliasCommand) other).toAdd));
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
