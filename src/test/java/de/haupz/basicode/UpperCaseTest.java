package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class UpperCaseTest extends InterpreterTest {

    @Test
    public void testUpperCase() {
        testInterpreter("""
                1000 GOTO 20
                1010 SR$="Hello, 123.":GOSUB 330:PRINT SR$
                1020 GOTO 950
                """, """
                HELLO, 123.
                """);
    }

}
