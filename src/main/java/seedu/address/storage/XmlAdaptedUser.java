package seedu.address.storage;

import seedu.address.model.user.ReadOnlyUser;
import seedu.address.model.user.User;

import javax.xml.bind.annotation.XmlElement;

public class XmlAdaptedUser {
    @XmlElement(required = true)
    private String userId;
    @XmlElement(required = true)
    private String salt;
    @XmlElement(required = true)
    private String password;

    /**
     * Converts a given User into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedUser(ReadOnlyUser source) {
        userId = source.getUserId();
        salt = source.getSalt();
        password = source.getPassword();
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     */
    public User toModelType(){
        return new User(this.userId, this.salt, this.password);
    }
}
