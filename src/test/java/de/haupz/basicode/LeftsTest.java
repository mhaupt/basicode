package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class LeftsTest extends ExpressionTest {

    @Test
    public void testEmptyString() {
        testExpression("LEFT$(\"\", 1)", "", String.class);
    }

    @Test
    public void testTypeError() {
        testExpressionThrows("LEFT$(42, 23)", IllegalStateException.class);
        testExpressionThrows("LEFT$(\"Hello\", \"A\")", IllegalStateException.class);
    }

    @Test
    public void testGoodLength() {
        testExpression("LEFT$(\"BASICODE\", 5)", "BASIC", String.class);
        testExpression("LEFT$(\"BASICODE\", 3)", "BAS", String.class);
        testExpression("LEFT$(\"BASICODE\", 8)", "BASICODE", String.class);
    }

    @Test
    public void testExcessiveLength() {
        testExpression("LEFT$(\"BASICODE\", 10)", "BASICODE", String.class);
    }

    @Test
    public void testZeroLength() {
        testExpression("LEFT$(\"Hello\", 0)", "", String.class);
    }

    @Test
    public void testDouble() {
        testExpression("LEFT$(\"BASICODE\", 5.0)", "BASIC", String.class);
    }

}
