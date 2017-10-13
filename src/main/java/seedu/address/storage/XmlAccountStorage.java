package seedu.address.storage;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAccount;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class XmlAccountStorage implements AccountStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlAddressBookStorage.class);

    private String filePath;

    public XmlAccountStorage(String filePath){
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getAccountFilePath() {
        return null;
    }

    @Override
    public Optional<ReadOnlyAccount> readAccount() throws FileNotFoundException, DataConversionException {
        requireNonNull(filePath);

        File accountFile = new File(filePath);
        if (!accountFile.exists()) {
            logger.info("AddressBook file " + accountFile + " not found");
            return Optional.empty();
        }

        ReadOnlyAccount accountOptional = XmlFileStorage.loadAccountsFromSaveFile(new File(filePath));

        return Optional.of(accountOptional);
    }

    @Override
    public Optional<ReadOnlyAccount> readAccount(String filePath) {
        return null;
    }

    @Override
    public void saveAccount(ReadOnlyAccount addressBook) {

    }

    @Override
    public void saveAccount(ReadOnlyAccount addressBook, String filePath) {

    }
}
