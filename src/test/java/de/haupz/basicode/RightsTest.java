package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class RightsTest extends ExpressionTest {

    @Test
    public void testEmptyString() {
        testExpression("RIGHT$(\"\", 1)", "", String.class);
    }

    @Test
    public void testTypeError() {
        testExpressionThrows("RIGHT$(42, 23)", IllegalStateException.class);
        testExpressionThrows("RIGHT$(\"Hello\", \"A\")", IllegalStateException.class);
    }

    @Test
    public void testGoodLength() {
        testExpression("RIGHT$(\"BASICODE\", 5)", "ICODE", String.class);
        testExpression("RIGHT$(\"BASICODE\", 3)", "ODE", String.class);
        testExpression("RIGHT$(\"BASICODE\", 8)", "BASICODE", String.class);
    }

    @Test
    public void testExcessiveLength() {
        testExpression("RIGHT$(\"BASICODE\", 10)", "BASICODE", String.class);
    }

    @Test
    public void testZeroLength() {
        testExpression("RIGHT$(\"Hello\", 0)", "", String.class);
    }

    @Test
    public void testDouble() {
        testExpression("RIGHT$(\"BASICODE\", 5.0)", "ICODE", String.class);
    }

}
