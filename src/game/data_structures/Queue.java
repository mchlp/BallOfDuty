package game.data_structures;

public class Queue<T> {

    Element<T> backElement;

    public void enqueue(T value) {
        Element<T> newElement = new Element();
        newElement.value = value;

        if (backElement != null) {
            newElement.next = backElement;
        }

        backElement = newElement;
    }

    public T dequeue() {
        Element<T> popElement = backElement;
        backElement = backElement.next;
        return popElement.value;
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
            while (cur.next != null) {
                cur = cur.next;
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
