package game.server;

import game.data_structures.FixedSizeList;
import game.data_structures.Queue;
import game.network.packets.Packet;
import game.network.packets.PacketBodyCoordinate;
import game.vec.Vec3;

public class ClientProfile {

    private static final int PACKET_HISTORY_LENGTH = 1000;

    public final String id;
    private PacketBodyCoordinate packetBodyCoordinate;
    public final long joined;
    public final String ip;
    public final Queue<Packet> outgoingQueue;
    public final FixedSizeList<PacketHistoryElement> receivedPacketHistory;
    public final FixedSizeList<PacketHistoryElement> sentPacketHistory;


    public ClientProfile(String id, String ip) {
        this.id = id;
        this.packetBodyCoordinate = new PacketBodyCoordinate(id, 0, 100, 0, 0, 0);
        this.joined = System.currentTimeMillis();
        this.ip = ip;
        outgoingQueue = new Queue<>();
        receivedPacketHistory = new FixedSizeList<>(PACKET_HISTORY_LENGTH);
        sentPacketHistory = new FixedSizeList<>(PACKET_HISTORY_LENGTH);
    }

    public void addReceivedPacketToHistory(Packet packet) {
        receivedPacketHistory.add(new PacketHistoryElement(packet));
    }

    public void setPacketBodyCoordinate(PacketBodyCoordinate packetBodyCoordinate) {
        this.packetBodyCoordinate = packetBodyCoordinate;
    }

    public PacketBodyCoordinate getPacketBodyCoordinate() {
        return packetBodyCoordinate;
    }

    public void addSentPacketToHistory(Packet packet) {
        sentPacketHistory.add(new PacketHistoryElement(packet));
    }

    public Queue<Packet> getOutgoingQueue() {
        return outgoingQueue;
    }
}
