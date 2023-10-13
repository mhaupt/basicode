package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class PowerTest extends ExpressionTest {

    @Test
    public void testInteger() {
        testExpression("2^3", 8, Integer.class);
        testExpression("2^-2", 0.25, Double.class);
        testExpression("-2^2", 4, Integer.class);
        testExpression("-2^3", -8, Integer.class);
    }

    @Test
    public void testDouble() {
        testExpression("2.0^3.0", 8.0, Double.class);
        testExpression("2.0^-2.0", 0.25, Double.class);
        testExpression("-2.0^2.0", 4.0, Double.class);
        testExpression("-2.0^3.0", -8.0, Double.class);
    }

    @Test
    public void testCoercion() {
        testExpression("2^3.0", 8.0, Double.class);
        testExpression("2.0^3", 8.0, Double.class);
    }

    @Test
    public void testTypeMismatch() {
        testExpressionThrows("2^\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"^2", IllegalStateException.class);
        testExpressionThrows("2.0^\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"^2.0", IllegalStateException.class);
    }

    @Test
    public void testStringError() {
        testExpressionThrows("\"A\"^\"B\"", IllegalStateException.class);
    }

}
