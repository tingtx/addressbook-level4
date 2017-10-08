package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.commons.events.ui.ViewAliasRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;

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
}
