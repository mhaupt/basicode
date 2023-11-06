package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class GosubTest extends InterpreterTest {

    @Test
    public void testGosub() {
        testInterpreter("""
                1010 GOSUB 1040
                1020 PRINT "Two."
                1030 END
                1040 PRINT "One."
                1050 RETURN
                """, """
                One.
                Two.
                """);
    }

    @Test
    public void testMoreGosub() {
        testInterpreter("""
                1010 GOSUB 1050
                1020 GOSUB 1070
                1030 PRINT "Three."
                1040 END
                1050 PRINT "One."
                1060 RETURN
                1070 PRINT "Two."
                1080 RETURN
                """, """
                One.
                Two.
                Three.
                """);
    }

    @Test
    public void testNestedGosub() {
        testInterpreter("""
                1010 GOSUB 1040
                1020 PRINT "Four."
                1030 END
                1040 GOSUB 1070
                1050 PRINT "Three."
                1060 RETURN
                1070 PRINT "One."
                1080 GOSUB 1100
                1090 RETURN
                1100 PRINT "Two."
                1110 RETURN
                """, """
                One.
                Two.
                Three.
                Four.
                """);
    }

    @Test
    public void testContinueOnSameLine() {
        testInterpreter("""
                1010 PRINT "One."
                1020 GOSUB 1040:PRINT "Three."
                1030 END
                1040 PRINT "Two."
                1050 RETURN
                """, """
                One.
                Two.
                Three.
                """);
    }

    @Test
    public void testReturnOnSameLine() {
        testInterpreter("""
                1010 PRINT "One."
                1020 GOSUB 1040:PRINT "Three."
                1030 END
                1040 PRINT "Two.":RETURN
                """, """
                One.
                Two.
                Three.
                """);
    }

}
