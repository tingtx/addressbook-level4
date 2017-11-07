//@@author keloysiusmak
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.user.exceptions.DuplicateUserException;

/**
 * Export data for transfer to another computer
 */
public class TransferCommand extends Command {

    public static final String COMMAND_WORD = "transfer";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Zips the configuration files and includes a installation guide.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_TRANSFER_SUCCESS = "Successfully exported ZIP file.";
    public static final String MESSAGE_TRANSFER_ERROR = "SOme user settings were missing. Successfully exported ZIP "
            + "file with default settings";

    public TransferCommand() {
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateUserException {
        requireNonNull(model);
        requireNonNull(config);

        try {
            model.transferData();
        } catch (ConfigMissingException e) {
            model.transferDataWithDefault();
            return new CommandResult(MESSAGE_TRANSFER_SUCCESS);
        }

        return new CommandResult(MESSAGE_TRANSFER_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExportCommand); // state check
    }
}
