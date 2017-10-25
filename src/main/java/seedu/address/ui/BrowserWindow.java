package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * The Browser Panel of the App.
 */
public class BrowserWindow extends UiPart<Region> {

    public static final String GOOGLE_SEARCH_URL_PREFIX = "https://www.google.com.sg/search?safe=off&q=";
    public static final String GOOGLE_SEARCH_URL_SUFFIX = "&cad=h";
    private static final String ICON = "/images/help_icon.png";
    private static final String TITLE = "Google Search";

    private static final String FXML = "BrowserWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(BrowserWindow.class);

    @FXML
    private WebView browser;

    private final Stage dialogStage;

    public BrowserWindow() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);
        Scene scene = new Scene(getRoot());
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMinHeight(400);
        dialogStage.setMinWidth(600);
        FxViewUtil.setStageIcon(dialogStage, ICON);
    }

    /**
     * Loads a person page with a ReadOnlyPerson
     */
    public void loadPersonPage(ReadOnlyPerson person) {
        loadPage(GOOGLE_SEARCH_URL_PREFIX + person.getName().fullName.replaceAll(" ", "+")
                + GOOGLE_SEARCH_URL_SUFFIX);
    }

    /**
     * Loads a page.
     */
    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }


    /**
     * Shows the browser panel.
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        dialogStage.showAndWait();
    }

    public void close() {
        dialogStage.close();
    }
}
