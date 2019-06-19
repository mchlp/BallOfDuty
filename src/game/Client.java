/*
 *  Author: Henry Gu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/18
 *  Course: ICS4U
 */

package game;

import game.client.ClientLoop;

public class Client {

    public static void main(String[] args) {
        ClientLoop loop = new ClientLoop();
        loop.run();
    }

}