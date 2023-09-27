package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class TanTest extends ExpressionTest {

    @Test
    public void testZero() {
        testExpression("TAN(0)", 0.0, Double.class);
        testExpression("TAN(0.0)", 0.0, Double.class);
    }

    @Test
    public void testInteger() {
        testExpression("TAN(1)", 1.557407, Double.class);
    }

    @Test
    public void testDouble() {
        testExpression("TAN(1.0)", 1.557407, Double.class);
    }

}
