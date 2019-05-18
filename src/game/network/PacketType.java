package game.network;

public enum PacketType {
    PLAYER_MOVE(1),
    PLAYER_SHOOT(2),
    PLAYER_JOIN(3),
    PLAYER_LEAVE(4),
    PLAYER_HEARTBEAT(5),
    SERVER_HEARTBEAT(6),
    TEXT(7);

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
