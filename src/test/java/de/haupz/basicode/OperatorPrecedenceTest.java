package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class OperatorPrecedenceTest extends ExpressionTest {

    @Test
    public void testMultiplicationPrecedence() {
        testExpression("2+3*4", 14, Integer.class);
        testExpression("2*3+4", 10, Integer.class);
        testExpression("2-3*4", -10, Integer.class);
        testExpression("2*3-4", 2, Integer.class);
    }

    @Test
    public void testDivisionPrecedence() {
        testExpression("20+10/5", 22, Integer.class);
        testExpression("20/10+5", 7, Integer.class);
        testExpression("20-10/5", 18, Integer.class);
        testExpression("20/10-5", -3, Integer.class);
    }

    @Test
    public void testBrackets() {
        testExpression("(2+3)*4", 20, Integer.class);
        testExpression("2*(3+4)", 14, Integer.class);
        testExpression("(2-3)*4", -4, Integer.class);
        testExpression("2*(3-4)", -2, Integer.class);
    }

}
