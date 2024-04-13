package flik;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestFilk {
    @Test
    public void testIsSameNumber(){
        int i = 0;
        for (int j = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                break;
            }
        }
        System.out.println("i is " + i);
        assertEquals(500, i);
    }


}
