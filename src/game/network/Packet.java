/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.nio.ByteBuffer;

public class Packet {

    public static final int HEADER_LENGTH_BYTES = Integer.BYTES;

    private String body;

    public Packet(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public ByteBuffer getByteBuffer() {
        byte[] msgBytes = body.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(msgBytes.length + HEADER_LENGTH_BYTES);
        byteBuffer.putInt(msgBytes.length);
        byteBuffer.put(msgBytes);
        byteBuffer.flip();
        return byteBuffer;
    }

    @Override
    public String toString() {
        return getBody();
    }
}
