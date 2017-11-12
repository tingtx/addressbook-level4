package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.ui.BrowserWindow.GOOGLE_SEARCH_URL_PREFIX;
import static seedu.address.ui.BrowserWindow.GOOGLE_SEARCH_URL_SUFFIX;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import guitests.guihandles.BrowserWindowHandle;
import javafx.stage.Stage;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;

public class BrowserWindowTest extends GuiUnitTest {
    private PersonPanelSelectionChangedEvent selectionChangedEventStub;

    private BrowserWindow browserWindow;
    private BrowserWindowHandle browserWindowHandle;

    @Before
    public void setUp() throws Exception {

        selectionChangedEventStub = new PersonPanelSelectionChangedEvent(new PersonCard(ALICE, 0));

        guiRobot.interact(() -> browserWindow = new BrowserWindow());
        Stage browserWindowStage = FxToolkit.setupStage((stage) -> stage.setScene(browserWindow.getRoot().getScene()));
        FxToolkit.showStage();
        browserWindowHandle = new BrowserWindowHandle(browserWindowStage);
    }

    @Test
    public void display() throws Exception {

        postNow(selectionChangedEventStub);
        URL expectedPersonUrl = new URL(GOOGLE_SEARCH_URL_PREFIX
                + ALICE.getName().fullName.replaceAll(" ", "+") + GOOGLE_SEARCH_URL_SUFFIX);

        waitUntilBrowserLoaded(browserWindowHandle);
        assertEquals(expectedPersonUrl, browserWindowHandle.getLoadedUrl());
    }
}
