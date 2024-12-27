package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class UnaryPlusTest extends ExpressionTest {

    @Test
    public void testValues() {
        testExpression("+1", 1.0, Double.class);
        testExpression("+1.2", 1.2, Double.class);
    }

    @Test
    public void testNoNumber() {
        testExpressionThrows("+hello", IllegalStateException.class);
    }

    @Test
    public void testExpression() {
        testExpression("+1+2", 3.0, Double.class);
        testExpression("1++2", 3.0, Double.class);
        testExpression("+1++2", 3.0, Double.class);
    }

}
