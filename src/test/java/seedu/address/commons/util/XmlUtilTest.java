package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.AddressBook;
import seedu.address.model.EventBook;
import seedu.address.storage.XmlSerializableAddressBook;
import seedu.address.storage.XmlSerializableEventBook;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.EventBookBuilder;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TestUtil;

public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_ADDRESSBOOK_FILE = new File(TEST_DATA_FOLDER + "validAddressBook.xml");
    private static final File TEMP_ADDRESSBOOK_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempAddressBook.xml"));
    private static final File VALID_EVENTBOOK_FILE = new File(TEST_DATA_FOLDER + "validEventBook.xml");
    private static final File TEMP_EVENTBOOK_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempEventBook.xml"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //==============================Addressbook==========================================
    @Test
    public void getAddressBookDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, AddressBook.class);
    }

    @Test
    public void getAddressBookDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_ADDRESSBOOK_FILE, null);
    }

    @Test
    public void getAddressBookDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, AddressBook.class);
    }

    @Test
    public void getAddressBookDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, AddressBook.class);
    }

    @Test
    public void getAddressBookDataFromFile_validFile_validResult() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(VALID_ADDRESSBOOK_FILE,
                XmlSerializableAddressBook.class);
        assertEquals(9, dataFromFile.getPersonList().size());
        assertEquals(0, dataFromFile.getTagList().size());
    }

    @Test
    public void saveAddressBookDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new AddressBook());
    }

    @Test
    public void saveAddressBookDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_ADDRESSBOOK_FILE, null);
    }

    @Test
    public void saveAddressBookDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new AddressBook());
    }

    @Test
    public void saveAddressBookDataToFile_validFile_dataSaved() throws Exception {
        TEMP_ADDRESSBOOK_FILE.createNewFile();
        XmlSerializableAddressBook dataToWrite = new XmlSerializableAddressBook(new AddressBook());
        XmlUtil.saveDataToFile(TEMP_ADDRESSBOOK_FILE, dataToWrite);
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(TEMP_ADDRESSBOOK_FILE,
                XmlSerializableAddressBook.class);
        assertEquals((new AddressBook(dataToWrite)).toString(), (new AddressBook(dataFromFile)).toString());
        //TODO: use equality instead of string comparisons

        AddressBookBuilder builder = new AddressBookBuilder(new AddressBook());
        dataToWrite = new XmlSerializableAddressBook(
                builder.withPerson(new PersonBuilder().build()).withTag("Friends").build());

        XmlUtil.saveDataToFile(TEMP_ADDRESSBOOK_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_ADDRESSBOOK_FILE, XmlSerializableAddressBook.class);
        assertEquals((new AddressBook(dataToWrite)).toString(), (new AddressBook(dataFromFile)).toString());
    }
    //=======================================================================================================

    //============================EventBook===============================================================
    @Test
    public void getEventBookDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, EventBook.class);
    }

    @Test
    public void getEventBookDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_EVENTBOOK_FILE, null);
    }

    @Test
    public void getEventBookDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, EventBook.class);
    }

    @Test
    public void getEventBookDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, EventBook.class);
    }

    @Test
    public void getEventBookDataFromFile_validFile_validResult() throws Exception {
        XmlSerializableEventBook dataFromFile = XmlUtil.getDataFromFile(VALID_EVENTBOOK_FILE,
                XmlSerializableEventBook.class);
        assertEquals(6, dataFromFile.getEventList().size());
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new EventBook());
    }

    @Test
    public void saveEventBookDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_EVENTBOOK_FILE, null);
    }

    @Test
    public void saveEventBookDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new EventBook());
    }

    @Test
    public void saveEventBookDataToFile_validFile_dataSaved() throws Exception {
        TEMP_EVENTBOOK_FILE.createNewFile();
        XmlSerializableEventBook dataToWrite = new XmlSerializableEventBook(new EventBook());
        XmlUtil.saveDataToFile(TEMP_EVENTBOOK_FILE, dataToWrite);
        XmlSerializableEventBook dataFromFile = XmlUtil.getDataFromFile(TEMP_EVENTBOOK_FILE,
                XmlSerializableEventBook.class);
        assertEquals((new EventBook(dataToWrite)).toString(), (new EventBook(dataFromFile)).toString());
        //TODO: use equality instead of string comparisons

        EventBookBuilder builder = new EventBookBuilder(new EventBook());
        dataToWrite = new XmlSerializableEventBook(builder.withEvent(new EventBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_EVENTBOOK_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_EVENTBOOK_FILE, XmlSerializableEventBook.class);
        assertEquals((new EventBook(dataToWrite)).toString(), (new EventBook(dataFromFile)).toString());
    }
    //=======================================================================================================
}
