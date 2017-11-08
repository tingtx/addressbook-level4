package seedu.address.logic.currentuser;

/**
 * Set the current user in one line
 */
public class CurrentUserUtil {
    public static void setCurrentUser(String userId, String userIdHex, String saltText, String passwordText) {
        CurrentUserDetails.userId = userId;
        CurrentUserDetails.userIdHex = userIdHex;
        CurrentUserDetails.saltText = saltText;
        CurrentUserDetails.passwordText = passwordText;
    }
}
