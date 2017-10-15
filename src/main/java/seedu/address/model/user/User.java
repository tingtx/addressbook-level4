package seedu.address.model.user;

import java.util.Objects;

public class User implements ReadOnlyUser {
    private String userId;
    private String salt;
    private String password;

    public User(String userId, String salt, String password){
        this.userId = userId;
        this.salt = salt;
        this.password = password;
    }

    /**
     * Creates a copy of the given ReadOnlyUser.
     */
    public User(ReadOnlyUser source) {
        this(source.getUserId(), source.getSalt(), source.getPassword());
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getSalt() {
        return salt;
    }

    @Override
    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyUser // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyUser) other));
    }
}
