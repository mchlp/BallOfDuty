/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
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

        Scanner scanner = new Scanner(System.in);

        while (true) {
            Packet packet = attemptReadPacket(socketChannel);
            if (packet != null) {
                System.out.println("Received packet: " + packet.getBody());
            }

            if (scanner.hasNext()) {
                Packet sendPacket = new Packet(scanner.nextLine());
                System.out.println(sendPacket.getBody());
                send(socketChannel, sendPacket);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ClientReceiver clientReceiver = new ClientReceiver();
    }

}
