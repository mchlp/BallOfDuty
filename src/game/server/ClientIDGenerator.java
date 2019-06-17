/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.server;

/**
 * Generates IDs for clients in increasing order.
 */
public class ClientIDGenerator {
    public int next;

    public ClientIDGenerator() {
        next = 0;
    }

    public int nextID() {
        return next++;
    }
}
