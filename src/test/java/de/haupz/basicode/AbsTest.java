package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class AbsTest extends ExpressionTest {

    @Test
    public void testPositiveInteger() {
        testExpression("ABS(23)", 23, Integer.class);
    }

    @Test
    public void testExtraPositiveInteger() {
        testExpression("ABS(+23)", 23, Integer.class);
    }

    @Test
    public void testNegativeInteger() {
        testExpression("ABS(-42)", 42, Integer.class);
    }

    @Test
    public void testPositiveFloat() {
        testExpression("ABS(2.3)", 2.3, Double.class);
    }

    @Test
    public void testExtraPositiveFloat() {
        testExpression("ABS(+2.3)", 2.3, Double.class);
    }

    @Test
    public void testNegativeFloat() {
        testExpression("ABS(-4.2)", 4.2, Double.class);
    }

    @Test
    public void testZero() {
        testExpression("ABS(0)", 0, Integer.class);
        testExpression("ABS(0.0)", 0.0, Double.class);
        testExpression("ABS(+0)", 0, Integer.class);
        testExpression("ABS(+0.0)", 0.0, Double.class);
        testExpression("ABS(-0)", 0, Integer.class);
        testExpression("ABS(-0.0)", 0.0, Double.class);
    }

    @Test
    public void testString() {
        testExpressionThrows("ABS(\"Hello\")", IllegalStateException.class);
    }

}
