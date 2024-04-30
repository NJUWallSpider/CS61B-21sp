package deque;

import org.junit.Test;
import org.junit.Assert;
import java.util.Comparator;

public class TestMaxArrayDeque {
    public static class MaxIntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 > o2) {
                return 1;
            } else if (o1.equals(o2)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    @Test
    public void testInteger() {
        Comparator<Integer> nc = new TestMaxArrayDeque.MaxIntegerComparator();
        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<>(nc);
        lld1.addFirst(1);
        lld1.addLast(2);
        lld1.addFirst(0);
        lld1.addLast(3);
        int expected = 3;
        int actual = lld1.max();
        Assert.assertEquals(expected, actual);
    }
}
