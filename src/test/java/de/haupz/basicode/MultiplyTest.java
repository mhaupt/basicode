package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class MultiplyTest extends ExpressionTest {

    @Test
    public void test3times4() {
        testExpression("3 * 4", 12.0, Double.class);
    }

    @Test
    public void test3times4NoSpace() {
        testExpression("3*4", 12.0, Double.class);
    }

    @Test
    public void test3times4Double() {
        testExpression("3.1*4.2", 13.02, Double.class);
    }

    @Test
    public void testCoercion() {
        testExpression("3*4.1", 12.3, Double.class);
        testExpression("3.1*4", 12.4, Double.class);
    }

    @Test
    public void testNegativeNumbers() {
        testExpression("-1*2", -2.0, Double.class);
        testExpression("1*-2", -2.0, Double.class);
        testExpression("-1.1*2.1", -2.31, Double.class);
        testExpression("1.1*-2.1", -2.31, Double.class);
    }

    @Test
    public void testNegativeCoercion() {
        testExpression("-1*2.1", -2.1, Double.class);
        testExpression("-1.1*2", -2.2, Double.class);
        testExpression("1*-2.1", -2.1, Double.class);
        testExpression("1.1*-2", -2.2, Double.class);
    }

    @Test
    public void testStringError() {
        testExpressionThrows("\"A\"*\"B\"", IllegalStateException.class);
    }

    @Test
    public void testTypeMismatches() {
        testExpressionThrows("3*\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"*3", IllegalStateException.class);
        testExpressionThrows("3.2*\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"*3.2", IllegalStateException.class);
    }

}
