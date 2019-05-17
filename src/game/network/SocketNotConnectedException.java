package game.network;

public class SocketNotConnectedException extends Exception {
    public SocketNotConnectedException() {
        super("Socket not connected.");
    }
}
