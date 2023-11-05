package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class Goto950Test extends InterpreterTest {

    @Test
    public void testGoto950() {
        testInterpreter("""
                1000 PRINT "Hello."
                1010 GOTO 950
                1020 PRINT "Not to be seen."
                """, """
                Hello.
                """);
    }

}
