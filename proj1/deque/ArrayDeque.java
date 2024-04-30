package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int front, back, original;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = 0;
        back = 0;
        original = 4;
    }

    /**
     * convert the relative distance from the origin
     * to the absolute index, mode 1 for front, 2 for back
     */
    private int indexConverter(int mode, int distance) {
        if (mode == 1) {
            int maxRoom = items.length - original - 1; // remaining room for front
            if (distance <= maxRoom) {
                return original + distance;
            } else {
                int exceeded = distance - maxRoom;
                return exceeded - 1;
            }
        } else if (mode == 2) {
            int maxRoom = original;
            if (distance <= maxRoom) {
                return original - distance;
            } else {
                int exceeded = distance - maxRoom;
                return items.length - exceeded;
            }
        }
        return 0;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int newOriginal = capacity / 2;
        int maxRoomFront = items.length - original - 1;
        int maxRoomBack = original;
        if (front <= maxRoomFront && back <= maxRoomBack) {
            System.arraycopy(items, original + 1, a, newOriginal + 1, front);
            System.arraycopy(items, original - back, a, newOriginal - back, back);
        } else if (front > maxRoomFront) {
            System.arraycopy(items, original + 1,
                    a, newOriginal + 1, maxRoomFront);
            System.arraycopy(items, 0, a, newOriginal + 1 + maxRoomFront,
                    front - maxRoomFront);
            System.arraycopy(items, original - back, a, newOriginal - back, back);
        } else {
            System.arraycopy(items, original + 1, a, newOriginal + 1, front);
            System.arraycopy(items, 0, a, newOriginal - maxRoomBack, maxRoomBack);
            System.arraycopy(items, items.length - back + maxRoomBack, a,
                    newOriginal - back, back - maxRoomBack);
        }
        original = newOriginal;
        items = a;
    }
    @Override
    public void addFirst(T x) {
        if (size >= items.length - 1) {
            resize(items.length * 2);
        }
        front += 1;
        items[indexConverter(1, front)] = x;
        size += 1;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void addLast(T x) {
        if (size >= items.length - 1) {
            resize(items.length * 2);
        }
        back += 1;
        items[indexConverter(2, back)] = x;
        size += 1;
    }
    @Override
    public void printDeque() {
        if (isEmpty()) {
            System.out.println("Empty deque");
        } else {
            if (front > 0) {
                for (int i = front; i > 0; i -= 1) {
                    System.out.print(items[indexConverter(1, i)]);
                    System.out.print(" ");
                }
            }
            if (back > 0) {
                for (int i = 1; i <= back; i += 1) {
                    System.out.print(items[indexConverter(2, i)]);
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returned;
        double usage = (double) size / items.length;
        if (usage < 0.25 && items.length > 8) {
            resize(items.length / 2);
        }
        if (front > 0) {
            returned = items[indexConverter(1, front)];
            items[indexConverter(1, front)] = null;
            front -= 1;
        } else {
            returned = items[indexConverter(2, 1)];
            items[indexConverter(2, 1)] = null;
            if (original != 0) {
                original -= 1;
            } else {
                original = items.length - 1;
            }
            back -= 1;
        }
        size -= 1;
        return returned;
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returned;
        double usage = (double) size / items.length;
        if (usage < 0.25 && items.length > 8) {
            resize(items.length / 2);
        }
        if (back > 0) {
            returned = items[indexConverter(2, back)];
            items[indexConverter(2, back)] = null;
            back -= 1;
        } else {
            returned = items[indexConverter(1, 1)];
            items[indexConverter(1, 1)] = null;
            if (original != items.length - 1) {
                original += 1;
            } else {
                original = 0;
            }
            front -= 1;
        }
        size -= 1;
        return returned;
    }
    @Override
    public T get(int index) {
        if (index + 1 <= front) {
            return items[indexConverter(1, front - index)];
        } else {
            return items[indexConverter(2, index + 1 - front)];
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int currPos;
        ArrayDequeIterator() {
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
            Deque<T> oad = (Deque<T>) o;
            if (oad.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size; i += 1) {
                if (!this.get(i).equals(oad.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}


