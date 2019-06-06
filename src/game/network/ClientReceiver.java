/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import game.network.packets.Packet;
import game.network.packets.PacketBody;
import game.network.packets.PacketBodyCoordinate;
import game.network.packets.PacketType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class ClientReceiver extends Receiver {

    public static final int TIME_PER_HEARTBEAT = 2 * 1000;

    private static final String ADDRESS = "localhost";
    private static final int PORT = 9100;

    private SocketChannel socketChannel;
    private String address;
    private int port;
    private long lastHeartbeatSent;
    private boolean isConnected;
    private String clientId;

    public ClientReceiver(String address, int port) throws IOException {
        super();
        this.address = address;
        this.port = port;
        lastHeartbeatSent = 0;
        isConnected = false;
        openChannel();
    }

    private void openChannel() {
        isConnected = false;
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(address, port));
            socketChannel.configureBlocking(false);

            isConnected = true;
            sendPacket(new Packet(PacketType.PLAYER_REQUEST_JOIN, PacketBody.EMPTY_BODY));

        } catch (IOException e) {
            isConnected = false;
            System.out.println("Could not connect to server.");
        }
    }

    public ArrayList<Packet> checkForPackets() {

        if (!isConnected() && socketChannel.isConnected()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (System.currentTimeMillis() - lastHeartbeatSent > TIME_PER_HEARTBEAT) {
            sendPacket(new Packet(PacketType.PLAYER_HEARTBEAT, PacketBody.EMPTY_BODY));
            lastHeartbeatSent = System.currentTimeMillis();
        }
        ArrayList<Packet> packetList = new ArrayList<>();
        while (isConnected()) {
            Packet packet;
            try {
                packet = attemptReadPacket(socketChannel);
                if (packet == null) {
                    break;
                }
                switch (packet.type) {
                    case PLAYER_RESPOND_JOIN:
                        clientId = packet.body.toString();
                        break;
                    case PLAYER_KICK:
                        isConnected = false;
                        break;
                }
                packetList.add(packet);
            } catch (IOException e) {
                isConnected = false;
                break;
            }
        }
        return packetList;
    }

    public void sendPacket(Packet sendPacket) {
        if (isConnected()) {
            try {
                send(socketChannel, sendPacket);
            } catch (IOException ignored) {
                isConnected = false;
            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean attemptReconnect() {
        openChannel();
        return isConnected;
    }

    public String getClientId() {
        return clientId;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientReceiver clientReceiver = new ClientReceiver(ADDRESS, PORT);

        while (true) {
            Thread.sleep(500);

            ArrayList<Packet> receivedPackets = clientReceiver.checkForPackets();

            clientReceiver.sendPacket(new Packet(PacketType.PLAYER_MOVE,
                    new PacketBodyCoordinate(clientReceiver.getClientId(), 10, 10, 10)));

            for (Packet packet : receivedPackets) {
                System.out.println("Packet received: " + packet);
            }

            if (!clientReceiver.isConnected()) {
                clientReceiver.attemptReconnect();
            }
        }
    }

}
