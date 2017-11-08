package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.currentuser.CurrentUserDetails;
import seedu.address.logic.encryption.FileEncryptor;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.user.exceptions.DuplicateUserException;

/**
 * Log out the current user to "PUBLIC" user
 */
public class LogoutCommand extends Command {
    public static final String COMMAND_WORD = "logout";
    public static final String COMMAND_ALIAS = "lgo";
    private static final String MESSAGE_SUCCESS = "Logged out successfully!";
    private static final String MESSAGE_LOGOUT_ERROR = "You have not logged in!";

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateUserException {
        if (CurrentUserDetails.userId.equals("PUBLIC")) {
            throw new CommandException(MESSAGE_LOGOUT_ERROR);
        }
        try {
            ObservableList<ReadOnlyPerson> list = model.getListLength();
            FileEncryptor.encryptFile(CurrentUserDetails.userIdHex.substring(0, 10), CurrentUserDetails.saltText
                    + CurrentUserDetails.passwordText, true);
            model.emptyPersonList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CurrentUserDetails.userId = "PUBLIC";
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
