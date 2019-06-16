package game.server;

import game.data_structures.Pair;
import game.network.ServerReceiver;
import game.network.packets.Packet;
import game.network.packets.PacketBody;
import game.network.packets.PacketBodyText;
import game.network.packets.PacketType;

import java.io.IOException;
import java.util.HashMap;

public class ServerProcessor {

    public static final int TIME_PER_HEARTBEAT = 2 * 1000;

    private ServerReceiver serverReceiver;

    private long lastHeartbeat;

    public ServerProcessor(String ip, int port) throws IOException {
        serverReceiver = new ServerReceiver(ip, port);
    }

    public void tick() throws IOException {
        serverReceiver.sendAndRecieve();
        while (serverReceiver.hasNextIncomingPacket()) {
            Pair<String, Packet> incoming = serverReceiver.popNextIncomingPacket();
            System.out.format("Packet received from %s: %s\n", incoming.first, incoming.second);

            // Process packets
            switch (incoming.second.type) {
                case PLAYER_MOVE:
                    for (String player : serverReceiver.getClientList().keySet()) {
                        if (!player.equals(incoming.first)) {
                            serverReceiver.enqueueOutgoingPacket(player, incoming.second);
                        }
                    }
                    break;
                case PLAYER_REQUEST_JOIN:
                    System.out.println(incoming.first);
                    serverReceiver.enqueueOutgoingPacket(incoming.first, new Packet(PacketType.PLAYER_RESPOND_JOIN,
                            new PacketBodyText(incoming.first)));
            }

            Packet receivedPacket = new Packet(PacketType.TEXT, new PacketBodyText("Received Message"));
            serverReceiver.enqueueOutgoingPacket(incoming.first, receivedPacket);
        }

        if (System.currentTimeMillis() - lastHeartbeat > TIME_PER_HEARTBEAT) {
            for (String clientId : serverReceiver.getClientList().keySet()) {
                serverReceiver.enqueueOutgoingPacket(clientId, new Packet(PacketType.SERVER_HEARTBEAT,
                        PacketBody.EMPTY_BODY));
            }
            lastHeartbeat = System.currentTimeMillis();
        }
    }

    public ServerReceiver getServerReceiver() {
        return serverReceiver;
    }

    public String[] getClients() {
        HashMap<String, ClientProfile> clientMap = serverReceiver.getClientList();
        String[] clients = new String[clientMap.size()];
        return clientMap.keySet().toArray(clients);
    }

    public HashMap<String, ClientProfile> getClientList() {
        return serverReceiver.getClientList();
    }
}
