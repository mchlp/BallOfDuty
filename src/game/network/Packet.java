/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.nio.ByteBuffer;

public class Packet {

    public static final int HEADER_LENGTH_BYTES = Integer.BYTES;
    public static final int TYPE_LENGTH_BYTES = Integer.BYTES;

    private String body;
    private PacketType type;

    public Packet(PacketType type, String body) {
        this.type = type;
        this.body = body;
    }

    public PacketType getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public ByteBuffer getByteBuffer() {
        byte[] msgBytes = body.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(msgBytes.length + HEADER_LENGTH_BYTES + TYPE_LENGTH_BYTES);
        byteBuffer.putInt(msgBytes.length);
        byteBuffer.putInt(type.code);
        byteBuffer.put(msgBytes);
        byteBuffer.flip();
        return byteBuffer;
    }

    @Override
    public String toString() {
        return getType() + " - " + getBody();
    }
}
