package deque;

import java.util.Iterator;

/** Implementation of deque based on linked list */
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private DLList<T> list = new DLList<>();
    public LinkedListDeque() {
        list = new DLList<>();
    }
    /** add one item to the front */
    @Override
    public void addFirst(T item) {
        list.addFirst(item);
    }
    @Override
    public void addLast(T item) {
        list.addLast(item);
    }

    @Override
    public int size() {
        return list.size();
    }
    @Override
    public void printDeque() {
        list.print();
    }
    @Override
    public T removeFirst() {
        return list.removeFirst();
    }
    @Override
    public T removeLast() {
        return list.removeLast();
    }
    @Override
    public T get(int index) {
       return list.get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int currPos;
        public LinkedListDequeIterator() {
            currPos = 0;
        }
        @Override
        public boolean hasNext() {
            return currPos < size();
        }

        @Override
        public T next() {
            T returnItem = get(currPos);
            currPos += 1;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Deque) {
            Deque<T> od = (Deque<T>) o;
            if (od.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size(); i += 1) {
                if(!this.get(i).equals(od.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public T getRecursive(int index) {
        return list.getRecursive(index);
    }

}
