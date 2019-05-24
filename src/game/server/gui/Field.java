package game.server.gui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Field extends HBox {

    private Text titleText;
    private Text textText;

    public Field(String title, String text) {
        titleText = new Text(title + ": ");
        titleText.setStyle("-fx-font-weight: bold");
        textText = new Text(text);
        getChildren().addAll(titleText, textText);
    }

    public void setText(String text) {
        textText.setText(text);
    }

}
