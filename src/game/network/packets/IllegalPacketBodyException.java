package game.network.packets;

public class IllegalPacketBodyException extends IllegalArgumentException {
    public IllegalPacketBodyException(String allowedTypes, PacketType packetType) {
        super("Body must be of type(s) " + allowedTypes + " when PacketType is " + packetType);
    }
}
