package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class CosTest extends ExpressionTest {

    @Test
    public void testZero() {
        testExpression("COS(0)", 1.0, Double.class);
        testExpression("COS(0.0)", 1.0, Double.class);
    }

    @Test
    public void testInteger() {
        testExpression("COS(1)", 0.540302, Double.class);
    }

    @Test
    public void testDouble() {
        testExpression("COS(1.0)", 0.540302, Double.class);
    }

}
