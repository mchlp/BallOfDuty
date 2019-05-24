package game.server;

import game.network.packets.Packet;

import java.util.Date;

public class PacketHistoryElement {

    private Date created;
    private Packet packet;

    public PacketHistoryElement(Packet packet) {
        created = new Date(System.currentTimeMillis());
        this.packet = packet;
    }

    @Override
    public String toString() {
        return created.toString() + " - " + packet.toString();
    }
}
