package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class IntTest extends ExpressionTest {

    @Test
    public void testInteger() {
        testExpression("INT(1)", 1.0, Double.class);
        testExpression("INT(-1)", -1.0, Double.class);
    }

    @Test
    public void testDouble() {
        testExpression("INT(1.1)", 1.0, Double.class);
        testExpression("INT(-1.1)", -2.0, Double.class);
    }

    @Test
    public void testString() {
        testExpressionThrows("INT(\"Hello\")", IllegalStateException.class);
    }

}
