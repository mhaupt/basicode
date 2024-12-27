package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class ExpTest extends ExpressionTest {

    @Test
    public void testExp() {
        testExpression("EXP(1)", 2.71828182, Double.class);
        testExpression("EXP(0)", 1.0, Double.class);
        testExpression("EXP(-1)", 0.36787944, Double.class);
    }

}
