package deque;

/** Implementation of Doubly Linked List */
class DLList<T> {
    /** The naked DLList */
    /* the first item (if it exists) is at sentinel.next */
    private int size;
    private Node sentinel;
    /** creates an empty SLList */
    private class Node {
        private T item;
        private Node next;
        private Node prev;
        Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }
    DLList() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }
    /** adds x to the front of list */
    public void addFirst(T x) {
        sentinel.next = new Node(x, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }
    /** returns the first item of the list */
    public T getFirst() {
        return sentinel.next.item;
    }
    /** return the last item of the list */
    public T getLast() {
        return sentinel.prev.item;
    }
    /** adds an item to the end of the list */
    public void addLast(T x) {
        sentinel.prev = new Node(x, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }
    /** returns the number of items in the list */
    public int size(){
        return size;
    }
    public void print() {
        Node temp = sentinel;
        for (int i = 0; i < size - 1; i += 1) {
            System.out.print(temp.next.item);
            System.out.print(" ");
            temp = temp.next;
        }
        System.out.println(temp.next.item);
    }
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returned = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return returned;
    }
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returned = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return returned;
    }
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node temp = sentinel;
        if ((double) index / size <= 0.5) {
            for (int i = 0; i <= index; i += 1) {
                temp = temp.next;
            }
            return temp.item;
        } else {
            int searchTimes = size - index;
            for(int i = 0; i < searchTimes; i += 1) {
                temp = temp.prev;
            }
            return temp.item;
        }
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        Node temp = sentinel;
        if ((double) index / size <= 0.5) {
            return helperGetRecur1(index, temp);
        } else {
            int search_times = size - index;
            return helperGetRecur2(search_times, temp);
        }
    }

    private T helperGetRecur1(int index, Node ptr) {
        if (index < 0) {
            return ptr.item;
        } else {
            return helperGetRecur1(index - 1, ptr.next);
        }
    }
    private T helperGetRecur2(int index, Node ptr) {
        if (index == 0) {
            return ptr.item;
        } else {
            return helperGetRecur2(index - 1, ptr.prev);
        }
    }
}

