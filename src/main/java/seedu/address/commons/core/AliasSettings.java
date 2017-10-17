package seedu.address.commons.core;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.commons.core.Messages.MESSAGE_DUPLICATE_ALIAS;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;
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
    }

    public AliasSettings(String addCommand, String clearCommand, String deleteCommand, String editCommand,
                         String exitCommand, String findCommand, String helpCommand,
                         String historyCommand, String listCommand, String orderCommand, String redoCommand,
                         String remarkCommand, String selectCommand, String setAliasCommand, String undoCommand,
                         String viewAliasCommand) {
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

        usedAliases.add("add");
        usedAliases.add("clear");
        usedAliases.add("delete");
        usedAliases.add("edit");
        usedAliases.add("exit");
        usedAliases.add("find");
        usedAliases.add("help");
        usedAliases.add("history");
        usedAliases.add("list");
        usedAliases.add("order");
        usedAliases.add("redo");
        usedAliases.add("remark");
        usedAliases.add("select");
        usedAliases.add("setalias");
        usedAliases.add("undo");
        usedAliases.add("viewalias");
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

    public void setAlias(String command, String alias) throws DuplicateAliasException, UnknownCommandException {
        if (usedAliases.contains(alias)) {
            throw new DuplicateAliasException(MESSAGE_DUPLICATE_ALIAS);
        } else if (command.equals(AddCommand.getCommandWord())) {
            usedAliases.remove(this.addCommand.getAlias());
            usedAliases.add(alias);
            this.addCommand = new Alias(AddCommand.getCommandWord(), alias);
        } else if (command.equals(ClearCommand.getCommandWord())) {
            usedAliases.remove(this.clearCommand.getAlias());
            usedAliases.add(alias);
            this.clearCommand = new Alias(ClearCommand.getCommandWord(), alias);
        } else if (command.equals(DeleteCommand.getCommandWord())) {
            usedAliases.remove(this.deleteCommand.getAlias());
            usedAliases.add(alias);
            this.deleteCommand = new Alias(DeleteCommand.getCommandWord(), alias);
        } else if (command.equals(EditCommand.getCommandWord())) {
            usedAliases.remove(this.editCommand.getAlias());
            this.editCommand = new Alias(EditCommand.getCommandWord(), alias);
        } else if (command.equals(ExitCommand.getCommandWord())) {
            usedAliases.remove(this.exitCommand.getAlias());
            usedAliases.add(alias);
            this.exitCommand = new Alias(ExitCommand.getCommandWord(), alias);
        } else if (command.equals(FindCommand.getCommandWord())) {
            usedAliases.remove(this.findCommand.getAlias());
            usedAliases.add(alias);
            this.findCommand = new Alias(FindCommand.getCommandWord(), alias);
        } else if (command.equals(HelpCommand.getCommandWord())) {
            usedAliases.remove(this.helpCommand.getAlias());
            usedAliases.add(alias);
            this.helpCommand = new Alias(HelpCommand.getCommandWord(), alias);
        } else if (command.equals(HistoryCommand.getCommandWord())) {
            usedAliases.remove(this.historyCommand.getAlias());
            usedAliases.add(alias);
            this.historyCommand = new Alias(HistoryCommand.getCommandWord(), alias);
        } else if (command.equals(ListCommand.getCommandWord())) {
            usedAliases.remove(this.listCommand.getAlias());
            usedAliases.add(alias);
            this.listCommand = new Alias(ListCommand.getCommandWord(), alias);
        } else if (command.equals(OrderCommand.getCommandWord())) {
            usedAliases.remove(this.orderCommand.getAlias());
            usedAliases.add(alias);
            this.orderCommand = new Alias(OrderCommand.getCommandWord(), alias);
        } else if (command.equals(RedoCommand.getCommandWord())) {
            usedAliases.remove(this.redoCommand.getAlias());
            usedAliases.add(alias);
            this.redoCommand = new Alias(RedoCommand.getCommandWord(), alias);
        } else if (command.equals(RemarkCommand.getCommandWord())) {
            usedAliases.remove(this.remarkCommand.getAlias());
            usedAliases.add(alias);
            this.remarkCommand = new Alias(RemarkCommand.getCommandWord(), alias);
        } else if (command.equals(SelectCommand.getCommandWord())) {
            usedAliases.remove(this.selectCommand.getAlias());
            usedAliases.add(alias);
            this.selectCommand = new Alias(SelectCommand.getCommandWord(), alias);
        } else if (command.equals(SetAliasCommand.getCommandWord())) {
            usedAliases.remove(this.setAliasCommand.getAlias());
            usedAliases.add(alias);
            this.setAliasCommand = new Alias(SetAliasCommand.getCommandWord(), alias);
        } else if (command.equals(UndoCommand.getCommandWord())) {
            usedAliases.remove(this.undoCommand.getAlias());
            usedAliases.add(alias);
            this.undoCommand = new Alias(UndoCommand.getCommandWord(), alias);
        } else if (command.equals(ViewAliasCommand.getCommandWord())) {
            usedAliases.remove(this.viewAliasCommand.getAlias());
            usedAliases.add(alias);
            this.viewAliasCommand = new Alias(ViewAliasCommand.getCommandWord(), alias);
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
