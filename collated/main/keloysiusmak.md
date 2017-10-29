# keloysiusmak
###### /java/seedu/address/ui/ViewAliasCard.java
``` java
package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;


/**
 * An UI component that displays information of a {@code Person}.
 */
public class ViewAliasCard extends UiPart<Region> {

    private static final String FXML = "ViewAliasListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final String command;

    @FXML
    private HBox cardPane;
    @FXML
    private Label defaultAlias;
    @FXML
    private Label alias;
    @FXML
    private Label id;

    public ViewAliasCard(String command, String commandWord, Logic logic) {
        super(FXML);
        this.command = command;
        id.setText(command);

        defaultAlias.setText("Default Alias : " + commandWord);
        alias.setText("Set Alias : " + logic.getAliasForCommand(commandWord));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        ViewAliasCard card = (ViewAliasCard) other;
        return id.getText().equals(card.id.getText())
                && command.equals(card.command);
    }
}
```
###### /java/seedu/address/ui/ViewAliasListPanel.java
``` java
package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;

/**
 * Panel containing the list of commands, and their command words and aliases
 */
class ViewAliasListPanel extends UiPart<Region> {
    private static final String FXML = "ViewAliasListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ViewAliasListPanel.class);

    @javafx.fxml.FXML
    private ListView<ViewAliasCard> viewAliasListView;

    public ViewAliasListPanel(ArrayList<ArrayList<String>> commandList, Logic logic) {
        super(FXML);
        setCommands(commandList, logic);
    }

    private void setCommands(ArrayList<ArrayList<String>> commandList, Logic logic) {

        ArrayList<ViewAliasCard> mappedList = new ArrayList<ViewAliasCard>();
        for (int i = 0; i < commandList.size(); i++) {
            ArrayList<String> command = commandList.get(i);
            ViewAliasCard v = new ViewAliasCard(command.get(0), command.get(1), logic);
            mappedList.add(v);
        }
        ObservableList<ViewAliasCard> convertedList = FXCollections.observableArrayList(mappedList);
        viewAliasListView.setItems(convertedList);
        viewAliasListView.setCellFactory(listView -> new ViewAliasListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class ViewAliasListViewCell extends ListCell<ViewAliasCard> {
        @Override
        protected void updateItem(ViewAliasCard command, boolean empty) {
            super.updateItem(command, empty);

            if (empty || command == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(command.getRoot());
            }
        }
    }
}
```
###### /java/seedu/address/ui/ViewAliasWindow.java
``` java
package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Logic;

/**
 * Controller for a help page
 */
public class ViewAliasWindow extends UiPart<Region> {

    public static final String VIEWALIAS_FILE_PATH = "/docs/ViewAliasGuide.html";

    private static final Logger logger = LogsCenter.getLogger(ViewAliasWindow.class);
    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "ViewAliasWindow.fxml";
    private static final String TITLE = "View Aliases";

    private ViewAliasListPanel viewAliasListPanel;

    @FXML
    private StackPane viewAliasListPanelPlaceholder;

    @FXML
    private final Stage dialogStage;


    public ViewAliasWindow(ArrayList<ArrayList<String>> c, Logic logic) {
        super(FXML);

        viewAliasListPanel = new ViewAliasListPanel(c, logic);
        viewAliasListPanelPlaceholder.getChildren().add(viewAliasListPanel.getRoot());

        Scene scene = new Scene(getRoot());
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMinHeight(600);
        dialogStage.setMinWidth(400);

        FxViewUtil.setStageIcon(dialogStage, ICON);

        registerAsAnEventHandler(this);
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException <ul>
     *                               <li>
     *                               if this method is called on a thread other than the JavaFX Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing aliases for the application.");
        dialogStage.showAndWait();
    }

}
```
###### /java/seedu/address/commons/core/AliasSettings.java
``` java
package seedu.address.commons.core;

import static seedu.address.commons.core.Messages.MESSAGE_DUPLICATE_ALIAS;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteEventCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditEventCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindEventCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListEventCommand;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.commands.OrderEventCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetAliasCommand;
import seedu.address.logic.commands.SetThemeCommand;
import seedu.address.logic.commands.SwitchCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.ViewAliasCommand;

import seedu.address.model.alias.Alias;
import seedu.address.model.alias.exceptions.DuplicateAliasException;
import seedu.address.model.alias.exceptions.UnknownCommandException;


/**
 * A Serializable class that contains the Alias settings.
 */
public class AliasSettings implements Serializable {

    private Alias addCommand;
    private Alias clearCommand;
    private Alias deleteCommand;
    private Alias editCommand;
    private Alias exitCommand;
    private Alias findCommand;
    private Alias helpCommand;
    private Alias historyCommand;
    private Alias listCommand;
    private Alias orderCommand;
    private Alias redoCommand;
    private Alias remarkCommand;
    private Alias selectCommand;
    private Alias setAliasCommand;
    private Alias undoCommand;
    private Alias viewAliasCommand;
    private Alias addEventCommand;
    private Alias deleteEventCommand;
    private Alias editEventCommand;
    private Alias listEventCommand;
    private Alias orderEventCommand;
    private Alias findEventCommand;
    private Alias setThemeCommand;
    private Alias switchCommand;
    private HashSet<String> usedAliases;


    public AliasSettings() {
        usedAliases = new HashSet<String>();
        this.addCommand = new Alias(AddCommand.getCommandWord(), "add");
        usedAliases.add("add");
        this.clearCommand = new Alias(ClearCommand.getCommandWord(), "clear");
        usedAliases.add("clear");
        this.deleteCommand = new Alias(DeleteCommand.getCommandWord(), "delete");
        usedAliases.add("delete");
        this.editCommand = new Alias(EditCommand.getCommandWord(), "edit");
        usedAliases.add("edit");
        this.exitCommand = new Alias(ExitCommand.getCommandWord(), "exit");
        usedAliases.add("exit");
        this.findCommand = new Alias(FindCommand.getCommandWord(), "find");
        usedAliases.add("find");
        this.helpCommand = new Alias(HelpCommand.getCommandWord(), "help");
        usedAliases.add("help");
        this.historyCommand = new Alias(HistoryCommand.getCommandWord(), "history");
        usedAliases.add("history");
        this.listCommand = new Alias(ListCommand.getCommandWord(), "list");
        usedAliases.add("list");
        this.orderCommand = new Alias(OrderCommand.getCommandWord(), "order");
        usedAliases.add("order");
        this.redoCommand = new Alias(RedoCommand.getCommandWord(), "redo");
        usedAliases.add("redo");
        this.remarkCommand = new Alias(RemarkCommand.getCommandWord(), "remark");
        usedAliases.add("remark");
        this.selectCommand = new Alias(SelectCommand.getCommandWord(), "select");
        usedAliases.add("select");
        this.setAliasCommand = new Alias(SetAliasCommand.getCommandWord(), "setalias");
        usedAliases.add("setalias");
        this.undoCommand = new Alias(UndoCommand.getCommandWord(), "undo");
        usedAliases.add("undo");
        this.viewAliasCommand = new Alias(ViewAliasCommand.getCommandWord(), "viewalias");
        usedAliases.add("viewalias");
        this.addEventCommand = new Alias(AddEventCommand.getCommandWord(), "addevent");
        usedAliases.add("addevent");
        this.deleteEventCommand = new Alias(DeleteEventCommand.getCommandWord(), "deleteevent");
        usedAliases.add("deleteevent");
        this.editEventCommand = new Alias(EditEventCommand.getCommandWord(), "editevent");
        usedAliases.add("editevent");
        this.listEventCommand = new Alias(ListEventCommand.getCommandWord(), "listevent");
        usedAliases.add("orderevent");
        this.orderEventCommand = new Alias(OrderEventCommand.getCommandWord(), "orderevent");
        usedAliases.add("findevent");
        this.findEventCommand = new Alias(FindEventCommand.getCommandWord(), "findevent");
        usedAliases.add("settheme");
        this.setThemeCommand = new Alias(SetThemeCommand.getCommandWord(), "settheme");
        usedAliases.add("switch");
        this.switchCommand = new Alias(SwitchCommand.getCommandWord(), "switch");
    }

    public AliasSettings(String addCommand, String clearCommand, String deleteCommand, String editCommand,
                         String exitCommand, String findCommand, String helpCommand,
                         String historyCommand, String listCommand, String orderCommand, String redoCommand,
                         String remarkCommand, String selectCommand, String setAliasCommand, String undoCommand,
                         String viewAliasCommand, String addEventCommand, String deleteEventCommand,
                         String editEventCommand, String listEventCommand, String orderEventCommand,
                         String findEventCommand, String setThemeCommand, String switchCommand) {
        this.addCommand = new Alias(AddCommand.getCommandWord(), addCommand);
        usedAliases.add(addCommand);
        this.clearCommand = new Alias(ClearCommand.getCommandWord(), clearCommand);
        usedAliases.add(clearCommand);
        this.deleteCommand = new Alias(DeleteCommand.getCommandWord(), deleteCommand);
        usedAliases.add(deleteCommand);
        this.editCommand = new Alias(EditCommand.getCommandWord(), editCommand);
        usedAliases.add(editCommand);
        this.exitCommand = new Alias(ExitCommand.getCommandWord(), exitCommand);
        usedAliases.add(exitCommand);
        this.findCommand = new Alias(FindCommand.getCommandWord(), findCommand);
        usedAliases.add(findCommand);
        this.helpCommand = new Alias(HelpCommand.getCommandWord(), helpCommand);
        usedAliases.add(helpCommand);
        this.historyCommand = new Alias(HistoryCommand.getCommandWord(), historyCommand);
        usedAliases.add(historyCommand);
        this.listCommand = new Alias(ListCommand.getCommandWord(), listCommand);
        usedAliases.add(listCommand);
        this.orderCommand = new Alias(OrderCommand.getCommandWord(), orderCommand);
        usedAliases.add(orderCommand);
        this.redoCommand = new Alias(RedoCommand.getCommandWord(), redoCommand);
        usedAliases.add(redoCommand);
        this.remarkCommand = new Alias(RemarkCommand.getCommandWord(), remarkCommand);
        usedAliases.add(remarkCommand);
        this.selectCommand = new Alias(SelectCommand.getCommandWord(), selectCommand);
        usedAliases.add(selectCommand);
        this.setAliasCommand = new Alias(SetAliasCommand.getCommandWord(), setAliasCommand);
        usedAliases.add(setAliasCommand);
        this.undoCommand = new Alias(UndoCommand.getCommandWord(), undoCommand);
        usedAliases.add(undoCommand);
        this.viewAliasCommand = new Alias(ViewAliasCommand.getCommandWord(), viewAliasCommand);
        usedAliases.add(viewAliasCommand);
        this.addEventCommand = new Alias(AddEventCommand.getCommandWord(), addEventCommand);
        usedAliases.add(addEventCommand);
        this.deleteEventCommand = new Alias(DeleteEventCommand.getCommandWord(), deleteEventCommand);
        usedAliases.add(deleteEventCommand);
        this.editEventCommand = new Alias(EditEventCommand.getCommandWord(), editEventCommand);
        usedAliases.add(editEventCommand);
        this.listEventCommand = new Alias(ListEventCommand.getCommandWord(), listEventCommand);
        usedAliases.add(orderEventCommand);
        this.orderEventCommand = new Alias(OrderEventCommand.getCommandWord(), orderEventCommand);
        usedAliases.add(findEventCommand);
        this.findEventCommand = new Alias(FindEventCommand.getCommandWord(), findEventCommand);
        usedAliases.add(setThemeCommand);
        this.setThemeCommand = new Alias(SetThemeCommand.getCommandWord(), setThemeCommand);
        usedAliases.add(switchCommand);
        this.switchCommand = new Alias(SwitchCommand.getCommandWord(), switchCommand);
    }

    public Alias getAddCommand() {
        return addCommand;
    }

    public Alias getClearCommand() {
        return clearCommand;
    }

    public Alias getDeleteCommand() {
        return deleteCommand;
    }

    public Alias getEditCommand() {
        return editCommand;
    }

    public Alias getExitCommand() {
        return exitCommand;
    }

    public Alias getFindCommand() {
        return findCommand;
    }

    public Alias getHelpCommand() {
        return helpCommand;
    }

    public Alias getHistoryCommand() {
        return historyCommand;
    }

    public Alias getListCommand() {
        return listCommand;
    }

    public Alias getOrderCommand() {
        return orderCommand;
    }

    public Alias getRedoCommand() {
        return redoCommand;
    }

    public Alias getRemarkCommand() {
        return remarkCommand;
    }

    public Alias getSelectCommand() {
        return selectCommand;
    }

    public Alias getSetAliasCommand() {
        return setAliasCommand;
    }

    public Alias getUndoCommand() {
        return undoCommand;
    }

    public Alias getViewAliasCommand() {
        return viewAliasCommand;
    }

    public Alias getAddEventCommand() {
        return addEventCommand;
    }

    public Alias getDeleteEventCommand() {
        return deleteEventCommand;
    }

    public Alias getEditEventCommand() {
        return editEventCommand;
    }

    public Alias getListEventCommand() {
        return listEventCommand;
    }

    public Alias getOrderEventCommand() {
        return orderEventCommand;
    }

    public Alias getFindEventCommand() {
        return findEventCommand;
    }

    public Alias getSetThemeCommand() {
        return setThemeCommand;
    }

    public Alias getSwitchCommand() {
        return switchCommand;
    }

    public void setAlias(String command, String alias) throws DuplicateAliasException, UnknownCommandException {
        if (usedAliases.contains(alias)) {
            throw new DuplicateAliasException(MESSAGE_DUPLICATE_ALIAS);
        } else if (command.equals(AddCommand.getCommandWord())) {
            if (!this.addCommand.getAlias().equals("add")) {
                usedAliases.remove(this.addCommand.getAlias());
            }
            usedAliases.add(alias);
            this.addCommand = new Alias(AddCommand.getCommandWord(), alias);
        } else if (command.equals(ClearCommand.getCommandWord())) {
            if (!this.clearCommand.getAlias().equals("clear")) {
                usedAliases.remove(this.clearCommand.getAlias());
            }
            usedAliases.add(alias);
            this.clearCommand = new Alias(ClearCommand.getCommandWord(), alias);
        } else if (command.equals(DeleteCommand.getCommandWord())) {
            if (!this.deleteCommand.getAlias().equals("delete")) {
                usedAliases.remove(this.deleteCommand.getAlias());
            }
            usedAliases.add(alias);
            this.deleteCommand = new Alias(DeleteCommand.getCommandWord(), alias);
        } else if (command.equals(EditCommand.getCommandWord())) {
            if (!this.editCommand.getAlias().equals("edit")) {
                usedAliases.remove(this.editCommand.getAlias());
            }
            this.editCommand = new Alias(EditCommand.getCommandWord(), alias);
        } else if (command.equals(ExitCommand.getCommandWord())) {
            if (!this.editCommand.getAlias().equals("exit")) {
                usedAliases.remove(this.editCommand.getAlias());
            }
            usedAliases.add(alias);
            this.exitCommand = new Alias(ExitCommand.getCommandWord(), alias);
        } else if (command.equals(FindCommand.getCommandWord())) {
            if (!this.findCommand.getAlias().equals("find")) {
                usedAliases.remove(this.findCommand.getAlias());
            }
            usedAliases.add(alias);
            this.findCommand = new Alias(FindCommand.getCommandWord(), alias);
        } else if (command.equals(HelpCommand.getCommandWord())) {
            if (!this.helpCommand.getAlias().equals("help")) {
                usedAliases.remove(this.helpCommand.getAlias());
            }
            usedAliases.add(alias);
            this.helpCommand = new Alias(HelpCommand.getCommandWord(), alias);
        } else if (command.equals(HistoryCommand.getCommandWord())) {
            if (!this.historyCommand.getAlias().equals("history")) {
                usedAliases.remove(this.historyCommand.getAlias());
            }
            usedAliases.add(alias);
            this.historyCommand = new Alias(HistoryCommand.getCommandWord(), alias);
        } else if (command.equals(ListCommand.getCommandWord())) {
            if (!this.listCommand.getAlias().equals("list")) {
                usedAliases.remove(this.listCommand.getAlias());
            }
            usedAliases.add(alias);
            this.listCommand = new Alias(ListCommand.getCommandWord(), alias);
        } else if (command.equals(OrderCommand.getCommandWord())) {
            if (!this.orderCommand.getAlias().equals("order")) {
                usedAliases.remove(this.orderCommand.getAlias());
            }
            usedAliases.add(alias);
            this.orderCommand = new Alias(OrderCommand.getCommandWord(), alias);
        } else if (command.equals(RedoCommand.getCommandWord())) {
            if (!this.redoCommand.getAlias().equals("redo")) {
                usedAliases.remove(this.redoCommand.getAlias());
            }
            usedAliases.add(alias);
            this.redoCommand = new Alias(RedoCommand.getCommandWord(), alias);
        } else if (command.equals(RemarkCommand.getCommandWord())) {
            if (!this.remarkCommand.getAlias().equals("remark")) {
                usedAliases.remove(this.remarkCommand.getAlias());
            }
            usedAliases.add(alias);
            this.remarkCommand = new Alias(RemarkCommand.getCommandWord(), alias);
        } else if (command.equals(SelectCommand.getCommandWord())) {
            if (!this.selectCommand.getAlias().equals("select")) {
                usedAliases.remove(this.selectCommand.getAlias());
            }
            usedAliases.add(alias);
            this.selectCommand = new Alias(SelectCommand.getCommandWord(), alias);
        } else if (command.equals(SetAliasCommand.getCommandWord())) {
            if (!this.setAliasCommand.getAlias().equals("setalias")) {
                usedAliases.remove(this.setAliasCommand.getAlias());
            }
            usedAliases.add(alias);
            this.setAliasCommand = new Alias(SetAliasCommand.getCommandWord(), alias);
        } else if (command.equals(UndoCommand.getCommandWord())) {
            if (!this.undoCommand.getAlias().equals("undo")) {
                usedAliases.remove(this.undoCommand.getAlias());
            }
            usedAliases.add(alias);
            this.undoCommand = new Alias(UndoCommand.getCommandWord(), alias);
        } else if (command.equals(ViewAliasCommand.getCommandWord())) {
            if (!this.viewAliasCommand.getAlias().equals("viewalias")) {
                usedAliases.remove(this.viewAliasCommand.getAlias());
            }
            usedAliases.add(alias);
            this.viewAliasCommand = new Alias(ViewAliasCommand.getCommandWord(), alias);
        } else if (command.equals(AddEventCommand.getCommandWord())) {
            if (!this.addEventCommand.getAlias().equals("addevent")) {
                usedAliases.remove(this.addEventCommand.getAlias());
            }
        } else if (command.equals(DeleteEventCommand.getCommandWord())) {
            if (!this.deleteEventCommand.getAlias().equals("deleteevent")) {
                usedAliases.remove(this.deleteEventCommand.getAlias());
            }
        } else if (command.equals(EditEventCommand.getCommandWord())) {
            if (!this.editEventCommand.getAlias().equals("editevent")) {
                usedAliases.remove(this.editEventCommand.getAlias());
            }
        } else if (command.equals(ListEventCommand.getCommandWord())) {
            if (!this.listEventCommand.getAlias().equals("listevent")) {
                usedAliases.remove(this.listEventCommand.getAlias());
            }
        } else if (command.equals(OrderEventCommand.getCommandWord())) {
            if (!this.orderEventCommand.getAlias().equals("orderevent")) {
                usedAliases.remove(this.orderEventCommand.getAlias());
            }
        } else if (command.equals(FindEventCommand.getCommandWord())) {
            if (!this.findEventCommand.getAlias().equals("findevent")) {
                usedAliases.remove(this.findEventCommand.getAlias());
            }
        } else if (command.equals(SetThemeCommand.getCommandWord())) {
            if (!this.setThemeCommand.getAlias().equals("settheme")) {
                usedAliases.remove(this.setThemeCommand.getAlias());
            }
        } else if (command.equals(SwitchCommand.getCommandWord())) {
            if (!this.switchCommand.getAlias().equals("switch")) {
                usedAliases.remove(this.switchCommand.getAlias());
            }
        } else {
            throw new UnknownCommandException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AliasSettings)) { //this handles null as well.
            return false;
        }

        AliasSettings o = (AliasSettings) other;

        return Objects.equals(addCommand, o.getAddCommand())
                && Objects.equals(clearCommand, o.getClearCommand())
                && Objects.equals(deleteCommand, o.getDeleteCommand())
                && Objects.equals(editCommand, o.getEditCommand())
                && Objects.equals(exitCommand, o.getExitCommand())
                && Objects.equals(findCommand, o.getFindCommand())
                && Objects.equals(helpCommand, o.getHelpCommand())
                && Objects.equals(historyCommand, o.getHistoryCommand())
                && Objects.equals(listCommand, o.getListCommand())
                && Objects.equals(orderCommand, o.getOrderCommand())
                && Objects.equals(redoCommand, o.getRedoCommand())
                && Objects.equals(remarkCommand, o.getRemarkCommand())
                && Objects.equals(selectCommand, o.getSelectCommand())
                && Objects.equals(setAliasCommand, o.getSetAliasCommand())
                && Objects.equals(undoCommand, o.getUndoCommand())
                && Objects.equals(viewAliasCommand, o.getViewAliasCommand())
                && Objects.equals(addEventCommand, o.getAddEventCommand())
                && Objects.equals(deleteEventCommand, o.getDeleteEventCommand())
                && Objects.equals(editEventCommand, o.getEditEventCommand())
                && Objects.equals(listEventCommand, o.getListEventCommand())
                && Objects.equals(orderEventCommand, o.getOrderEventCommand())
                && Objects.equals(findEventCommand, o.getFindEventCommand())
                && Objects.equals(setThemeCommand, o.getSetThemeCommand())
                && Objects.equals(switchCommand, o.getSwitchCommand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.addCommand, this.clearCommand, this.deleteCommand, this.editCommand,
                this.exitCommand, this.findCommand, this.helpCommand, this.historyCommand, this.listCommand,
                this.orderCommand, this.redoCommand, this.remarkCommand, this.selectCommand, this.setAliasCommand,
                this.undoCommand, this.viewAliasCommand, this.addEventCommand, this.deleteEventCommand,
                this.editEventCommand, this.listEventCommand, this.orderEventCommand,
                this.findEventCommand, this.switchCommand);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Add Command : " + addCommand.getAlias() + "\n");
        sb.append("Clear Command : " + clearCommand.getAlias() + "\n");
        sb.append("Delete Command : " + deleteCommand.getAlias() + "\n");
        sb.append("Edit Command : " + editCommand.getAlias() + "\n");
        sb.append("Exit Command : " + exitCommand.getAlias() + "\n");
        sb.append("Find Command : " + findCommand.getAlias() + "\n");
        sb.append("Help Command : " + helpCommand.getAlias() + "\n");
        sb.append("History Command : " + historyCommand.getAlias() + "\n");
        sb.append("List Command : " + listCommand.getAlias() + "\n");
        sb.append("Order Command : " + orderCommand.getAlias() + "\n");
        sb.append("Redo Command : " + redoCommand.getAlias() + "\n");
        sb.append("Remark Command : " + remarkCommand.getAlias() + "\n");
        sb.append("Select Command : " + selectCommand.getAlias() + "\n");
        sb.append("Set Alias Command : " + setAliasCommand.getAlias() + "\n");
        sb.append("Undo Command : " + undoCommand.getAlias() + "\n");
        sb.append("View Alias Command : " + viewAliasCommand.getAlias() + "\n");
        sb.append("Add Event Command : " + addEventCommand.getAlias() + "\n");
        sb.append("Delete Event Command : " + deleteEventCommand.getAlias() + "\n");
        sb.append("Edit Event Command : " + editEventCommand.getAlias() + "\n");
        sb.append("List Event Command : " + listEventCommand.getAlias() + "\n");
        sb.append("Order Event Command : " + orderEventCommand.getAlias() + "\n");
        sb.append("Find Event Command : " + findEventCommand.getAlias() + "\n");
        sb.append("Set Theme Command : " + setThemeCommand.getAlias() + "\n");
        sb.append("Switch Command : " + switchCommand.getAlias() + "\n");

        return sb.toString();
    }
}
```
###### /java/seedu/address/commons/events/ui/ChangedThemeEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of persons
 */
