package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class LogicTest extends ExpressionTest {

    @Test
    public void testOr() {
        testExpression("0 OR 1", 1, Integer.class);
        testExpression("1 OR 2", 3, Integer.class);
        testExpression("1 OR 0", 1, Integer.class);
        testExpression("2 OR 1", 3, Integer.class);
        testExpression("1.1 OR 2.2", 3, Integer.class);
    }
    
    @Test
    public void testAnd() {
        testExpression("0 AND 1", 0, Integer.class);
        testExpression("1 AND 3", 1, Integer.class);
        testExpression("1 AND 0", 0, Integer.class);
        testExpression("3 AND 1", 1, Integer.class);
        testExpression("1.1 AND 3.3", 1, Integer.class);
    }

    @Test
    public void testNot() {
        testExpression("NOT 0", -1, Integer.class);
        testExpression("NOT -1", 0, Integer.class);
        testExpression("NOT 42", -43, Integer.class);
        testExpression("NOT 0.5", -1, Integer.class);
    }

    @Test
    public void testStringError() {
        testExpressionThrows("\"A\" OR \"B\"", IllegalStateException.class);
        testExpressionThrows("\"A\" AND \"B\"", IllegalStateException.class);
        testExpressionThrows("NOT \"A\"", IllegalStateException.class);
    }

    @Test
    public void testTypeMismatch() {
        testExpressionThrows("\"A\" OR 1", IllegalStateException.class);
        testExpressionThrows("1 OR \"B\"", IllegalStateException.class);
        testExpressionThrows("\"A\" AND 1", IllegalStateException.class);
        testExpressionThrows("1 AND \"B\"", IllegalStateException.class);
    }

}
