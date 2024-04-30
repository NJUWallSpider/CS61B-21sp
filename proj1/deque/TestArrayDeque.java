package deque;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Assert.*;

import java.util.Optional;

import static org.junit.Assert.*;

public class TestArrayDeque {
    /** test add and remove when front and back are both positive, and no resize */
    @Test
    public void testRemove1() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(1);
        lld1.addLast(2);
        lld1.addFirst(0);
        lld1.addLast(3);
        lld1.printDeque();

        lld1.removeLast(); lld1.printDeque();
        lld1.removeFirst(); lld1.printDeque();
        lld1.removeLast(); lld1.printDeque();
        lld1.removeFirst(); lld1.printDeque();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }
    /** test add and remove when front and back are both positive with resize */
    @Test
    public void testRemove2() {
        ArrayDeque<Integer> lld2 = new ArrayDeque<>();
        for (int i = 4; i >= 0; i -= 1){
            lld2.addFirst(i);
        }
        for (int i = 5; i <= 9; i += 1) {
            lld2.addLast(i);
        }
//        Assert.assertEquals(lld2.front, 5);
//        Assert.assertEquals(lld2.back, 5);
        lld2.printDeque();
        for (int i = 0; i < 5; i += 1) {
            lld2.removeFirst();
            lld2.removeLast();
            lld2.printDeque();
        }
    }
    /** test add and remove when front and back are possibly negative without resize */
    @Test
    public void testRemove3() {
        ArrayDeque<Integer> lld3 = new ArrayDeque<>();
        for (int i = 2; i >= 0; i -=1 ) {
            lld3.addFirst(i);
        }
        for (int i = 3; i <= 9; i += 1) {
            lld3.addLast(i);
        }
        lld3.printDeque();
        for (int i = 0; i < 10; i += 1) {
            lld3.removeLast();
            lld3.printDeque();
        }
        assertTrue(lld3.isEmpty());
    }
    /** test get when front and back are possibly negative with resize */
    @Test
    public void testRemove4() {
        ArrayDeque<Integer> lld4 = new ArrayDeque<>();
        for (int i = 2; i >= 0; i -=1 ) {
            lld4.addFirst(i);
        }
        for (int i = 3; i <= 9; i += 1) {
            lld4.addLast(i);
        }
        lld4.printDeque();
        for (int i = 0; i < 10; i += 1) {
            lld4.removeFirst();
        }
        for (int i = 0; i < 10; i += 1) {
            lld4.addLast(i);
        }
        lld4.printDeque();
        int actual3 = lld4.get(0);
        assertEquals(0, actual3);
    }

    @Test
    public void testIterable(){
        ArrayDeque<Integer> lld5 = new ArrayDeque<>();
        for (int i = 3; i <= 9; i += 1) {
            lld5.addLast(i);
        }
        int sum = 0;
        for (int i : lld5) {
            sum += i;
        }
        assertEquals(sum, 42);
    }
    @Test
    public void testEqual(){
        ArrayDeque<Integer> lld7 = new ArrayDeque<>();
        ArrayDeque<Integer> lld9 = new ArrayDeque<>();
        LinkedListDeque<Integer> lld10 = new LinkedListDeque<>();
        for (int i = 0; i < 3; i += 1) {
            lld7.addFirst(i);
            lld9.addFirst(i);

        }
        lld10.addLast(2);
        lld10.addLast(1);
        lld10.addLast(0);
        assertEquals(lld7, lld9);
        assertEquals(lld7, lld10);
    }


}
