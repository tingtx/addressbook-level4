//@@author keloysiusmak
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.commands.TransferCommand.MESSAGE_TRANSFER_ERROR;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Config;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;


public class TransferCommandTest {

    private TransferCommand transferCommand;
    private CommandHistory history;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        history = new CommandHistory();
        transferCommand = new TransferCommand();
        transferCommand.setData(model, history, new UndoRedoStack(), new Config());
    }

    @Test
    public void execute_transfer_success() {
        CommandResult result = transferCommand.execute();
        assertEquals(MESSAGE_TRANSFER_ERROR, result.feedbackToUser);
    }
}