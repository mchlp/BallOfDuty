package game;

import game.data_structures.Pair;
import game.network.packets.Packet;
import game.network.ServerReceiver;
import game.network.packets.PacketType;
import game.server.ClientProfile;

import java.io.IOException;
import java.util.HashMap;

public class Server {

    private static final int PORT = 9100;

    private boolean keepRunning;
    private ServerReceiver serverReceiver;
    private HashMap<String, ClientProfile> clientList;

    public Server(int port) throws IOException {
        clientList = new HashMap<>();
        serverReceiver = new ServerReceiver(port, this::registerClient, this::deregisterClient);
        keepRunning = true;
    }

    public void startLoop() throws IOException {
        while (keepRunning) {
            serverReceiver.sendAndRecieve();
            while (serverReceiver.hasNextIncomingPacket()) {
                Pair<String, Packet> incoming = serverReceiver.popNextIncomingPacket();
                System.out.format("Packet received from %s: %s\n", incoming.first, incoming.second);
                serverReceiver.enqueueOutgoingPacket(incoming.first, new Packet(PacketType.TEXT, "Received Message"));
            }
        }
    }

    private void registerClient(String cliendId) {
        ClientProfile newClient = new ClientProfile();
        clientList.put(cliendId, newClient);
    }

    private void deregisterClient(String clientId) {
        clientList.remove(clientId);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(PORT);
        server.startLoop();
    }
}
