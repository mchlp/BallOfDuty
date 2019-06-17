/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.server;

import game.network.packets.Packet;

import java.util.Date;

/**
 * Wraps around a packet for it to be stored in a list of sent or received packets. Stores the packet and the time
 * this element was created.
 */
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
