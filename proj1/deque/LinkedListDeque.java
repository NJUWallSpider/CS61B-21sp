package deque;
/** Implementation of deque based on linked list */
public class LinkedListDeque<T> {
    DLList<T> list = new DLList<>();
    public LinkedListDeque(){
        list = new DLList<>();
    }
    /** add one item to the front */
    public void addFirst(T item){
        list.addFirst(item);
    }
    public void addLast(T item){
        list.addLast(item);
    }
    public boolean isEmpty(){
        return list.size() == 0;
    }
    public int size(){
        return list.size();
    }
    public void printDeque(){
        list.print();
    }
    public T removeFirst(){
        return list.removeFirst();
    }
    public T removeLast(){
        return list.removeLast();
    }
    public T get(int index){
       return list.get(index);
    }
}
