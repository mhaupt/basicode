package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class AddTest extends ExpressionTest {

    @Test
    public void test3plus4() {
        testExpression("3 + 4", 7, Integer.class);
    }

    @Test
    public void test3plus4NoSpace() {
        testExpression("3+4", 7, Integer.class);
    }

}
