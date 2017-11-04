package seedu.address.logic.parser;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {
    @Override
    public ExportCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        return new ExportCommand(trimmedArgs);
    }
}
