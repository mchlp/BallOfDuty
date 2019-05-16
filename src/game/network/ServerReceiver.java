/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerReceiver {
    private static final int PORT = 9100;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer buffer;


    public ServerReceiver() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        buffer = ByteBuffer.allocate(2048);
    }

    private void openChannel() throws IOException, ClassNotFoundException {
        serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Opened server socket...");

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    register(selector, serverSocketChannel);
                }
                if (key.isReadable()) {
                    process(key);
                }
                iterator.remove();
            }
        }

    }

    private void process(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        System.out.println("Packet received " + new String(buffer.array()));

        ByteBuffer response = ByteBuffer.wrap("Received".getBytes());
        client.write(response);
        buffer.clear();
    }

    private void register(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        System.out.println(client);
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerReceiver server = new ServerReceiver();
        server.openChannel();
    }
}
