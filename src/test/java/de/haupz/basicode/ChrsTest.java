package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class ChrsTest extends ExpressionTest {

    @Test
    public void testChrs() {
        testExpression("CHR$(65)", "A", String.class);
    }

    @Test
    public void testChrsDouble() {
        testExpression("CHR$(65.0)", "A", String.class);
        testExpression("CHR$(65.1)", "A", String.class);
        testExpression("CHR$(65.9)", "A", String.class);
    }

    @Test
    public void testChrsTypeError() {
        testExpressionThrows("CHR$(\"E\")", IllegalStateException.class);
    }

    @Test
    public void testChrsRangeError() {
        testExpressionThrows("CHR$(-1)", IllegalStateException.class);
        testExpressionThrows("CHR$(128)", IllegalStateException.class);
    }

}
