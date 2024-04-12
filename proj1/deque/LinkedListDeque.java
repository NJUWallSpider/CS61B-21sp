package deque;
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
}
