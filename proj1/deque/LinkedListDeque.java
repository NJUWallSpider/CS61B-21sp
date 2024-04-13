package deque;

import java.util.Iterator;

/** Implementation of deque based on linked list */
public class LinkedListDeque<T> implements Deque<T>{
    DLList<T> list = new DLList<>();
    public LinkedListDeque(){
        list = new DLList<>();
    }
    /** add one item to the front */
    @Override
    public void addFirst(T item){
        list.addFirst(item);
    }
    @Override
    public void addLast(T item){
        list.addLast(item);
    }
    @Override
    public int size(){
        return list.size();
    }
    @Override
    public void printDeque(){
        list.print();
    }
    @Override
    public T removeFirst(){
        return list.removeFirst();
    }
    @Override
    public T removeLast(){
        return list.removeLast();
    }
    @Override
    public T get(int index){
       return list.get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T>{
        private int currPos;
        public LinkedListDequeIterator(){
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
}
