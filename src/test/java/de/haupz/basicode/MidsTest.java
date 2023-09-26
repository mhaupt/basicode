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

}
