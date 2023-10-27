package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class DependentJumpTest extends InterpreterTest {

    @Test
    public void testOnGoto() {
        testInterpreter("""
                10 A=3
                20 ON A GOTO 40,50,60,70
                30 PRINT "Not to be seen."
                40 PRINT "A"
                50 PRINT "B"
                60 PRINT "C"
                70 PRINT "D"
                """, """
                C
                D
                """);
    }

    @Test
    public void testOnGosub() {
        testInterpreter("""
                10 A=3
                20 ON A GOSUB 40,50,60,70
                30 END
                40 PRINT "A":RETURN
                50 PRINT "B":RETURN
                60 PRINT "C":RETURN
                70 PRINT "D":RETURN
                """, """
                C
                """);
    }

    @Test
    public void testOnGotoFloat() {
        testInterpreter("""
                10 A=3.5
                20 ON A GOTO 40,50,60,70
                30 PRINT "Not to be seen."
                40 PRINT "A"
                50 PRINT "B"
                60 PRINT "C"
                70 PRINT "D"
                """, """
                C
                D
                """);
    }

    @Test
    public void testOnGosubFloat() {
        testInterpreter("""
                10 A=3.5
                20 ON A GOSUB 40,50,60,70
                30 END
                40 PRINT "A":RETURN
                50 PRINT "B":RETURN
                60 PRINT "C":RETURN
                70 PRINT "D":RETURN
                """, """
                C
                """);
    }

    @Test
    public void testWrongIndexType() {
        testInterpreterThrows("""
                10 A$="oops"
                20 ON A$ GOTO 30
                30 END
                """, IllegalStateException.class);
    }

    @Test
    public void testIndexOutOfBounds() {
        testInterpreterThrows("""
                10 A=0
                20 ON A GOTO 30
                30 END
                """, IllegalStateException.class);
        testInterpreterThrows("""
                10 A=4
                20 ON A GOTO 30,40,50
                30 END
                """, IllegalStateException.class);
    }

}
