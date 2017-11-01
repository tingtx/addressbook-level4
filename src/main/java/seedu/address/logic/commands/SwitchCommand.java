package seedu.address.logic.commands;

import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * switch between the addressbook and eventbook tab
 */
public class SwitchCommand extends Command {

    public static final String COMMAND_WORD = "switch";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switch between the addressbook and eventbook UI tab";

    public static final String MESSAGE_SUCCESS = "Switched to the other tab";

    private final TabPane tabPane;

    public SwitchCommand(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() throws CommandException {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        int selectedIndex = selectionModel.getSelectedIndex();
        if (selectedIndex == 0) {
            selectedIndex = 1;
        } else {
            selectedIndex = 0;
        }
        selectionModel.select(selectedIndex);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
