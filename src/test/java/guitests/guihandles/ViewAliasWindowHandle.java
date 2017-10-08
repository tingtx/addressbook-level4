package guitests.guihandles;

import java.net.URL;

import guitests.GuiRobot;
import javafx.stage.Stage;

/**
 * A handle to the {@code ViewAliasWindow} of the application.
 */
public class ViewAliasWindowHandle extends StageHandle {

    public static final String VIEWALIAS_WINDOW_TITLE = "View Alias";

    private static final String VIEWALIAS_WINDOW_BROWSER_ID = "#browser";

    public ViewAliasWindowHandle(Stage viewAliasWindowStage) {
        super(viewAliasWindowStage);
    }

    /**
     * Returns true if a viewalias window is currently present in the application.
     */
    public static boolean isWindowPresent() {
        return new GuiRobot().isWindowShown(VIEWALIAS_WINDOW_TITLE);
    }

    /**
     * Returns the {@code URL} of the currently loaded page.
     */
    public URL getLoadedUrl() {
        return WebViewUtil.getLoadedUrl(getChildNode(VIEWALIAS_WINDOW_BROWSER_ID));
    }
}
