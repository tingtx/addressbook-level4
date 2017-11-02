package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import seedu.address.logic.commands.digestutil.HashDigest;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author quanle1994
/**
 * Log the user in.
 */
public class LoginCommand extends Command {

    public static final String COMMAND_WORD = "login";
    public static final String COMMAND_ALIAS = "li";
    public static final String MESSAGE_SUCCESS = "Log In Successful";
    private static final String MESSAGE_ERROR_NO_USER = "User does not exist";
    private static final String MESSAGE_ERROR_WRONG_PASSWORD = "Wrong Password";
    private byte[] password;
    private String userId;
    private String passwordText;

    public LoginCommand(String userId, String passwordText) {
        this.userId = userId;
        this.passwordText = passwordText;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    private boolean isSameDigest(byte[] digest1, byte[] digest2) {
        return Arrays.equals(digest1, digest2);
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        if (!checkExistingUserId()) {
            return new CommandResult(MESSAGE_ERROR_NO_USER);
        }
        String pwSalt = getSalt();
        String combinedPw = pwSalt + passwordText;
        if (!matchedPassword(new HashDigest().getHashDigest(combinedPw))) {
            return new CommandResult(MESSAGE_ERROR_WRONG_PASSWORD);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    private boolean matchedPassword(byte[] digest) {
        return isSameDigest(password, digest);
    }

    private boolean checkExistingUserId() {
        byte[] uidDigest = new HashDigest().getHashDigest(userId);
        byte[] retrievedDigest = model.retrieveDigestFromStorage();
        return isSameDigest(uidDigest, retrievedDigest);
    }

    public byte[] getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public String getSalt() {
        return model.retrieveSaltFromStorage(userId);
    }
}
