package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class VarTest extends ExpressionTest {

    @Test
    public void testInt() {
        state.setVar("AA", 23);
        testExpression("AA", 23, Integer.class);
    }

    @Test
    public void testDouble() {
        state.setVar("AA", 23.42);
        testExpression("AA", 23.42, Double.class);
    }

    @Test
    public void testString() {
        state.setVar("AA", "Hello.");
        testExpression("AA", "Hello.", String.class);
    }

}
