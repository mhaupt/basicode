package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class SgnTest extends ExpressionTest {

    @Test
    public void testZero() {
        testExpression("SGN(0)", 0.0, Double.class);
        testExpression("SGN(0.0)", 0.0, Double.class);
    }

    @Test
    public void testInteger() {
        testExpression("SGN(1)", 1.0, Double.class);
        testExpression("SGN(1234)", 1.0, Double.class);
        testExpression("SGN(-1)", -1.0, Double.class);
        testExpression("SGN(-1234)", -1.0, Double.class);
    }

    @Test
    public void testDouble() {
        testExpression("SGN(1.0)", 1.0, Double.class);
        testExpression("SGN(1234.0)", 1.0, Double.class);
        testExpression("SGN(-1.0)", -1.0, Double.class);
        testExpression("SGN(-1234.0)", -1.0, Double.class);
    }

}
