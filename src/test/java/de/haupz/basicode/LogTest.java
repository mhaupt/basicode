package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class LogTest extends ExpressionTest {

    @Test
    public void testLog() {
        testExpression("LOG(2.71828)", 1.0, Double.class);
        testExpression("LOG(10)", 2.30258509, Double.class);
        testExpression("LOG(23)", 3.13549421, Double.class);
    }

    @Test
    public void testIllegalValues() {
        testExpressionThrows("LOG(0)", IllegalStateException.class);
        testExpressionThrows("LOG(-1)", IllegalStateException.class);
    }

}
