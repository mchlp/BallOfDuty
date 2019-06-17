/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.data_structures;

/**
 * An element in be stored in a queue.
 */
public class Element<T> {
    public T value;
    public Element<T> before;
    public Element<T> next;
}