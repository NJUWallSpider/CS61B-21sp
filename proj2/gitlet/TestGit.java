package gitlet;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class TestInit {
    @Test
    public void test1() throws IOException {
        Repository.initiate();
    }
}
