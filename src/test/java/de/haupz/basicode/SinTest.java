package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class SinTest extends ExpressionTest {

    @Test
    public void testZero() {
        testExpression("SIN(0)", 0.0, Double.class);
        testExpression("SIN(0.0)", 0.0, Double.class);
    }

    @Test
    public void testInteger() {
        testExpression("SIN(1)", 0.84147, Double.class);
    }

    @Test
    public void testDouble() {
        testExpression("SIN(1.0)", 0.84147, Double.class);
    }

}
