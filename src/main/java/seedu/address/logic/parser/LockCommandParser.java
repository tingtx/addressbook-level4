package seedu.address.logic.parser;

import seedu.address.logic.commands.LockCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class LockCommandParser implements Parser<LockCommand>{
    @Override
    public LockCommand parse(String userInput) throws ParseException {
        return new LockCommand("a","b");
    }
}
