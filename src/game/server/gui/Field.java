/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.server.gui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * A GUI element that represents a field. Contains the field type and the field value.
 */
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
