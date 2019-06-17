package game.server;

import game.data_structures.Pair;
import game.network.ServerReceiver;
import game.network.packets.*;

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
                case PLAYER_SHOOT:
                    PacketBodyText packetBodyText = (PacketBodyText) incoming.second.body;
                    serverReceiver.enqueueOutgoingPacket(packetBodyText.text, incoming.second);
                    break;
                case PLAYER_MOVE:
                    PacketBodyCoordinate packetBodyCoordinate = (PacketBodyCoordinate) incoming.second.body;
                    serverReceiver.getClientList().get(incoming.first).setPacketBodyCoordinate(packetBodyCoordinate);

                    for (String player : serverReceiver.getClientList().keySet()) {
                        if (!player.equals(incoming.first)) {
                            serverReceiver.enqueueOutgoingPacket(player, incoming.second);
                        }
                    }
                    break;
                case PLAYER_REQUEST_JOIN:
                    serverReceiver.enqueueOutgoingPacket(incoming.first, new Packet(PacketType.PLAYER_RESPOND_JOIN,
                            new PacketBodyText(incoming.first)));
                    for (ClientProfile player : serverReceiver.getClientList().values()) {
                        serverReceiver.enqueueOutgoingPacket(incoming.first, new Packet(PacketType.PLAYER_MOVE,
                                player.getPacketBodyCoordinate()));
                        serverReceiver.enqueueOutgoingPacket(player.id, new Packet(PacketType.PLAYER_MOVE,
                                serverReceiver.getClientList().get(incoming.first).getPacketBodyCoordinate()));
                    }
            }
        }

        if (System.currentTimeMillis() - lastHeartbeat > TIME_PER_HEARTBEAT) {
            for (ClientProfile clientId : serverReceiver.getClientList().values()) {
                serverReceiver.enqueueOutgoingPacket(clientId.id, new Packet(PacketType.SERVER_HEARTBEAT,
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
