package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ContainsKeywordsPredicate;

//@@author tingtx
/**
 * Parses input arguments and creates a new ListCommand object
 */
public class LIstCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an ListCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (!trimmedArgs.isEmpty() && !trimmedArgs.substring(0,2).equals("g/")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.COMMAND_USAGE));
        }

        ContainsKeywordsPredicate.setPredicateType('g');
        return new ListCommand(trimmedArgs);
    }
}
