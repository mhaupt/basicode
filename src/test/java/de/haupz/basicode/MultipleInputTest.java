package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class MultipleInputTest extends InterpreterTest {

    @Test
    public void testMultipleInput() {
        testInterpreter("""
                10 INPUT A
                20 INPUT B
                30 PRINT A+B
                """, """
                23
                42
                """, """
                ? ?  65\s
                """);
    }

}
