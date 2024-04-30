package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> nc;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        nc = c;
    }

    /** returns the maximum element in the deque
     * as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, simply return null.
     */
    public T max() {
        if (this.size() == 0) {
            return null;
        }
        T currMax = this.get(0);
        for (int i = 1; i < this.size(); i += 1) {
            T temp = this.get(i);
            if (nc.compare(currMax, temp) < 0) {
                currMax = temp;
            }
        }
        return currMax;
    }

    public T max(Comparator<T> c) {
        Comparator<T> temp = nc;
        nc = c;
        T currMax = this.max();
        nc = temp;
        return currMax;
    }
}
