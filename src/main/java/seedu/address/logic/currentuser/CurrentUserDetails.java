package seedu.address.logic.currentuser;

public class CurrentUserDetails {
    public static String userId = "PUBLIC";
    public static String userIdHex = "";
    public static String saltText = "";
    public static String passwordText = "";
    public static void setCurrentUser(String userId, String userIdHex, String saltText, String passwordText) {
        CurrentUserDetails.userId = userId;
        CurrentUserDetails.userIdHex = userIdHex;
        CurrentUserDetails.saltText = saltText;
        CurrentUserDetails.passwordText = passwordText;
    }
}
