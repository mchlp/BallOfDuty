package game;

import game.data_structures.Pair;
import game.network.Packet;
import game.network.ServerReceiver;

import java.io.IOException;

public class Server {

    private static final int PORT = 9100;

    private boolean keepRunning;
    private ServerReceiver serverReceiver;

    public Server(int port) throws IOException {
        serverReceiver = new ServerReceiver(port);
        keepRunning = true;
    }

    public void startLoop() throws IOException {
        while (keepRunning) {
            serverReceiver.sendAndRecieve();
            while (serverReceiver.hasNextIncomingPacket()) {
                Pair<String, Packet> incoming = serverReceiver.popNextIncomingPacket();
                System.out.format("Packet received from %s: %s\n", incoming.first, incoming.second);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(PORT);
        server.startLoop();
    }
}
