package game.server;

import game.data_structures.FixedSizeList;
import game.data_structures.Queue;
import game.network.packets.Packet;

import java.util.Date;

public class ClientProfile {

    private static final int PACKET_HISTORY_LENGTH = 1000;

    public final String id;
    public final long joined;
    public final String ip;
    public final Queue<Packet> outgoingQueue;
    public final FixedSizeList<PacketHistoryElement> receivedPacketHistory;
    public final FixedSizeList<PacketHistoryElement> sentPacketHistory;


    public ClientProfile(String id, String ip) {
        this.id = id;
        this.joined = System.currentTimeMillis();
        this.ip = ip;
        outgoingQueue = new Queue<>();
        receivedPacketHistory = new FixedSizeList<>(PACKET_HISTORY_LENGTH);
        sentPacketHistory = new FixedSizeList<>(PACKET_HISTORY_LENGTH);
    }

    public void addReceivedPacketToHistory(Packet packet) {
        receivedPacketHistory.add(new PacketHistoryElement(packet));
    }

    public void addSentPacketToHistory(Packet packet) {
        sentPacketHistory.add(new PacketHistoryElement(packet));
    }

    public Queue<Packet> getOutgoingQueue() {
        return outgoingQueue;
    }

    @Override
    public String toString() {
        String string = "";
        string += "\n";
        string += ("RECEIVED PACKET HISTORY\n");

        for (PacketHistoryElement packetHistoryElement : receivedPacketHistory.getElements()) {
            string += packetHistoryElement.toString() + "\n";
        }

        string += "\n";
        string += ("SENT PACKET HISTORY\n");

        for (PacketHistoryElement packetHistoryElement : sentPacketHistory.getElements()) {
            string += packetHistoryElement.toString() + "\n";
        }

        return string;
    }
}
