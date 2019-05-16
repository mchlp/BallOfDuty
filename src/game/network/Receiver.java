package game.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Receiver {

    private static final int BODY_BUFFER_LENGTH = 2048;

    private ByteBuffer headerBuffer;
    private ByteBuffer bodyBuffer;

    public abstract ArrayList<Packet> checkForPackets() throws IOException;
    public abstract void sendPacket(Packet sendPacket) throws IOException;

    Receiver() {
        headerBuffer = ByteBuffer.allocate(Packet.HEADER_LENGTH_BYTES);
        bodyBuffer = ByteBuffer.allocate(BODY_BUFFER_LENGTH);

    }

    Packet attemptReadPacket(SocketChannel socketChannel) throws IOException {
        int bytesRead = socketChannel.read(headerBuffer);
        if (bytesRead > 0) {
            headerBuffer.flip();
            int bodyLength = headerBuffer.getInt();
            headerBuffer.clear();
            int bytesLeft = bodyLength;
            StringBuilder body = new StringBuilder();

            while (bytesLeft > 0) {
                int byteToRead = Math.min(BODY_BUFFER_LENGTH, bytesLeft);
                bodyBuffer.limit(byteToRead);
                socketChannel.read(bodyBuffer);
                body.append(new String(Arrays.copyOfRange(bodyBuffer.array(), 0, byteToRead)));
                bytesLeft -= byteToRead;
                bodyBuffer.clear();
            }

            return new Packet(body.toString());
        }
        return null;
    }

    void send(SocketChannel socketChannel, Packet packet) throws IOException {
        ByteBuffer response = packet.getByteBuffer();
        while (response.hasRemaining()) {
            socketChannel.write(response);
        }
    }
}
