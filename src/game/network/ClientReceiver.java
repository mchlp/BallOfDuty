/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientReceiver {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 9100;

    private boolean isConnected = false;
    private SocketChannel socketChannel;
    private ObjectOutputStream outputStream;
    private ByteBuffer buffer;

    public ClientReceiver() throws IOException {
        socketChannel = SocketChannel.open();
        buffer = ByteBuffer.allocate(2048);
        openChannel();
    }

    private void openChannel() throws IOException {
        socketChannel.connect(new InetSocketAddress(ADDRESS, PORT));
        isConnected = true;

        sendObject();

        while (true) {
            socketChannel.read(buffer);
            System.out.println("Read message: " + new String(buffer.array()));
        }
    }

    private void sendObject() throws IOException {
        ByteBuffer response = ByteBuffer.wrap("Testingtesting".getBytes());
        socketChannel.write(response);
        buffer.clear();
    }

    public static void main(String[] args) throws IOException {
        ClientReceiver clientReceiver = new ClientReceiver();
    }

}
