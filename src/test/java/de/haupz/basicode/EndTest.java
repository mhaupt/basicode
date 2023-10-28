package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class EndTest extends InterpreterTest {

    @Test
    public void testEnd() {
        testInterpreter("""
                10 PRINT "Hello."
                20 END
                30 PRINT "Invisible."
                """, "Hello.\n");
    }

    @Test
    public void testEndOnSameLine() {
        testInterpreter("""
                10 PRINT "Hello.":END
                20 PRINT "Invisible."
                """, "Hello.\n");
    }

    @Test
    public void testStop() {
        testInterpreter("""
                10 PRINT "Hello."
                20 STOP
                30 PRINT "Invisible."
                """, "Hello.\n");
    }

    @Test
    public void testStopOnSameLine() {
        testInterpreter("""
                10 PRINT "Hello.":STOP
                20 PRINT "Invisible."
                """, "Hello.\n");
    }

}
