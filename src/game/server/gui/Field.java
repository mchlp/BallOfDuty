package game.server.gui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Field extends HBox {
    public Field(String title, String text) {
        Text titleText = new Text(title + ": ");
        titleText.setStyle("-fx-font-weight: bold");
        Text textText = new Text(text);
        getChildren().addAll(titleText, textText);
    }
}
