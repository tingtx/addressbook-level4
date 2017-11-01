package seedu.address.logic.parser;

import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class LoginCommandParser implements Parser<LoginCommand> {

    @Override
    public LoginCommand parse(String userInput) throws ParseException {
        try {
            return new LoginCommand("test", "test");
        } catch (Exception e) {
            return null;
        }
    }
}
