/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.data_structures;

/**
 * Queue ADT.
 */
public class Queue<T> {

    Element<T> frontElement;
    Element<T> backElement;

    public void enqueue(T num) {
        Element<T> newElement = new Element<>();
        newElement.value = num;

        if (frontElement != null) {
            newElement.next = frontElement;
            frontElement.before = newElement;
        }

        if (backElement == null) {
            backElement = newElement;
        }
        frontElement = newElement;
    }

    public T dequeue() {
        Element popElement = backElement;
        backElement = backElement.before;
        return (T) popElement.value;
    }

    public T peek() {
        return backElement.value;
    }

    public int size() {
        if (backElement == null) {
            return 0;
        } else {
            int sizeCount = 1;
            Element cur = backElement;
            while (cur.before != null) {
                cur = cur.before;
                sizeCount++;
            }
            return sizeCount;
        }
    }

    public boolean isEmpty() {
        return backElement == null;
    }

    public boolean isFull() {
        return false;
    }
}
