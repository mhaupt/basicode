package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class ValTest extends ExpressionTest {

    @Test
    public void testInteger() {
        testExpression("VAL(\"0\")", 0.0, Double.class);
        testExpression("VAL(\"23\")", 23.0, Double.class);
        testExpression("VAL(\"-23\")", -23.0, Double.class);
    }

    @Test
    public void testDouble() {
        testExpression("VAL(\"0.0\")", 0.0, Double.class);
        testExpression("VAL(\"23.0\")", 23.0, Double.class);
        testExpression("VAL(\"-23.5\")", -23.5, Double.class);
    }

    @Test
    public void testStrangeInput() {
        testExpression("VAL(\"2+4\")", 2.0, Double.class);
        testExpression("VAL(\"Hello\")", 0.0, Double.class);
    }

}
