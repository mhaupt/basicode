package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class AtnTest extends ExpressionTest {

    @Test
    public void testZero() {
        testExpression("ATN(0)", 0.0, Double.class);
        testExpression("ATN(0.0)", 0.0, Double.class);
    }

    @Test
    public void testInteger() {
        testExpression("ATN(1)", 0.785398, Double.class);
        testExpression("ATN(-1)", -0.785398, Double.class);
    }

    @Test
    public void testDouble() {
        testExpression("ATN(1.0)", 0.785398, Double.class);
        testExpression("ATN(-1.0)", -0.785398, Double.class);
    }

}
