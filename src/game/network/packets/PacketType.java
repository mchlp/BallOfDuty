/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.network.packets;

/**
 * Possible PacketTypes that can be used to create a Packet
 */
public enum PacketType {
    PLAYER_MOVE(1),
    PLAYER_SHOOT(2),
    PLAYER_REQUEST_JOIN(3),
    PLAYER_RESPOND_JOIN(4),
    PLAYER_LEAVE(5),
    PLAYER_HEARTBEAT(6),
    SERVER_HEARTBEAT(7),
    PLAYER_KICK(8),
    TEXT(9);

    public final int code;

    PacketType(int code) {
        this.code = code;
    }

    public static PacketType getPacketType(int code) {
        for (PacketType packetType : PacketType.values()) {
            if (packetType.code == code) {
                return packetType;
            }
        }
        throw new IllegalArgumentException("Enum not found.");
    }
}
