package game.server;

import game.network.ServerReceiver;

import java.io.IOException;

public class ServerLoop {

    private int port;

    public ServerLoop(int port) {
        this.port = port;
    }

    public void run() throws IOException {
        ServerReceiver serverReceiver = new ServerReceiver(this.port);
    }
}
