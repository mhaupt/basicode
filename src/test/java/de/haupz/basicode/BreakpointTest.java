package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class BreakpointTest extends InterpreterTest {

    @Test
    public void testBreakpoint() {
        testInterpreter("""
                1000 GOTO 20
                1010 GOSUB 963
                1020 GOTO 950
                """, """
                at line 1010, statement 0
                1010 GOSUB 963
                -----^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0
                """);
    }

    @Test
    public void testBreakpointGosub() {
        testInterpreter("""
                1000 GOTO 20
                1010 GOSUB 1100
                1020 GOTO 950
                1100 GOSUB 1200:RETURN
                1200 GOSUB 963:RETURN
                """,
                """
                at line 1200, statement 0
                1200 GOSUB 963:RETURN
                -----^
                at line 1100, statement 0
                1100 GOSUB 1200:RETURN
                -----^
                at line 1010, statement 0
                1010 GOSUB 1100
                -----^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0
                """);
    }

}
