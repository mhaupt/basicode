package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class DependentJumpTest extends InterpreterTest {

    @Test
    public void testOnGoto() {
        testInterpreter("""
                1000 A=3
                2000 ON A GOTO 4000,5000,6000,7000
                3000 PRINT "Not to be seen."
                4000 PRINT "A"
                5000 PRINT "B"
                6000 PRINT "C"
                7000 PRINT "D"
                """, """
                C
                D
                """);
    }

    @Test
    public void testOnGosub() {
        testInterpreter("""
                1000 A=3
                2000 ON A GOSUB 4000,5000,6000,7000
                3000 END
                4000 PRINT "A":RETURN
                5000 PRINT "B":RETURN
                6000 PRINT "C":RETURN
                7000 PRINT "D":RETURN
                """, """
                C
                """);
    }

    @Test
    public void testOnGotoFloat() {
        testInterpreter("""
                1000 A=3.5
                2000 ON A GOTO 4000,5000,6000,7000
                3000 PRINT "Not to be seen."
                4000 PRINT "A"
                5000 PRINT "B"
                6000 PRINT "C"
                7000 PRINT "D"
                """, """
                C
                D
                """);
    }

    @Test
    public void testOnGosubFloat() {
        testInterpreter("""
                1000 A=3.5
                2000 ON A GOSUB 4000,5000,6000,7000
                3000 END
                4000 PRINT "A":RETURN
                5000 PRINT "B":RETURN
                6000 PRINT "C":RETURN
                7000 PRINT "D":RETURN
                """, """
                C
                """);
    }

    @Test
    public void testWrongIndexType() {
        testInterpreterThrows("""
                1000 A$="oops"
                2000 ON A$ GOTO 3000
                3000 END
                """, IllegalStateException.class);
    }

    @Test
    public void testIndexOutOfBounds() {
        testInterpreterThrows("""
                1000 A=0
                2000 ON A GOTO 3000
                3000 END
                """, IllegalStateException.class);
        testInterpreterThrows("""
                1000 A=4
                2000 ON A GOTO 3000,4000,5000
                3000 END
                """, IllegalStateException.class);
    }

    @Test
    public void testOnGotoSubroutine() {
        testInterpreter("""
                1000 GOTO 20
                1010 FOR K=1 TO 2
                1020 ON K GOTO 1030,950
                1030 PRINT "1030"
                1040 NEXT K
                """, """
                1030
                """);
    }

}
