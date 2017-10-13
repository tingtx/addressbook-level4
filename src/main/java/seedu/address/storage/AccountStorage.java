package seedu.address.storage;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAccount;

import java.io.FileNotFoundException;
import java.util.Optional;

public interface AccountStorage {

    /**
     * Returns the file path of the data file.
     */
    String getAccountFilePath();

    /**
     * Returns AddressBook data as a {@link seedu.address.model.ReadOnlyAccount}.
     * Returns {@code Optional.empty()} if storage file is not found.
     */
    Optional<ReadOnlyAccount> readAccount() throws FileNotFoundException, DataConversionException;

    /**
     * @see #getAccountFilePath()
     */
    Optional<ReadOnlyAccount> readAccount(String filePath);

    /**
     * Saves the given {@link ReadOnlyAccount} to the storage.
     *
     * @param addressBook cannot be null.
     */
    void saveAccount(ReadOnlyAccount addressBook);

    /**
     * @see #saveAccount(ReadOnlyAccount)
     */
    void saveAccount(ReadOnlyAccount addressBook, String filePath);

    /**
     * @see #saveAddressBook(ReadOnlyAddressBook)
     */
}
