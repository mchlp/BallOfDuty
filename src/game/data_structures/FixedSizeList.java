package game.data_structures;

import java.util.ArrayList;

public class FixedSizeList<T> {
    private int size;
    private int limit;
    private Queue<T> queue;

    public FixedSizeList(int limit) {
        super();
        this.limit = limit;
        this.size = 0;
        this.queue = new Queue<>();
    }

    public void add(T element) {
        size++;
        if (size > limit) {
            queue.backElement = queue.backElement.before;
        }
        queue.enqueue(element);
    }

    public T getTopElement() {
        if (queue.frontElement == null) {
            return null;
        }
        return queue.frontElement.value;
    }

    public ArrayList<T> getElements() {
        ArrayList<T> elements = new ArrayList<>();
        if (queue.frontElement != null) {
            Element<T> cur = queue.frontElement;
            elements.add(cur.value);
            while (cur.next != null) {
                elements.add(cur.next.value);
                cur = cur.next;
            }
        }
        return elements;
    }
}
