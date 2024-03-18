package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    public int front, back;
    public ArrayDeque(){
        items = (T[]) new Object[8];
        size = 0;
        front = 0;
        back = 0;
    }
    private void resize(int capacity){
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, front);
        System.arraycopy(items, items.length - back, a, a.length - back, back);
        items = a;
    }
    public void addFirst(T x){
        if(front >= 0){
            if (size >= items.length) {
                resize(size * 2);
            }
            items[front] = x;
            front += 1;
        } else{
            items[items.length + front] = x;
            front += 1;
        }
        size += 1;
    }
    public int size(){
        return size;
    }
    public void addLast(T x){
        if(back >= 0){
            if (size >= items.length) {
                resize(size * 2);
            }
            items[items.length - 1 - back] = x;
            back += 1;

        } else{
            items[back + 1] = x;
            back += 1;
        }
        size += 1;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public void printDeque(){
        if(isEmpty()){
            System.out.println("Empty deque");
        }else{
            if(front > 0){
                if(back > 0){
                    for(int i = front - 1; i >= 0; i-=1){
                        System.out.print(items[i]);
                        System.out.print(" ");
                    }
                    for(int i = 0; i < back; i+=1){
                        System.out.print(items[items.length - 1 - i]);
                        System.out.print(" ");
                    }
                } else{
                    for(int i = front - 1; i >= -back; i-=1){
                        System.out.print(items[i]);
                        System.out.print(" ");
                    }
                }
            } else{
                for(int i = -front; i < back ; i+=1){
                    System.out.print(items[items.length - 1 - i]);
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }
    public T removeFirst() {
        if(size == 0){
            return null;
        }
        T returned;
        if(front > 0){
            double usage = (double) size / items.length;
            front -= 1;
            returned = items[front];
            items[front] = null;
            if (usage < 0.25) {
                resize(size / 2);
            }
        } else{
            front -= 1;
            returned = items[items.length + front];
            items[items.length + front] = null;
        }
        size -= 1;
        return returned;
    }
    public T removeLast() {
        if(size == 0){
            return null;
        }
        T returned;
        if(back > 0){
            double usage = (double) size / items.length;
            returned = items[items.length - back];
            items[items.length - back] = null;
            back -= 1;
            if (usage < 0.25) {
                resize(size / 2);
            }
        } else{
            returned = items[-back];
            items[-back] = null;
            back -= 1;
        }
        size -= 1;
        return returned;
    }
    public T get(int index){
        if(index > size - 1 || index < 0){
            System.out.println("arrayBoundError");
            return null;
        }
        if(index < front){
            return items[front - 1 - index];
        } else if(front >= 0){
            return items[items.length - index + front];
        } else{
            return items[items.length - 1 + front - index];
        }
    }
}
