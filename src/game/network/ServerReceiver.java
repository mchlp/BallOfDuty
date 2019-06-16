/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import game.data_structures.Pair;
import game.data_structures.Queue;
import game.network.packets.Packet;
import game.network.packets.PacketBodyText;
import game.network.packets.PacketType;
import game.server.ClientIDGenerator;
import game.server.ClientProfile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ServerReceiver extends Receiver {

    private ClientIDGenerator clientIDGenerator;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private Queue<Pair<String, Packet>> incomingPacketQueue;
    private HashMap<String, ClientProfile> clientList;
    private HashSet<String> kickQueue;

    public ServerReceiver(String ip, int port) throws IOException {
        clientIDGenerator = new ClientIDGenerator();
        clientList = new HashMap<>();
        selector = Selector.open();
        incomingPacketQueue = new Queue<>();
        kickQueue = new HashSet<>();
        serverSocketChannel = ServerSocketChannel.open();
        openChannel(ip, port);
    }

    private void openChannel(String ip, int port) throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(ip, port));
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
        String socketId = Integer.toString(clientIDGenerator.nextID());
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
        key.cancel();
        client.close();
        String clientId = (String) key.attachment();

        clientList.remove(clientId);

        for (String player : clientList.keySet()) {
            if (!player.equals(clientId)) {
                enqueueOutgoingPacket(player, new Packet(PacketType.PLAYER_LEAVE, new PacketBodyText(clientId)));
            }
        }

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
                if (clientList.containsKey(key.attachment()) && key.isValid()) {
                    if (key.isReadable()) {
                        enqueueIncomingPackets(key);
                    }
                    if (key.isWritable()) {
                        if (kickQueue.contains(key.attachment())) {
                            deregister(key);
                        } else if (!clientList.get(key.attachment()).getOutgoingQueue().isEmpty()) {
                            sendQueuedPackets(key);
                        }
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
