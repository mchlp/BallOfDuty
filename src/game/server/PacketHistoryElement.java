package game.server;

import game.network.packets.Packet;

import java.util.Date;

public class PacketHistoryElement {

    public final long created;
    public final Packet packet;

    public PacketHistoryElement(Packet packet) {
        created = System.currentTimeMillis();
        this.packet = packet;
    }

    @Override
    public String toString() {
        Date createdDate = new Date(created);
        return createdDate.toString() + " - " + packet.toString();
    }
}
