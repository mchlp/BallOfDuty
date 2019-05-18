package game;

import game.server.ServerLoop;

import java.io.IOException;

public class Server {

    private static final int PORT = 9001;

    public static void main(String[] args) throws IOException {
        ServerLoop serverLoop = new ServerLoop(PORT);
        serverLoop.run();
    }
}
