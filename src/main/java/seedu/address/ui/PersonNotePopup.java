package seedu.address.ui;

import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class PersonNotePopup extends UiPart<Region>{
    private static final String FXML = "PersonListPanel.fxml";
    private static Stage window = new Stage();
    public PersonNotePopup() {
        super(FXML);
    }

    public static void popup() {
        window.show();
    }

    public static void closePopup() {
        window.close();
    }
}
