package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class ComparisonTest extends ExpressionTest {

    @Test
    public void testEqualsTrue() {
        testExpression("1=1", -1, Integer.class);
        testExpression("1.1=1.1", -1, Integer.class);
        testExpression("\"A\"=\"A\"", -1, Integer.class);
    }

    @Test
    public void testEqualsFalse() {
        testExpression("1=2", 0, Integer.class);
        testExpression("1.1=1.2", 0, Integer.class);
        testExpression("\"A\"=\"B\"", 0, Integer.class);
    }

    @Test
    public void testNotEqualsTrue() {
        testExpression("1<>2", -1, Integer.class);
        testExpression("1.1<>1.2", -1, Integer.class);
        testExpression("\"A\"<>\"B\"", -1, Integer.class);
    }

    @Test
    public void testNotEqualsFalse() {
        testExpression("1<>1", 0, Integer.class);
        testExpression("1.1<>1.1", 0, Integer.class);
        testExpression("\"A\"<>\"A\"", 0, Integer.class);
    }

    @Test
    public void testCoercion() {
        testExpression("1=1.0", -1, Integer.class);
        testExpression("1.0=1", -1, Integer.class);
        testExpression("1=1.1", 0, Integer.class);
        testExpression("1.1=1", 0, Integer.class);
        testExpression("1<>1.1", -1, Integer.class);
        testExpression("1.1<>1", -1, Integer.class);
        testExpression("1<>1.0", 0, Integer.class);
        testExpression("1.0<>1", 0, Integer.class);
    }

    @Test
    public void testTypeMismatch() {
        testExpressionThrows("\"A\"=1", IllegalStateException.class);
        testExpressionThrows("1=\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"=1.0", IllegalStateException.class);
        testExpressionThrows("1.0=\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<>1", IllegalStateException.class);
        testExpressionThrows("1<>\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<>1.0", IllegalStateException.class);
        testExpressionThrows("1.0<>\"A\"", IllegalStateException.class);
    }

}
