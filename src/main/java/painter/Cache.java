package painter;

/**
 * Created by airat on 26.11.15.
 */
public class Cache<T> {
    private final int MAX_CACHE_SIZE;
    //--------------   fields   --------------
    private Node current;

    //-----------   Constructors   ------------
    public Cache(T t, int maxSize) {
        MAX_CACHE_SIZE = maxSize;
        put(t);
    }

    //--------------   methods   -------------
    public void put(T t) {
        Node node = new Node();
        node.t = t;
        node.prev = current;
        if (current != null)
            current.next = node;
        current = node;
        checkLimit();
    }

    private void checkLimit() {
        int amountNode = 0;
        for (Node currentNode = current; currentNode != null; currentNode = currentNode.prev) {
            ++amountNode;
            if (amountNode == MAX_CACHE_SIZE) {
                currentNode.prev = null;
            }
        }
    }

    public T get() {
        return current.t;
    }

    public T getPrev() {
        current = current.prev;
        return current.t;
    }

    public T getNext() {
        if (current.next != null) {
            current = current.next;
        }
        return current.t;
    }

    private class Node {
        //--------------   fields   -------------
        Node next;
        Node prev;
        T t;
    }

}
