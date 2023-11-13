package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class SubtractTest extends ExpressionTest {

    @Test
    public void test3minus4() {
        testExpression("3 - 4", -1.0, Double.class);
    }

    @Test
    public void test3minus4NoSpace() {
        testExpression("3-4", -1.0, Double.class);
    }

    @Test
    public void test3minus4Double() {
        testExpression("3.1-4.2", -1.1, Double.class);
    }

    @Test
    public void testCoercion() {
        testExpression("3-4.1", -1.1, Double.class);
        testExpression("3.1-4", -0.9, Double.class);
    }

    @Test
    public void testNegativeNumbers() {
        testExpression("-1-2", -3.0, Double.class);
        testExpression("1--2", 3.0, Double.class);
        testExpression("-1.1-2.1", -3.2, Double.class);
        testExpression("1.1--2.1", 3.2, Double.class);
    }

    @Test
    public void testNegativeCoercion() {
        testExpression("-1-2.1", -3.1, Double.class);
        testExpression("-1.1-2", -3.1, Double.class);
        testExpression("1--2.1", 3.1, Double.class);
        testExpression("1.1--2", 3.1, Double.class);
    }

    @Test
    public void testStringError() {
        testExpressionThrows("\"A\"-\"B\"", IllegalStateException.class);
    }

    @Test
    public void testTypeMismatches() {
        testExpressionThrows("3-\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"-3", IllegalStateException.class);
        testExpressionThrows("3.2-\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"-3.2", IllegalStateException.class);
    }

}
