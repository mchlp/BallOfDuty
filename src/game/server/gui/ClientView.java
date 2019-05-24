package game.server.gui;

import game.server.ClientProfile;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Date;

public class ClientView extends VBox {

    private static final int PADDING = 10;

    private ClientProfile clientProfile;

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
            getChildren().addAll(idField, joinedField, ipField);
        }
    }
}
