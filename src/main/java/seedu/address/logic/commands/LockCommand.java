package seedu.address.logic.commands;

import seedu.address.logic.commands.digestUtil.HashDigest;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.user.ReadOnlyUser;
import seedu.address.model.user.User;
import seedu.address.model.user.exceptions.DuplicateUserException;

import java.security.SecureRandom;
import java.util.Random;

import static java.util.Objects.requireNonNull;

public class LockCommand extends Command {
    public static final String COMMAND_WORD = "lock";
    public static final String COMMAND_ALIAS = "lo";
    private static final String MESSAGE_EXISTING_USER = "User already exists";
    private static final String MESSAGE_SUCCESS = "Account is created and your Address Book is locked with your password";
    private String userId;
    private String passwordText;

    public LockCommand (String userId, String passwordText){
        this.userId = userId;
        this.passwordText = passwordText;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateUserException {
        requireNonNull(model);
        byte[] uIdDigest = new HashDigest().getHashDigest(userId);
        if (model.isExistingUser()){
            return new CommandResult(MESSAGE_EXISTING_USER);
        }
        byte[] pwDigest = new HashDigest().getHashDigest(passwordText);
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        model.persistUserAccount(new User(new String(uIdDigest), new String(salt), new String(pwDigest)));
        return new CommandResult(MESSAGE_SUCCESS);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswordText() {
        return passwordText;
    }

    public void setPasswordText(String passwordText) {
        this.passwordText = passwordText;
    }
}
