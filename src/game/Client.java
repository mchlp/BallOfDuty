/*
 *  Author: Henry Gu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/18
 *  Course: ICS4U
 */

package game;

import game.client.ClientLoop;

public class Client {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 8861;

    public static void main(String[] args) {
        ClientLoop loop = new ClientLoop(ADDRESS, PORT);
        loop.run();
    }

}