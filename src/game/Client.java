package game;

import game.client.ClientLoop;

public class Client {

    public static void main(String[] args) {
        ClientLoop loop = new ClientLoop();
        loop.run();
    }

}