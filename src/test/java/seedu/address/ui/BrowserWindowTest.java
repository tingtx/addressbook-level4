package seedu.address.ui;

import guitests.guihandles.BrowserWindowHandle;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;

import java.net.URL;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.ui.BrowserWindow.GOOGLE_SEARCH_URL_PREFIX;
import static seedu.address.ui.BrowserWindow.GOOGLE_SEARCH_URL_SUFFIX;

public class BrowserWindowTest extends GuiUnitTest {
    private PersonPanelSelectionChangedEvent selectionChangedEventStub;

    private BrowserWindow browserWindow;
    private BrowserWindowHandle browserWindowHandle;

    @Before
    public void setUp() throws Exception {
        guiRobot.interact(() -> browserWindow = new BrowserWindow());
        Stage browserWindowStage = FxToolkit.setupStage((stage) -> stage.setScene(browserWindow.getRoot().getScene()));
        FxToolkit.showStage();
        browserWindowHandle = new BrowserWindowHandle(browserWindowStage);
    }

    @Test
    public void display() throws Exception {

        PersonCard aliceCard = new PersonCard(ALICE, 1);
        // associated web page of a person
        selectionChangedEventStub = new PersonPanelSelectionChangedEvent(aliceCard);
        browserWindow.loadPersonPage(ALICE);
        URL expectedPersonUrl = new URL(GOOGLE_SEARCH_URL_PREFIX
                + ALICE.getName().fullName.replaceAll(" ", "+") + GOOGLE_SEARCH_URL_SUFFIX);

        waitUntilBrowserLoaded(browserWindowHandle);
        assertEquals(expectedPersonUrl, browserWindowHandle.getLoadedUrl());
    }
}
