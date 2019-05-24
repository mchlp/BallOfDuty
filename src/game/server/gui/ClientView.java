package game.server.gui;

import game.server.ClientProfile;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Date;

public class ClientView extends VBox {

    private static final int PADDING = 10;

    private ClientProfile clientProfile;
    private ClientHistoryContainer sentPacketHistory;
    private ClientHistoryContainer receivedPacketHistory;
    private Field lastPacketReceivedAgoField;
    private Field lastPacketSentAgoField;

    public ClientView() {
        setPadding(new Insets(PADDING));
        getChildren().add(new Text("No client selected."));
    }

    public void setClient(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
        getChildren().clear();
        if (clientProfile == null) {
            getChildren().add(new Text("No client selected"));
        } else {
            Field idField = new Field("Client ID", clientProfile.id);
            Date joinedDate = new Date(clientProfile.joined);
            Field joinedField = new Field("Joined", joinedDate.toString());
            Field ipField = new Field("IP", clientProfile.ip);
            lastPacketReceivedAgoField = new Field("Last Packet Received", "No packets received.");
            lastPacketSentAgoField = new Field("Last Packet Sent", "No packets sent.");
            getChildren().addAll(idField, joinedField, ipField, lastPacketReceivedAgoField, lastPacketSentAgoField);

            VBox packetHistoryContainer = new VBox();
            packetHistoryContainer.setPadding(new Insets(10, 0, 0, 0));
            packetHistoryContainer.setSpacing(5);
            sentPacketHistory = new ClientHistoryContainer("Sent Packet History", clientProfile.sentPacketHistory);
            receivedPacketHistory = new ClientHistoryContainer("Received Packet History", clientProfile.receivedPacketHistory);
            packetHistoryContainer.getChildren().addAll(sentPacketHistory, receivedPacketHistory);
            getChildren().add(packetHistoryContainer);
        }
    }

    public void tick() {
        if (clientProfile != null) {
            sentPacketHistory.tick();
            receivedPacketHistory.tick();
            double lastSentAgo = (System.currentTimeMillis() - clientProfile.sentPacketHistory.getTopElement().created)/1000.0;
            double lastReceivedAgo = (System.currentTimeMillis() - clientProfile.receivedPacketHistory.getTopElement().created)/1000.0;
            lastPacketSentAgoField.setText(String.format("%.2f seconds ago.", lastSentAgo));
            lastPacketReceivedAgoField.setText(String.format("%.2f seconds ago.", lastReceivedAgo));
        }
    }
}
