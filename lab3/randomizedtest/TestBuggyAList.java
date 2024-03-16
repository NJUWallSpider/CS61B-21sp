package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    public static void main(String[] args){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 5000;
        for(int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                org.junit.Assert.assertEquals(L.size(), B.size());
            } else if (operationNumber == 2 && L.size() > 0) {
                int remove_L = L.removeLast();
                int remove_B = B.removeLast();
                org.junit.Assert.assertEquals(remove_B, remove_L);

            } else if (operationNumber == 3 && L.size() > 0) {
                int get_L = L.getLast();
                int get_B = B.getLast();
                org.junit.Assert.assertEquals(get_B, get_L);
            }
        }
    }
}
