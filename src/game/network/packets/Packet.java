/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network.packets;

import java.nio.ByteBuffer;

public class Packet {

    public static final int HEADER_LENGTH_BYTES = Integer.BYTES;
    public static final int TYPE_LENGTH_BYTES = Integer.BYTES;

    public final PacketBody body;
    public final PacketType type;

    public Packet(PacketType type, String stringBody) {
        this(type, new PacketBody(stringBody));
    }

    public Packet(PacketType type, PacketBody body) throws IllegalPacketBodyException {
        this.type = type;

        switch (type) {
            case PLAYER_MOVE:
                if (!(body instanceof PacketBodyCoordinate)) {
                    throw new IllegalPacketBodyException("PacketBodyCoordinate", PacketType.PLAYER_MOVE);
                }
        }
        this.body = body;
    }

    public ByteBuffer getByteBuffer() {
        byte[] msgBytes = body.serializedBody.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(msgBytes.length + HEADER_LENGTH_BYTES + TYPE_LENGTH_BYTES);
        byteBuffer.putInt(msgBytes.length);
        byteBuffer.putInt(type.code);
        byteBuffer.put(msgBytes);
        byteBuffer.flip();
        return byteBuffer;
    }

    @Override
    public String toString() {
        return type + " - " + body;
    }
}