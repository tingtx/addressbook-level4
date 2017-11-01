//@@author keloysiusmak
package seedu.address.ui;

import org.junit.Before;
import org.testfx.api.FxToolkit;

import guitests.guihandles.ViewAliasWindowHandle;
import javafx.stage.Stage;
import seedu.address.logic.Logic;

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
