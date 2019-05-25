package game.server.gui;

import game.data_structures.FixedSizeList;
import game.server.PacketHistoryElement;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ClientHistoryContainer extends VBox {

    private String name;
    private FixedSizeList<PacketHistoryElement> history;
    private TextArea historyDisplay;

    public ClientHistoryContainer(String name, FixedSizeList<PacketHistoryElement> history) {
        super();
        this.name = name;
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
