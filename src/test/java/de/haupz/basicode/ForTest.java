package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class ForTest extends InterpreterTest {

    @Test
    public void testFor() {
        testInterpreter("""
                10 FOR I=1 TO 3
                20 PRINT I
                30 NEXT I
                """, """
                 1\s
                 2\s
                 3\s
                """);
    }

    @Test
    public void testPostChecked() {
        testInterpreter("""
                10 FOR I=10 TO 1
                20 PRINT I
                30 NEXT I
                """, """
                 10\s
                """);
    }

    @Test
    public void testStep() {
        testInterpreter("""
                10 FOR I=1 TO 12 STEP 3
                20 PRINT I
                30 NEXT I
                """, """
                 1\s
                 4\s
                 7\s
                 10\s
                """);
    }

    @Test
    public void testStepDown() {
        testInterpreter("""
                10 FOR I=20 TO 1 STEP -7
                20 PRINT I
                30 NEXT I
                """, """
                 20\s
                 13\s
                 6\s
                """);
    }

    @Test
    public void testForDouble() {
        testInterpreter("""
                10 FOR I=1.1 TO 7.8 STEP 0.9
                20 PRINT I
                30 NEXT I
                """, """
                 1.1\s
                 2\s
                 2.9\s
                 3.8\s
                 4.7\s
                 5.6\s
                 6.5\s
                 7.4\s
                """);
    }

    @Test
    public void testForDoubleDown() {
        testInterpreter("""
                10 FOR I=7.2 TO 1.8 STEP -1.3
                20 PRINT I
                30 NEXT I
                """, """
                 7.2\s
                 5.9\s
                 4.6\s
                 3.3\s
                 2\s
                """);
    }

    @Test
    public void testCoerceStep() {
        testInterpreter("""
                10 FOR I=1 TO 3 STEP 0.7
                20 PRINT I
                30 NEXT I
                """, """
                 1\s
                 1.7\s
                 2.4\s
                """);
    }

    @Test
    public void testCoerceInit() {
        testInterpreter("""
                10 FOR I=1.1 TO 3
                20 PRINT I
                30 NEXT I
                """, """
                 1.1\s
                 2.1\s
                """);
    }

    @Test
    public void testCoerceEnd() {
        testInterpreter("""
                10 FOR I=1 TO 3.5
                20 PRINT I
                30 NEXT I
                """, """
                 1\s
                 2\s
                 3\s
                """);
    }

    @Test
    public void testStringIterator() {
        testInterpreterThrows("""
                10 FOR A$=1 TO 7
                20 NEXT I
                """, IllegalStateException.class);
    }

    @Test
    public void testStringInit() {
        testInterpreterThrows("""
                10 FOR A="boop" TO 7
                20 NEXT I
                """, IllegalStateException.class);
    }

    @Test
    public void testStringEnd() {
        testInterpreterThrows("""
                10 FOR A=1 TO "moop"
                20 NEXT I
                """, IllegalStateException.class);
    }

    @Test
    public void testStringStep() {
        testInterpreterThrows("""
                10 FOR A=1 TO 7 STEP "ieps"
                20 NEXT I
                """, IllegalStateException.class);
    }

    @Test
    public void testForReInit() {
        testInterpreter("""
                1000 FOR X=0 TO 1:PRINT X:NEXT X
                1020 PRINT X
                1010 FOR X=0 TO 1:PRINT X:NEXT X
                """, """
                 0\s
                 1\s
                 2\s
                 0\s
                 1\s
                """);
    }

    @Test
    public void testAfterLoopValue() {
        testInterpreter("""
                1000 FOR I=1 TO 3:PRINT I:NEXT I
                1010 PRINT I
                """, """
                 1\s
                 2\s
                 3\s
                 4\s
                """);
    }

    @Test
    public void testMultipleNext() {
        testInterpreter("""
                1000 FOR X=0 TO 5
                1010 IF X=1 THEN PRINT "one":NEXT X:PRINT "one out"
                1020 IF X=3 THEN PRINT "three":NEXT X:PRINT "three out"
                1030 IF X=5 THEN PRINT "five":NEXT X:PRINT "five out"
                1040 PRINT X:IF X<5 THEN NEXT X
                1050 PRINT "end"
                """, """
                 0\s
                one
                 2\s
                three
                 4\s
                five
                five out
                 6\s
                end
                """);
    }

    @Test
    public void testForFromUndefinedVariable() {
        testInterpreter("""
                1000 FOR I=A TO 3
                1010 PRINT I
                1020 NEXT I
                """, """
                 0\s
                 1\s
                 2\s
                 3\s
                """);
    }

}
