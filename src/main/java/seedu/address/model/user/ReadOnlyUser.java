package seedu.address.model.user;

public interface ReadOnlyUser {
    String getUserId();

    void setUserId(String userId);

    String getSalt();

    void setSalt(String salt);

    String getPassword();

    void setPassword(String password);

    default boolean isExistingUser(String userId) {
        return this.getUserId().equals(userId);
    }

    default boolean isCorrectPassword(String userId, String password) {
        return this.getUserId().equals(userId) && this.getPassword().equals(password);
    }

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyUser other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getUserId().equals(this.getUserId())); // state checks here onwards
    }
}
