package seedu.address.commons.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.Objects;

import seedu.address.logic.commands.Command;
import seedu.address.model.alias.Alias;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetAliasCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.ViewAliasCommand;

/**
 * A Serializable class that contains the GUI settings.
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


    public AliasSettings() {
        this.addCommand = new Alias(AddCommand.getCommandWord(), null);
        this.clearCommand = new Alias(ClearCommand.getCommandWord(), null);
        this.deleteCommand = new Alias(DeleteCommand.getCommandWord(), null);
        this.editCommand = new Alias(EditCommand.getCommandWord(), null);
        this.exitCommand = new Alias(ExitCommand.getCommandWord(), null);
        this.findCommand = new Alias(FindCommand.getCommandWord(), null);
        this.helpCommand = new Alias(HelpCommand.getCommandWord(), null);
        this.historyCommand = new Alias(HistoryCommand.getCommandWord(), null);
        this.listCommand = new Alias(ListCommand.getCommandWord(), null);
        this.orderCommand = new Alias(OrderCommand.getCommandWord(), null);
        this.redoCommand = new Alias(RedoCommand.getCommandWord(), null);
        this.remarkCommand = new Alias(RemarkCommand.getCommandWord(), null);
        this.selectCommand = new Alias(SelectCommand.getCommandWord(), null);
        this.setAliasCommand = new Alias(SetAliasCommand.getCommandWord(), null);
        this.undoCommand = new Alias(UndoCommand.getCommandWord(), null);
        this.viewAliasCommand = new Alias(ViewAliasCommand.getCommandWord(), null);
    }

    public AliasSettings(String add, String clear, String delete, String edit, String exit, String find, String help,
                         String history, String list, String order, String redo, String remark, String select,
                         String setAlias, String undo, String viewAlias) {
        this.addCommand = new Alias(AddCommand.getCommandWord(), add);
        this.clearCommand = new Alias(ClearCommand.getCommandWord(), clear);
        this.deleteCommand = new Alias(DeleteCommand.getCommandWord(), delete);
        this.editCommand = new Alias(EditCommand.getCommandWord(), edit);
        this.exitCommand = new Alias(ExitCommand.getCommandWord(), exit);
        this.findCommand = new Alias(FindCommand.getCommandWord(), find);
        this.helpCommand = new Alias(HelpCommand.getCommandWord(), help);
        this.historyCommand = new Alias(HistoryCommand.getCommandWord(), history);
        this.listCommand = new Alias(ListCommand.getCommandWord(), list);
        this.orderCommand = new Alias(OrderCommand.getCommandWord(), order);
        this.redoCommand = new Alias(RedoCommand.getCommandWord(), redo);
        this.remarkCommand = new Alias(RemarkCommand.getCommandWord(), remark);
        this.selectCommand = new Alias(SelectCommand.getCommandWord(), select);
        this.setAliasCommand = new Alias(SetAliasCommand.getCommandWord(), setAlias);
        this.undoCommand = new Alias(UndoCommand.getCommandWord(), undo);
        this.viewAliasCommand = new Alias(ViewAliasCommand.getCommandWord(), viewAlias);
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

    public void setAlias(String command, String alias) {
        switch(alias) {
            case("add"):
                this.addCommand = new Alias(AddCommand.getCommandWord(), alias);
            case "clear":
                this.clearCommand = new Alias(ClearCommand.getCommandWord(), alias);
            case "delete":
                this.deleteCommand = new Alias(DeleteCommand.getCommandWord(), alias);
            case "edit":
                this.editCommand = new Alias(EditCommand.getCommandWord(), alias);
            case "exit":
                this.exitCommand = new Alias(ExitCommand.getCommandWord(), alias);
            case "find":
                this.findCommand = new Alias(FindCommand.getCommandWord(), alias);
            case "help":
                this.helpCommand = new Alias(HelpCommand.getCommandWord(), alias);
            case "history":
                this.historyCommand = new Alias(HistoryCommand.getCommandWord(), alias);
            case "list":
                this.listCommand = new Alias(ListCommand.getCommandWord(), alias);
            case "order":
                this.orderCommand = new Alias(OrderCommand.getCommandWord(), alias);
            case "redo":
                this.redoCommand = new Alias(RedoCommand.getCommandWord(), alias);
            case "remark":
                this.remarkCommand = new Alias(RemarkCommand.getCommandWord(), alias);
            case "select":
                this.selectCommand = new Alias(SelectCommand.getCommandWord(), alias);
            case "setalias":
                this.setAliasCommand = new Alias(SetAliasCommand.getCommandWord(), alias);
            case "undo":
                this.undoCommand = new Alias(UndoCommand.getCommandWord(), alias);
            case "viewalias":
                this.viewAliasCommand = new Alias(ViewAliasCommand.getCommandWord(), alias);
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
                && Objects.equals(viewAliasCommand, o.getViewAliasCommand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.addCommand, this.clearCommand, this.deleteCommand, this.editCommand,
                this.exitCommand, this.findCommand, this.helpCommand, this.historyCommand, this.listCommand,
                this.orderCommand, this.redoCommand, this.remarkCommand, this.selectCommand, this.setAliasCommand,
                this.undoCommand, this.viewAliasCommand);
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
        return sb.toString();
    }
}
