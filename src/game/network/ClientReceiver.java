/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientReceiver extends Receiver {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 9100;

    private boolean isConnected = false;
    private SocketChannel socketChannel;

    public ClientReceiver() throws IOException {
        super();
        socketChannel = SocketChannel.open();
        openChannel();
    }

    private void openChannel() throws IOException {
        socketChannel.connect(new InetSocketAddress(ADDRESS, PORT));
        socketChannel.configureBlocking(false);
        isConnected = true;
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

    public void sendPacket(Packet sendPacket) throws IOException {
        if (isConnected) {
            send(socketChannel, sendPacket);
        } else {

        }
    }

    public static void main(String[] args) throws IOException {
        ClientReceiver clientReceiver = new ClientReceiver();
    }

}
