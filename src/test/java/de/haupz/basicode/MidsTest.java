package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class MidsTest extends ExpressionTest {

    @Test
    public void testBookExample() {
        testExpression("MID$(\"BASICODE-2\",5,6)", "CODE-2", String.class);
    }

    @Test
    public void testBookExampleDouble() {
        testExpression("MID$(\"BASICODE-2\",5.5,6.6)", "CODE-2", String.class);
    }

    @Test
    public void testTypeErrors() {
        testExpressionThrows("MID$(23,42,57)", IllegalStateException.class);
        testExpressionThrows("MID$(\"A\",\"B\",57)", IllegalStateException.class);
        testExpressionThrows("MID$(\"A\",42,\"A\")", IllegalStateException.class);
    }

    @Test
    public void testEmptyString() {
        testExpression("MID$(\"\",1,1)", "", String.class);
    }

    @Test
    public void testShortExamples() {
        testExpression("MID$(\"ABC\",1,2)", "AB", String.class);
        testExpression("MID$(\"ABC\",2,1)", "B", String.class);
        testExpression("MID$(\"ABC\",1,3)", "ABC", String.class);
        testExpression("MID$(\"ABC\",2,2)", "BC", String.class);
    }

    @Test
    public void testWeirdExamples() {
        testExpression("MID$(\"ABC\",4,1)", "", String.class);
        testExpression("MID$(\"ABC\",-1,2)", "AB", String.class);
        testExpression("MID$(\"ABC\",0,2)", "AB", String.class);
        testExpression("MID$(\"ABC\",1,-1)", "ABC", String.class);
        testExpression("MID$(\"ABC\",2,-1)", "BC", String.class);
    }

    @Test
    public void testDifferentLengths() {
        testExpression("MID$(\"BASICODE\",6,1)", "O", String.class);
        testExpression("MID$(\"BASICODE\",6,2)", "OD", String.class);
        testExpression("MID$(\"BASICODE\",8,1)", "E", String.class);
    }

    @Test
    public void testOmitLengthArgument() {
        testExpression("MID$(\"BASICODE\",6)", "ODE", String.class);
        testExpression("MID$(\"BASICODE\",7)", "DE", String.class);
        testExpression("MID$(\"BASICODE\",8)", "E", String.class);
    }

    @Test
    public void testb64() {
        testExpression("MID$(\"b64\",2,2)", "64", String.class);
    }

}
