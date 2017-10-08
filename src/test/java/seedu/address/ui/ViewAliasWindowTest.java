package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.ui.HelpWindow.USERGUIDE_FILE_PATH;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import guitests.guihandles.HelpWindowHandle;
import guitests.guihandles.ViewAliasWindowHandle;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

public class ViewAliasWindowTest extends GuiUnitTest {

    private ViewAliasWindow viewAliasWindow;
    private ViewAliasWindowHandle viewAliasWindowHandle;

    @Before
    public void setUp(Logic logic) throws Exception {

        guiRobot.interact(() -> viewAliasWindow = new ViewAliasWindow(logic.getCommands(), logic));
        Stage helpWindowStage = FxToolkit.setupStage((stage) -> stage.setScene(viewAliasWindow.getRoot().getScene()));
        FxToolkit.showStage();
        viewAliasWindowHandle = new ViewAliasWindowHandle(helpWindowStage);
    }
}
