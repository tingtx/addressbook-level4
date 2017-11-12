package seedu.address.logic.commands;

//@@author quanle1994

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Config;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.Logic;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.lockmodelstub.ModelStub;
import seedu.address.logic.currentuser.CurrentUserDetails;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
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
        LogoutCommand logoutCommand = getLogoutCommand(modelStub);
        CommandResult commandResult = logoutCommand.execute();
        assertEquals(LogoutCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
        assertTrue(logoutCommand.isPrivateEncryptionSuccessful());
        assertTrue(logoutCommand.isPublicEncryptionSuccessful());
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
    }
}
