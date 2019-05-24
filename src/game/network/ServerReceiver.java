/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import game.data_structures.Pair;
import game.data_structures.Queue;
import game.network.packets.Packet;
import game.server.ClientProfile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ServerReceiver extends Receiver {


    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private Queue<Pair<String, Packet>> incomingPacketQueue;
    private HashMap<String, ClientProfile> clientList;
    private HashSet<String> kickQueue;

    public ServerReceiver(int port) throws IOException {
        clientList = new HashMap<>();
        selector = Selector.open();
        incomingPacketQueue = new Queue<>();
        kickQueue = new HashSet<>();
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
            String clientId = (String) key.attachment();
            clientList.get(clientId).addReceivedPacketToHistory(packet);
            incomingPacketQueue.enqueue(new Pair<>(clientId, packet));
        }
    }

    private void register(SelectionKey key, Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        String socketId = UUID.randomUUID().toString();
        Queue<Packet> outgoingQueue = new Queue<>();

        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, socketId);

        ClientProfile newClient = new ClientProfile(socketId, client.getRemoteAddress().toString());
        clientList.put(socketId, newClient);

        System.out.println("Register client " + socketId);

    }

    private void deregister(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        client.close();
        String clientId = (String) key.attachment();

        clientList.remove(clientId);
        System.out.println("Deregister client " + clientId);
    }

    private void sendQueuedPackets(SelectionKey key) throws IOException {
        String clientId = (String) key.attachment();
        Queue<Packet> clientQueue = clientList.get(clientId).getOutgoingQueue();
        while (!clientQueue.isEmpty()) {
            Packet packet = clientQueue.dequeue();
            send((SocketChannel) key.channel(), packet);
            clientList.get(clientId).addSentPacketToHistory(packet);
        }
    }

    public void sendAndRecieve() throws IOException {
        selector.selectNow();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            try {
                if (key.isAcceptable()) {
                    register(key, selector, serverSocketChannel);
                }
                if (key.isReadable()) {
                    enqueueIncomingPackets(key);
                }
                if (key.isWritable()) {
                    if (kickQueue.contains(key.attachment())) {
                        deregister(key);
                    }
                    if (!clientList.get(key.attachment()).getOutgoingQueue().isEmpty()) {
                        sendQueuedPackets(key);
                    }
                }
            } catch (IOException e) {
                deregister(key);
            }
            iterator.remove();
        }
    }

    public void enqueueOutgoingPacket(String clientId, Packet packet) throws IOException {
        clientList.get(clientId).getOutgoingQueue().enqueue(packet);
    }

    public void kick(String clientId) {
        kickQueue.add(clientId);
    }

    public Pair<String, Packet> popNextIncomingPacket() {
        return incomingPacketQueue.dequeue();
    }

    public boolean hasNextIncomingPacket() {
        return !incomingPacketQueue.isEmpty();
    }

    public HashMap<String, ClientProfile> getClientList() {
        return clientList;
    }
}
