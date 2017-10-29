package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.ChangedThemeEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Sets a theme for the TunedIn Application
 */
public class SetThemeCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "settheme";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a theme for the .\n"
            + "Parameters: THEME ('summer', 'spring', 'autumn' or 'winter')\n"
            + "Example: " + COMMAND_WORD + " spring";

    public static final String MESSAGE_CHANGED_THEME_SUCCESS = "Changed Theme: %1$s";

    private final String theme;

    public SetThemeCommand(String setTheme) {
        this.theme = setTheme;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (!((this.theme.equals("summer"))
                || (this.theme.equals("spring"))
                || (this.theme.equals("winter"))
                || (this.theme.equals("autumn")))){
            throw new CommandException(String.format(Messages.MESSAGE_WRONG_THEME, this.theme));
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
