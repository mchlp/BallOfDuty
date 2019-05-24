/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import game.network.packets.Packet;
import game.network.packets.PacketBody;
import game.network.packets.PacketType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class ClientReceiver extends Receiver {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 9100;

    private SocketChannel socketChannel;
    private String address;
    private int port;

    public ClientReceiver(String address, int port) throws IOException {
        super();
        this.address = address;
        this.port = port;
        socketChannel = SocketChannel.open();
        openChannel();
    }

    private void openChannel() {
        try {
            socketChannel.connect(new InetSocketAddress(address, port));
            socketChannel.configureBlocking(false);
            System.out.format("Connected to server at %s:%d\n", address, port);
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
        }
    }

    public ArrayList<Packet> checkForPackets() {
        ArrayList<Packet> packetList = new ArrayList<>();
        while (isConnected()) {
            Packet packet = null;
            try {
                packet = attemptReadPacket(socketChannel);
                if (packet == null) {
                    break;
                }
                packetList.add(packet);
            } catch (IOException e) {
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
            }
        }
    }

    public boolean isConnected() {
        return socketChannel.isConnected();
    }

    public boolean attemptReconnect() {
        openChannel();
        return isConnected();
    }

    public static void main(String[] args) throws IOException {
        ClientReceiver clientReceiver = new ClientReceiver(ADDRESS, PORT);
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clientReceiver.sendPacket(new Packet(PacketType.PLAYER_HEARTBEAT, PacketBody.EMPTY_BODY));
            ArrayList<Packet> receivedPackets = clientReceiver.checkForPackets();

            for (Packet packet : receivedPackets) {
                System.out.println("Packet received: " + packet);
            }

            System.out.println("Connection status: " + clientReceiver.isConnected());
        }
    }

}
