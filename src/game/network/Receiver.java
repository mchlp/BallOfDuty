package game.network;

import game.network.packets.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public abstract class Receiver {

    private static final int BODY_BUFFER_LENGTH = 2048;

    private ByteBuffer headerBuffer;
    private ByteBuffer typeBuffer;
    private ByteBuffer bodyBuffer;

    Receiver() {
        headerBuffer = ByteBuffer.allocate(Packet.HEADER_LENGTH_BYTES);
        typeBuffer = ByteBuffer.allocate(Packet.TYPE_LENGTH_BYTES);
        bodyBuffer = ByteBuffer.allocate(BODY_BUFFER_LENGTH);

    }

    Packet attemptReadPacket(SocketChannel socketChannel) throws IOException {
        int bytesRead = socketChannel.read(headerBuffer);
        if (bytesRead > 0) {
            headerBuffer.flip();
            int bodyLength = headerBuffer.getInt();
            headerBuffer.clear();

            socketChannel.read(typeBuffer);
            typeBuffer.flip();
            PacketType type = PacketType.getPacketType(typeBuffer.getInt());
            typeBuffer.clear();

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

            PacketBody packetBody = null;
            switch (type) {
                case PLAYER_MOVE:
                    packetBody = PacketBodyCoordinate.fromSerialized(body.toString());
                    break;
                case PLAYER_RESPOND_JOIN:
                    packetBody = PacketBodyText.fromSerialized(body.toString());
                case TEXT:
                    packetBody = PacketBodyText.fromSerialized(body.toString());
                    break;
                case PLAYER_HEARTBEAT:
                case SERVER_HEARTBEAT:
                    packetBody = PacketBody.EMPTY_BODY;
                    break;
            }

            return new Packet(type, packetBody);
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
