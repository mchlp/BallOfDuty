/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.network.packets;

public class IllegalPacketBodyException extends IllegalArgumentException {
    public IllegalPacketBodyException(String allowedTypes, PacketType packetType) {
        super("Body must be of type(s) " + allowedTypes + " when PacketType is " + packetType);
    }
}
