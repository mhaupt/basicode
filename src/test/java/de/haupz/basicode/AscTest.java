package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class AscTest extends ExpressionTest {

    @Test
    public void testSingleCharacter() {
        testExpression("ASC(\"A\")", 65, Integer.class);
    }

    @Test
    public void testString() {
        testExpression("ASC(\"BCD\")", 66, Integer.class);
    }

    @Test
    public void testEmptyString() {
        testExpression("ASC(\"\")", -1, Integer.class);
    }

}
