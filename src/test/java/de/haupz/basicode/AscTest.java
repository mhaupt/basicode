package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class AscTest extends ExpressionTest {

    @Test
    public void testSingleCharacter() {
        testExpression("ASC(\"A\")", 65.0, Double.class);
    }

    @Test
    public void testString() {
        testExpression("ASC(\"BCD\")", 66.0, Double.class);
    }

    @Test
    public void testEmptyString() {
        testExpression("ASC(\"\")", -1.0, Double.class);
    }

}
