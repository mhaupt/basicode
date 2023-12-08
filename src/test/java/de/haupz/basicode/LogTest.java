package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class LogTest extends ExpressionTest {

    @Test
    public void testLog() {
        testExpression("LOG(10)", 1.0, Double.class);
        testExpression("LOG(100)", 2.0, Double.class);
        testExpression("LOG(23)", 1.36172784, Double.class);
    }

    @Test
    public void testIllegalValues() {
        testExpressionThrows("LOG(0)", IllegalStateException.class);
        testExpressionThrows("LOG(-1)", IllegalStateException.class);
    }

}
