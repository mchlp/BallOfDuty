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
        String address = ADDRESS;
        int port = PORT;
        if (args.length > 0) {
            address = args[0];
            if (args.length == 2) {
                port = Integer.parseInt(args[1]);
            }
        }
        ClientLoop loop = new ClientLoop(address, port);
        loop.run();
    }

}