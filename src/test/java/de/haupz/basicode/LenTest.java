package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class LenTest extends ExpressionTest {

    @Test
    public void testLen() {
        testExpression("LEN(\"Hello\")", 5.0, Double.class);
        testExpression("LEN(\"\")", 0.0, Double.class);
    }

    @Test
    public void testNoString() {
        testExpressionThrows("LEN(23)", IllegalStateException.class);
    }

}