public class ChangedThemeEvent extends BaseEvent {

    public final String theme;

    public ChangedThemeEvent(String setTheme) {
        this.theme = setTheme;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### /java/seedu/address/commons/events/ui/ViewAliasRequestEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view the help page.
 */
public class ViewAliasRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### /java/seedu/address/logic/parser/SetAliasCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_DUPLICATE_ALIAS;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;

import java.util.stream.Stream;

import seedu.address.logic.commands.SetAliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetAliasCommand object
 */
public class SetAliasCommandParser implements Parser<SetAliasCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetAliasCommand
     * and returns an SetAliasCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetAliasCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COMMAND, PREFIX_ALIAS);

        if (!arePrefixesPresent(argMultimap, PREFIX_ALIAS, PREFIX_COMMAND)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetAliasCommand.MESSAGE_USAGE));
        }


        String command = ParserUtil.parseCommand(argMultimap.getValue(PREFIX_COMMAND)).get();
        String alias = ParserUtil.parseAlias(argMultimap.getValue(PREFIX_ALIAS)).get();

        if (!(command.equals("add") || command.equals("clear") || command.equals("delete") || command.equals("edit")
                || command.equals("exit") || command.equals("find") || command.equals("help")
                || command.equals("history") || command.equals("list") || command.equals("order")
                || command.equals("redo") || command.equals("remark") || command.equals("select")
                || command.equals("undo") || command.equals("viewalias") || command.equals("setalias"))) {
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

        if ((alias.equals("add") || alias.equals("clear") || alias.equals("delete") || alias.equals("edit")
                || alias.equals("exit") || alias.equals("find") || alias.equals("help") || alias.equals("history")
                || alias.equals("list") || alias.equals("order") || alias.equals("redo") || alias.equals("remark")
                || alias.equals("select") || alias.equals("undo") || alias.equals("viewalias")
                || alias.equals("setalias"))) {
            throw new ParseException(MESSAGE_DUPLICATE_ALIAS);
        }

        return new SetAliasCommand(command, alias);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### /java/seedu/address/logic/parser/SetThemeCommandParser.java
``` java
package seedu.address.logic.parser;

import seedu.address.logic.commands.SetThemeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetAliasCommand object
 */
public class SetThemeCommandParser implements Parser<SetThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetAliasCommand
     * and returns an SetAliasCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetThemeCommand parse(String theme) throws ParseException {
        return new SetThemeCommand(theme.trim());
    }
}
```
###### /java/seedu/address/logic/commands/SetThemeCommand.java
``` java
package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.ChangedThemeEvent;

/**
 * Sets a theme for the TunedIn Application
 */
public class SetThemeCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "settheme";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a theme for the .\n"
            + "Parameters: THEME ('summer', 'spring', 'autumn' or 'winter')\n"
            + "Example: " + COMMAND_WORD + " spring";

    public static final String MESSAGE_CHANGED_THEME_SUCCESS = "Changed Theme: %1$s\nYour changes will be shown when "
            + "you restart the application";

    private final String theme;

    public SetThemeCommand() {
        this.theme = "summer";
    }

    public SetThemeCommand(String setTheme) {
        this.theme = setTheme;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        if (!((this.theme.equals("summer"))
                || (this.theme.equals("spring"))
                || (this.theme.equals("winter"))
                || (this.theme.equals("autumn")))) {
            return new CommandResult(String.format(Messages.MESSAGE_WRONG_THEME, this.theme));
        }
        config.setTheme(this.theme);
        EventsCenter.getInstance().post(new ChangedThemeEvent(this.theme));
        return new CommandResult(String.format(MESSAGE_CHANGED_THEME_SUCCESS, this.theme));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetThemeCommand // instanceof handles nulls
                && this.theme.equals(((SetThemeCommand) other).theme)); // state check
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
```
###### /java/seedu/address/logic/commands/ViewAliasCommand.java
``` java
package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ViewAliasRequestEvent;

/**
 * Adds a person to the address book.
 */
public class ViewAliasCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "viewalias";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all aliases.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Opened alias window.";

    @Override
    public CommandResult executeUndoableCommand() {
        EventsCenter.getInstance().post(new ViewAliasRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);

    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
```
###### /java/seedu/address/logic/commands/SetAliasCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_DUPLICATE_ALIAS;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.alias.exceptions.DuplicateAliasException;
import seedu.address.model.alias.exceptions.UnknownCommandException;

/**
 * Sets an alias for a particular command
 */
public class SetAliasCommand extends Command {
    public static final String COMMAND_WORD = "setalias";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets an alias for a command. "
            + "Parameters: "
            + PREFIX_COMMAND + "COMMAND "
            + PREFIX_ALIAS + "ALIAS";

    public static final String MESSAGE_SUCCESS = "Alias has been set.";

    private final String commandAdd;
    private final String toAdd;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyPerson}
     */
    public SetAliasCommand(String command, String alias) {
        if (command == null || alias == null) {
            throw new NullPointerException();
        }
        commandAdd = command;
        toAdd = alias;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        try {
            model.setAlias(commandAdd, toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UnknownCommandException e) {
            throw new CommandException(MESSAGE_UNKNOWN_COMMAND);
        } catch (DuplicateAliasException e) {
            throw new CommandException(MESSAGE_DUPLICATE_ALIAS);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetAliasCommand // instanceof handles nulls
                && toAdd.equals(((SetAliasCommand) other).toAdd));
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
}
```
###### /java/seedu/address/model/alias/Alias.java
``` java
package seedu.address.model.alias;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.Serializable;
import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Alias implements Serializable {

    public static final String MESSAGE_ALIAS_CONSTRAINTS =
            "Aliases should only contain alphanumeric characters and spaces, and it should not be blank";
    private String aliasCommand;
    private String aliasString;

    /**
     * Every field must be present and not null.
     */

    public Alias() {
        this.aliasCommand = null;
        this.aliasString = null;
    }

    public Alias(String aliasCommand, String aliasString) {
        try {
            requireAllNonNull(aliasCommand, aliasString);
            if (!(aliasCommand instanceof String && aliasString instanceof String)) {
                throw new IllegalValueException(MESSAGE_ALIAS_CONSTRAINTS);
            }
            this.aliasCommand = aliasCommand;
            this.aliasString = aliasString;
        } catch (IllegalValueException e) {
            ;
        }
    }

    public void setAlias(String alias) {
        this.aliasString = (requireNonNull(alias));
    }

    public String getAlias() {
        return aliasString;
    }

    public String getCommand() {
        return aliasCommand;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Alias// instanceof handles nulls
                && this.aliasString == ((Alias) other).aliasString);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(aliasCommand, aliasString);
    }

}
```
