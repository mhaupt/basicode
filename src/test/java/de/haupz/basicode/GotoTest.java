package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class GotoTest extends InterpreterTest {

    @Test
    public void testGoto() {
        testInterpreter("""
                10 PRINT "Hello."
                20 GOTO 40
                30 PRINT "You will not see this."
                40 PRINT "Good bye."
                """, """
                Hello.
                Good bye.
                """);
    }

    @Test
    public void testGotoBackAndForth() {
        testInterpreter("""
                10 GOTO 40
                20 PRINT "Two."
                30 GOTO 60
                40 PRINT "One."
                50 GOTO 20
                60 PRINT "Three."
                """, """
                One.
                Two.
                Three.
                """);
    }

}
