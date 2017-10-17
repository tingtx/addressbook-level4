package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;

import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.SetAliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetAliasCommand object
 */
public class SetAliasCommandParser implements Parser<SetAliasCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetAliasCommand
     * and returns an SetAliasCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetAliasCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COMMAND, PREFIX_ALIAS);

        if (!arePrefixesPresent(argMultimap, PREFIX_ALIAS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetAliasCommand.MESSAGE_USAGE));
        }

        try {
            String command = ParserUtil.parseCommand(argMultimap.getValue(PREFIX_COMMAND)).get();
            String alias  = ParserUtil.parseAlias(argMultimap.getValue(PREFIX_ALIAS)).get();

            return new SetAliasCommand(command, alias);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
