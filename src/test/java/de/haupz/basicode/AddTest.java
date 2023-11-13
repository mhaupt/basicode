package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class AddTest extends ExpressionTest {

    @Test
    public void test3plus4() {
        testExpression("3 + 4", 7.0, Double.class);
    }

    @Test
    public void test3plus4NoSpace() {
        testExpression("3+4", 7.0, Double.class);
    }

    @Test
    public void test3plus4Double() {
        testExpression("3.1+4.2", 7.3, Double.class);
    }

    @Test
    public void testCoercion() {
        testExpression("3+4.1", 7.1, Double.class);
        testExpression("3.1+4", 7.1, Double.class);
    }

    @Test
    public void testNegativeNumbers() {
        testExpression("-1+2", 1.0, Double.class);
        testExpression("1+-2", -1.0, Double.class);
        testExpression("-1.1+2.1", 1.0, Double.class);
        testExpression("1.1+-2.1", -1.0, Double.class);
    }

    @Test
    public void testNegativeCoercion() {
        testExpression("-1+2.1", 1.1, Double.class);
        testExpression("-1.1+2", 0.9, Double.class);
        testExpression("1+-2.1", -1.1, Double.class);
        testExpression("1.1+-2", -0.9, Double.class);
    }

    @Test
    public void testString() {
        testExpression("\"A\"+\"B\"", "AB", String.class);
    }

    @Test
    public void testTypeMismatches() {
        testExpressionThrows("3+\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"+3", IllegalStateException.class);
        testExpressionThrows("3.2+\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"+3.2", IllegalStateException.class);
    }

    @Test
    public void testDoubleNoLeadingZero() {
        testExpression("1+.1", 1.1, Double.class);
        testExpression(".1+1", 1.1, Double.class);
    }

}
