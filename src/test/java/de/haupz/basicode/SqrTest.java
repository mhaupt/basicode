package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class SqrTest extends ExpressionTest {

    @Test
    public void testZero() {
        testExpression("SQR(0)", 0.0, Double.class);
        testExpression("SQR(0.0)", 0.0, Double.class);
    }

    @Test
    public void testPositiveInteger() {
        testExpression("SQR(5)", 2.236067, Double.class);
        testExpression("SQR(9)", 3.0, Double.class);
    }

    @Test
    public void testPositiveDouble() {
        testExpression("SQR(5.0)", 2.236067, Double.class);
        testExpression("SQR(9.0)", 3.0, Double.class);
    }

    @Test
    public void testNegative() {
        testExpressionThrows("SQR(-1)", ArithmeticException.class);
        testExpressionThrows("SQR(-1.0)", ArithmeticException.class);
    }

}
