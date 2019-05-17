/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import game.data_structures.Queue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class ServerReceiver extends Receiver {
    private static final int PORT = 9100;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private HashMap<String, Queue<Packet>> allClientPacketQueue;

    public ServerReceiver() throws IOException {
        allClientPacketQueue = new HashMap<>();
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
    }

    private void openChannel() throws IOException {
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
                    register(key, selector, serverSocketChannel);
                }
                if (key.isReadable()) {
                    readPacket(key);
                }
                if (key.isWritable()) {
                    if (!allClientPacketQueue.get(key.attachment()).isEmpty()) {
                        sendQueuedPackets(key);
                    }
                }
                iterator.remove();
            }
        }
    }

    private void readPacket(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        while (true) {
            Packet packet = attemptReadPacket(client);
            if (packet == null) {
                return;
            }
            System.out.println("Packet received: " + packet);
            enqueuePacket((String) key.attachment(), new Packet("Received Message"));
        }
    }

    private void register(SelectionKey key, Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        String socketId = UUID.randomUUID().toString();
        Queue<Packet> clientQueue = new Queue<>();
        allClientPacketQueue.put(socketId, clientQueue);

        SocketChannel client = serverSocketChannel.accept();
        System.out.println(client);
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, socketId);
    }

    public void enqueuePacket(String clientId, Packet packet) throws IOException {
        allClientPacketQueue.get(clientId).enqueue(packet);
    }

    public void sendQueuedPackets(SelectionKey key) throws IOException {
        Queue<Packet> clientQueue = allClientPacketQueue.get(key.attachment());
        while (!clientQueue.isEmpty()) {
            Packet packet = clientQueue.dequeue();
            send((SocketChannel) key.channel(), packet);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerReceiver server = new ServerReceiver();
        server.openChannel();
    }
}
