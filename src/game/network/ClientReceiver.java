/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class ClientReceiver extends Receiver {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 9100;

    private boolean isConnected = false;
    private SocketChannel socketChannel;

    public ClientReceiver(String address, int port) throws IOException {
        super();
        socketChannel = SocketChannel.open();
        openChannel(address, port);
    }

    private void openChannel(String address, int port) throws IOException {
        socketChannel.connect(new InetSocketAddress(address, port));
        socketChannel.configureBlocking(false);
        isConnected = true;
        System.out.format("Connected to server at %s:%d\n", address, port);
    }

    public ArrayList<Packet> checkForPackets() throws IOException {
        ArrayList<Packet> packetList = new ArrayList<>();
        while (true) {
            Packet packet = attemptReadPacket(socketChannel);
            if (packet == null) {
                return packetList;
            }
            packetList.add(packet);
        }
    }

    public void sendPacket(Packet sendPacket) throws IOException, SocketNotConnectedException {
        if (isConnected) {
            send(socketChannel, sendPacket);
        } else {
            throw new SocketNotConnectedException();
        }
    }

    public static void main(String[] args) throws IOException, SocketNotConnectedException {
        ClientReceiver clientReceiver = new ClientReceiver(ADDRESS, PORT);
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clientReceiver.sendPacket(new Packet(PacketType.PLAYER_SHOOT, "Tester"));
            ArrayList<Packet> receivedPackets = clientReceiver.checkForPackets();

            for (Packet packet : receivedPackets) {
                System.out.println("Packet received: " + packet);
            }
        }
    }

}
