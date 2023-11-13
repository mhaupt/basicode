package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class OperatorPrecedenceTest extends ExpressionTest {

    @Test
    public void testMultiplicationPrecedence() {
        testExpression("2+3*4", 14.0, Double.class);
        testExpression("2*3+4", 10.0, Double.class);
        testExpression("2-3*4", -10.0, Double.class);
        testExpression("2*3-4", 2.0, Double.class);
    }

    @Test
    public void testDivisionPrecedence() {
        testExpression("20+10/5", 22.0, Double.class);
        testExpression("20/10+5", 7.0, Double.class);
        testExpression("20-10/5", 18.0, Double.class);
        testExpression("20/10-5", -3.0, Double.class);
    }

    @Test
    public void testBrackets() {
        testExpression("(2+3)*4", 20.0, Double.class);
        testExpression("2*(3+4)", 14.0, Double.class);
        testExpression("(2-3)*4", -4.0, Double.class);
        testExpression("2*(3-4)", -2.0, Double.class);
    }

    @Test
    public void testPowerPrecedence() {
        testExpression("2+2^3+2", 12.0, Double.class);
    }

}
