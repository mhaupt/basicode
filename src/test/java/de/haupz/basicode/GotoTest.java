package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class GotoTest extends InterpreterTest {

    @Test
    public void testGoto() {
        testInterpreter("""
                1010 PRINT "Hello."
                1020 GOTO 1040
                1030 PRINT "You will not see this."
                1040 PRINT "Good bye."
                """, """
                Hello.
                Good bye.
                """);
    }

    @Test
    public void testGotoBackAndForth() {
        testInterpreter("""
                1010 GOTO 1040
                1020 PRINT "Two."
                1030 GOTO 1060
                1040 PRINT "One."
                1050 GOTO 1020
                1060 PRINT "Three."
                """, """
                One.
                Two.
                Three.
                """);
    }

}
