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
                1
                2
                3
                """);
    }

    @Test
    public void testPostChecked() {
        testInterpreter("""
                10 FOR I=10 TO 1
                20 PRINT I
                30 NEXT I
                """, """
                10
                """);
    }

    @Test
    public void testStep() {
        testInterpreter("""
                10 FOR I=1 TO 12 STEP 3
                20 PRINT I
                30 NEXT I
                """, """
                1
                4
                7
                10
                """);
    }

    @Test
    public void testStepDown() {
        testInterpreter("""
                10 FOR I=20 TO 1 STEP -7
                20 PRINT I
                30 NEXT I
                """, """
                20
                13
                6
                """);
    }

    @Test
    public void testForDouble() {
        testInterpreter("""
                10 FOR I=1.1 TO 7.8 STEP 0.9
                20 PRINT I
                30 NEXT I
                """, """
                1.1
                2
                2.9
                3.8
                4.7
                5.6
                6.5
                7.4
                """);
    }

    @Test
    public void testForDoubleDown() {
        testInterpreter("""
                10 FOR I=7.2 TO 1.8 STEP -1.3
                20 PRINT I
                30 NEXT I
                """, """
                7.2
                5.9
                4.6
                3.3
                2
                """);
    }

    @Test
    public void testCoerceStep() {
        testInterpreter("""
                10 FOR I=1 TO 3 STEP 0.7
                20 PRINT I
                30 NEXT I
                """, """
                1
                1.7
                2.4
                """);
    }

    @Test
    public void testCoerceInit() {
        testInterpreter("""
                10 FOR I=1.1 TO 3
                20 PRINT I
                30 NEXT I
                """, """
                1.1
                2.1
                """);
    }

    @Test
    public void testCoerceEnd() {
        testInterpreter("""
                10 FOR I=1 TO 3.5
                20 PRINT I
                30 NEXT I
                """, """
                1
                2
                3
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
                0
                1
                2
                0
                1
                """);
    }

    @Test
    public void testAfterLoopValue() {
        testInterpreter("""
                1000 FOR I=1 TO 3:PRINT I:NEXT I
                1010 PRINT I
                """, """
                1
                2
                3
                4
                """);
    }

}
