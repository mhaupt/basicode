package de.haupz.basicode;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class GosubTest extends InterpreterTest {

    @Test
    public void testGosub() {
        testInterpreter("""
                10 GOSUB 40
                20 PRINT "Two."
                30 END
                40 PRINT "One."
                50 RETURN
                """, """
                One.
                Two.
                """);
    }

    @Test
    public void testMoreGosub() {
        testInterpreter("""
                10 GOSUB 50
                20 GOSUB 70
                30 PRINT "Three."
                40 END
                50 PRINT "One."
                60 RETURN
                70 PRINT "Two."
                80 RETURN
                """, """
                One.
                Two.
                Three.
                """);
    }

    @Test
    public void testNestedGosub() {
        testInterpreter("""
                10 GOSUB 40
                20 PRINT "Four."
                30 END
                40 GOSUB 70
                50 PRINT "Three."
                60 RETURN
                70 PRINT "One."
                80 GOSUB 100
                90 RETURN
                100 PRINT "Two."
                110 RETURN
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
                10 PRINT "One."
                20 GOSUB 40:PRINT "Three."
                30 END
                40 PRINT "Two."
                50 RETURN
                """, """
                One.
                Two.
                Three.
                """);
    }

    @Test
    public void testReturnOnSameLine() {
        testInterpreter("""
                10 PRINT "One."
                20 GOSUB 40:PRINT "Three."
                30 END
                40 PRINT "Two.":RETURN
                """, """
                One.
                Two.
                Three.
                """);
    }

}
