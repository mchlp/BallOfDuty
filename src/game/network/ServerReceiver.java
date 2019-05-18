/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import game.data_structures.Pair;
import game.data_structures.Queue;
import game.network.packets.Packet;
import game.network.packets.PacketType;

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
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private HashMap<String, Queue<Packet>> outgoingPacketQueueMap;
    private Queue<Pair<String, Packet>> incomingPacketQueue;
    private boolean runLoop;

    public ServerReceiver(int port) throws IOException {
        runLoop = true;
        outgoingPacketQueueMap = new HashMap<>();
        incomingPacketQueue = new Queue<>();
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        openChannel(port);
    }

    private void openChannel(int port) throws IOException {
        serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.format("Opened server socket on port %d...\n", port);
    }

    private void enqueueIncomingPackets(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        while (true) {
            Packet packet = attemptReadPacket(client);
            if (packet == null) {
                return;
            }
            incomingPacketQueue.enqueue(new Pair<>((String) key.attachment(), packet));
        }
    }

    private void register(SelectionKey key, Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        String socketId = UUID.randomUUID().toString();
        Queue<Packet> outgoingQueue = new Queue<>();
        outgoingPacketQueueMap.put(socketId, outgoingQueue);


        SocketChannel client = serverSocketChannel.accept();
        System.out.println(client);
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, socketId);
    }

    private void sendQueuedPackets(SelectionKey key) throws IOException {
        Queue<Packet> clientQueue = outgoingPacketQueueMap.get(key.attachment());
        while (!clientQueue.isEmpty()) {
            Packet packet = clientQueue.dequeue();
            send((SocketChannel) key.channel(), packet);
        }
    }

    public void sendAndRecieve() throws IOException {
        selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            if (key.isAcceptable()) {
                register(key, selector, serverSocketChannel);
            }
            if (key.isReadable()) {
                enqueueIncomingPackets(key);
            }
            if (key.isWritable()) {
                if (!outgoingPacketQueueMap.get(key.attachment()).isEmpty()) {
                    sendQueuedPackets(key);
                }
            }
            iterator.remove();
        }
    }

    public void enqueueOutgoingPacket(String clientId, Packet packet) throws IOException {
        outgoingPacketQueueMap.get(clientId).enqueue(packet);
    }

    public Pair<String, Packet> popNextIncomingPacket() {
        return incomingPacketQueue.dequeue();
    }

    public boolean hasNextIncomingPacket() {
        return !incomingPacketQueue.isEmpty();
    }
}
