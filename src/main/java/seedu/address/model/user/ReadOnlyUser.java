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

    default boolean isCorrectPassword(String userId, String password){
        return this.getUserId().equals(userId) && this.getPassword().equals(password);
    }
}
