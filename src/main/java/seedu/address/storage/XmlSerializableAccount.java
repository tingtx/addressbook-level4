package seedu.address.storage;

import seedu.address.model.ReadOnlyAccount;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * An Immutable Account that is serializable to XML format
 */
@XmlRootElement(name = "account")
public class XmlSerializableAccount implements ReadOnlyAccount {

    @XmlElement
    private List<XmlAdaptedUser> users;

    public List<XmlAdaptedUser> getUsers() {
        return users;
    }
}
