package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class WatchpointTest extends InterpreterTest {

    @Test
    public void testSingleTrigger() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM OD$(1):OD$(1)="A"
                1020 OC$="A=2":GOSUB 965:PRINT "watchpoint";OP
                1030 A=1:A=2:A=3
                1040 GOTO 950
                """, """
                watchpoint 1\s
                at line 1030, statement 1
                1030 A=1:A=2:A=3
                ---------^
                == variables ==
                A = 2.0\n
                """);
    }

}
