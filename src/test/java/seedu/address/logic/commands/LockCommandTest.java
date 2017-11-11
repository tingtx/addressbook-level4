package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.digestutil.HashDigest;
import seedu.address.commons.util.digestutil.HexCode;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.Logic;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Account;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAccount;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyEventBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.alias.exceptions.DuplicateAliasException;
import seedu.address.model.alias.exceptions.UnknownCommandException;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.group.Group;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.exceptions.UnrecognisedParameterException;
import seedu.address.model.tag.Tag;
import seedu.address.model.user.ReadOnlyUser;
import seedu.address.model.user.User;
import seedu.address.model.user.exceptions.DuplicateUserException;
import seedu.address.model.user.exceptions.UserNotFoundException;
import seedu.address.storage.Storage;
import seedu.address.ui.UiManager;

//@@author quanle1994
public class LockCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddEventCommand(null);
    }

    @Test
    public void execute_lockSuccessful() throws Exception {
        ModelStubAcceptingUserAdded modelStub = new ModelStubAcceptingUserAdded();

        CommandResult commandResult = getLockCommand("test", "123", modelStub).execute();
        assertEquals(LockCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
        String userIdHash = new String(new HashDigest().getHashDigest("test"));
        String userIdHex = new HexCode().getHexFormat(userIdHash);
        assertEquals(userIdHex, modelStub.userAdded.getUserId());
        File file = new File("data/" + userIdHex.substring(0, 10) + ".encrypted");
        assertTrue(file.exists());
        file.delete();

        commandResult = getLockCommand("lequangquan", "123", modelStub).execute();
        assertEquals(LockCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
        userIdHash = new String(new HashDigest().getHashDigest("lequangquan"));
        userIdHex = new HexCode().getHexFormat(userIdHash);
        assertEquals(userIdHex, modelStub.userAdded.getUserId());
        file = new File("data/" + userIdHex.substring(0, 10) + ".encrypted");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void execute_throwDuplicateUserException() throws Exception {
        ModelStubThrowingDuplicateUserException modelStub = new ModelStubThrowingDuplicateUserException();

        thrown.expect(CommandException.class);
        thrown.expectMessage(LockCommand.MESSAGE_EXISTING_USER);

        getLockCommand("test", "123456", modelStub).execute();
    }

    /**
     * Generates a new LockCommand with the details of the given event.
     */
    private LockCommand getLockCommand(String userName, String password, Model model) {
        LockCommand command = new LockCommand(userName, password);
        UserPrefs userPrefs = new UserPrefs();
        Config config = new Config();
        Logic logic = null;
        command.setData(model, new CommandHistory(), new UndoRedoStack(), new Config(),
                new UiManager(logic, config, userPrefs));
        return command;
    }

    /**
     * A Model stub that always throw a DuplicateEventException when trying to add a event.
     */
    private class ModelStubThrowingDuplicateUserException extends LockCommandTest.ModelStub {
        @Override
        public void persistUserAccount(ReadOnlyUser event) throws DuplicateUserException {
            throw new DuplicateUserException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public ReadOnlyAccount getAccount() {
            return new Account();
        }
    }

    private class ModelStubAcceptingUserAdded extends LockCommandTest.ModelStub {
        private User userAdded;

        @Override
        public void persistUserAccount(ReadOnlyUser user) throws DuplicateUserException {
            userAdded = new User(user);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public ReadOnlyAccount getAccount() {
            return new Account();
        }
    }

    private class ModelStub implements Model {
        @Override
        public void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void exportAddressBook() throws ParserConfigurationException,
                IOException, SAXException, TransformerException {

        }

        @Override
        public ReadOnlyAccount getAccount() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void transferData() {
            fail("This method should not be called.");
        }

        @Override
        public void transferDataWithDefault() {
            fail("This method should not be called.");
        }

        @Override
        public void deleteEncryptedContacts(String substring) {
            fail("This method should not be called.");
        }

        @Override
        public void releaseEncryptedContacts(String fileName) throws DataConversionException, DuplicatePersonException,
                IOException {
            fail("This method should not be called.");
        }

        @Override
        public UserPrefs getUserPrefs() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void refreshAddressBook() throws IOException, DataConversionException, DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void emptyPersonList(ObservableList<ReadOnlyPerson> list) throws PersonNotFoundException, IOException,
                DataConversionException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ReadOnlyPerson> getListLength() throws IOException, DataConversionException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void orderList(String parameter) throws UnrecognisedParameterException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) throws PersonNotFoundException, DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredListToShowAll() {
            fail("This method should not be called");
        }

        @Override
        public ObservableList<Group> getGroupList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ArrayList<ArrayList<String>> getCommands() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public String getAliasForCommand(String commandName) {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void resetEventData(ReadOnlyEventBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyEventBook getEventBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void exportEventBook() throws FileNotFoundException, ParserConfigurationException,
                IOException, SAXException, TransformerException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteEvent(ReadOnlyEvent target) throws EventNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void addEvent(ReadOnlyEvent event) throws DuplicateEventException {
            fail("This method should not be called.");
        }

        @Override
        public void updateEvent(ReadOnlyEvent target, ReadOnlyEvent editedEvent) throws DuplicateEventException,
                EventNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ReadOnlyEvent> getFilteredEventList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredEventList(Predicate<ReadOnlyEvent> predicate) {
            fail("This method should not be called");
        }

        @Override
        public void orderEventList(String parameter) throws UnrecognisedParameterException {
            fail("This method should not be called.");
        }

        @Override
        public void setAlias(String command, String alias) throws UnknownCommandException, DuplicateAliasException {
            fail("This method should not be called.");
        }

        @Override
        public void persistUserAccount(ReadOnlyUser user) throws DuplicateUserException {
            fail("This method should not be called.");
        }

        @Override
        public User getUserFromIdAndPassword(String userName, String password) throws UserNotFoundException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteUser(String userName, String saltedPasswordHex) throws UserNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public String retrieveSaltFromStorage(String userId) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void setUserStorage(Storage userStorage) {
            fail("This method should not be called.");
        }
    }
}
