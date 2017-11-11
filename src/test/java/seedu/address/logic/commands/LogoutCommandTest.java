package seedu.address.logic.commands;

//@@author quanle1994

import static junit.framework.TestCase.assertEquals;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.Logic;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.lockmodelstub.ModelStub;
import seedu.address.logic.currentuser.CurrentUserDetails;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.ui.UiManager;

/**
 * Test Logout Command
 */
public class LogoutCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void execute_logoutBeforeLoginException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(LogoutCommand.MESSAGE_LOGOUT_ERROR);

        getLogoutCommand(new ModelStubAcceptingUserAdded()).execute();
    }

    @Test
    public void execute_logoutSuccessful() throws Exception {
        ModelStubAcceptingUserAdded modelStub = new ModelStubAcceptingUserAdded();
        CurrentUserDetails.setCurrentUser("test", "", "", "");
        CommandResult commandResult = getLogoutCommand(modelStub).execute();
        assertEquals(LogoutCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
    }

    private LogoutCommand getLogoutCommand(Model model) {
        LogoutCommand command = new LogoutCommand();
        UserPrefs userPrefs = new UserPrefs();
        Config config = new Config();
        Logic logic = null;
        command.setData(model, new CommandHistory(), new UndoRedoStack(), new Config(),
                new UiManager(logic, config, userPrefs));
        return command;
    }

    private class ModelStubAcceptingUserAdded extends ModelStub {
        @Override
        public void emptyPersonList(ObservableList<ReadOnlyPerson> list) throws PersonNotFoundException, IOException,
                DataConversionException {
        }

        @Override
        public void refreshAddressBook() throws IOException, DataConversionException, DuplicatePersonException {
            return;
        }

        @Override
        public ObservableList<ReadOnlyPerson> getListLength() throws IOException, DataConversionException {
            return null;
        }
    }
}
