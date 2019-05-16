/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerReceiver extends Receiver {
    private static final int PORT = 9100;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public ServerReceiver() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
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

    private void process(SelectionKey key) throws IOException {
        System.out.println("Process.");
        SocketChannel client = (SocketChannel) key.channel();
        Packet packet = attemptReadPacket(client);
        System.out.println(packet);
        if (packet != null) {
            System.out.println("Packet received: " + packet.getBody());
            send(client, new Packet("Received Message."));
        }
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
