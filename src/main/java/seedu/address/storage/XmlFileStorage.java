package seedu.address.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores addressbook or eventbook data in an XML file
 */
public class XmlFileStorage {

    /**
     * Saves the given eventbook data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableEventBook eventBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, eventBook);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Saves the given addressbook data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableAddressBook addressBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, addressBook);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Saves the given account data to the specified file.
     */
    public static void saveAccountToFile(File file, XmlSerializableAccount account)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, account);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns event book in the file or an empty event book
     */
    public static XmlSerializableEventBook loadEventDataFromSaveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableEventBook.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableAddressBook loadDataFromSaveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableAddressBook.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * Returns accounts in the file or an empty account list
     */
    public static XmlSerializableAccount loadAccountFromSaveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableAccount.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * Export XML Data into CSV file
     */
    public static void exportGeneralbook(String source, String destination, String style, String objectElement)
            throws FileNotFoundException, ParserConfigurationException,
            IOException, SAXException, TransformerException {
        try {
            XmlUtil.exportDataToFile(source, destination, style, objectElement);
        } catch (ParserConfigurationException pce) {
            throw new ParserConfigurationException();
        } catch (SAXException se) {
            throw new SAXException();
        } catch (TransformerException te) {
            throw new TransformerException(te);
        }
    }
}
