package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class AbsTest extends ExpressionTest {

    @Test
    public void testPositiveInteger() {
        testExpression("ABS(23)", 23, Integer.class);
    }

}
