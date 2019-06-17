/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.server.gui;

import game.data_structures.FixedSizeList;
import game.server.PacketHistoryElement;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Stores the history of either packets that were received or sent in the GUI. Extends VBox element of JavaFX.
 */
public class ClientHistoryContainer extends VBox {

    private FixedSizeList<PacketHistoryElement> history;
    private TextArea historyDisplay;

    public ClientHistoryContainer(String name, FixedSizeList<PacketHistoryElement> history) {
        super();
        this.history = history;

        Text label = new Text(name);
        label.setStyle("-fx-font-weight: bold");
        getChildren().add(label);

        historyDisplay = new TextArea();
        getChildren().add(historyDisplay);
    }

    public void tick() {
        String historyStr = "";
        for (PacketHistoryElement packetHistoryElement : history.getElements()) {
            historyStr += packetHistoryElement.toString() + "\n";
        }
        historyDisplay.setText(historyStr);

    }

}
