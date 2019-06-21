/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game;

import game.server.ServerProcessor;
import game.server.gui.ClientView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

/**
 * This is the main class of the Server. It handles the GUI of the server and encompasses all the classes that
 * handles the networking on the server side.
 */
public class Server extends Application {

    private static final String ADDRESS = "0.0.0.0";
    private static final int PORT = 8861;
    private static int serverPort;

    private static final String NO_CLIENTS_CONNECTED_STRING = "No connected clients.";
    private static final String NO_CLIENT_SELECTED_STRING = "No selected client.";

    private long prevTime;
    private ServerProcessor serverProcessor;
    private ListView<String> clientListView;
    private ClientView clientView;
    private String selectedClient;

    public static void main(String[] args) {
        serverPort = PORT;
        if (args.length > 0) {
            serverPort = Integer.parseInt(args[0]);
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            serverProcessor = new ServerProcessor(ADDRESS, serverPort);
            selectedClient = NO_CLIENT_SELECTED_STRING;

            BorderPane root = new BorderPane();
            clientListView = new ListView<>();
            clientListView.setPrefWidth(320);
            root.setLeft(clientListView);

            clientView = new ClientView(serverProcessor);
            root.setCenter(clientView);

            Scene scene = new Scene(root);

            primaryStage.setTitle("Server GUI");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();

            prevTime = System.nanoTime();
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    double deltaTime = (now - prevTime) / 1E9;
                    onUpdate(deltaTime);
                    prevTime = now;
                }
            };

            animationTimer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onUpdate(double deltaTime) {
        try {
            serverProcessor.tick();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] clients = serverProcessor.getClients();
        Arrays.sort(clients);
        ObservableList<String> clientList;

        if (clients.length > 0) {
            clientList = FXCollections.observableArrayList(clients);
        } else {
            clientList = FXCollections.observableArrayList(NO_CLIENTS_CONNECTED_STRING);
        }
        clientListView.setItems(clientList);

        String selected = clientListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            selected = NO_CLIENT_SELECTED_STRING;
        }
        if (!selected.equals(selectedClient)) {
            if (selected.equals(NO_CLIENTS_CONNECTED_STRING)) {
                clientView.setClient(null);
                selectedClient = NO_CLIENT_SELECTED_STRING;
            } else {
                clientView.setClient(serverProcessor.getClientList().get(selected));
                selectedClient = selected;
            }
        }
        clientView.tick();
    }
}
