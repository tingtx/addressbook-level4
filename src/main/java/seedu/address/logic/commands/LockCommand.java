package seedu.address.logic.commands;

import seedu.address.logic.commands.digestUtil.HashDigest;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.user.ReadOnlyUser;
import seedu.address.model.user.User;
import seedu.address.model.user.exceptions.DuplicateUserException;

import java.security.SecureRandom;
import java.util.Random;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERID;

public class LockCommand extends Command {
    public static final String COMMAND_WORD = "lock";
    public static final String COMMAND_ALIAS = "lo";
    private static final String MESSAGE_EXISTING_USER = "User already exists";
    private static final String MESSAGE_SUCCESS = "Account is created and your Address Book is locked with your password";
    public static final Object MESSAGE_USAGE = COMMAND_WORD + ": Locks the current address book with a user account. "
            + "Parameters: "
            + PREFIX_USERID + "USER ID "
            + PREFIX_PASSWORD + "PASSWORD";
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
        byte[] salt = new byte[32];
        final Random r = new SecureRandom();
        r.nextBytes(salt);
        String saltText = new String(salt);
        byte[] pwDigest = new HashDigest().getHashDigest(saltText + passwordText);

        String hexUidDigest = getHexFormat(uIdDigest);
        String hexSalt = getHexFormat(salt);
        String hexPassword = getHexFormat(pwDigest);
        try {
            model.persistUserAccount(new User(hexUidDigest, hexSalt, hexPassword));
        } catch (DuplicateUserException due) {
            throw new CommandException(MESSAGE_EXISTING_USER);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    private String getHexFormat(byte[] byteStream) {
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteStream.length;i++) {
            hexString.append(Integer.toHexString(0xFF & byteStream[i]));
        }
        return hexString.toString();
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
