package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class NumberFormatTest extends InterpreterTest {

    @Test
    public void testGosub300() {
        testInterpreter("""
                1000 GOTO 20
                1010 SR=3.141:GOSUB 300:PRINT SR$
                1020 SR=1000:GOSUB 300:PRINT SR$
                1030 SR=-1.2345678:GOSUB 300:PRINT SR$
                1040 SR=-200000000:GOSUB 300:PRINT SR$
                1050 GOTO 950
                """, """
                3.141
                1000
                -1.2345678
                -200000000
                """);
    }

    @Test
    public void testFormatNumber() {
        testInterpreter("""
                1000 GOTO 20
                1010 CT=6:CN=0:SR=100000:GOSUB 310:PRINT SR$
                1020 CT=7:CN=2:SR=1000.23:GOSUB 310:PRINT SR$
                1030 CT=7:CN=2:SR=-999.42:GOSUB 310:PRINT SR$
                1040 CT=6:CN=0:SR=-99999:GOSUB 310:PRINT SR$
                1050 GOTO 950
                """, """
                100000
                1000.23
                -999.42
                -99999
                """);
    }

    @Test
    public void testRightAlignment() {
        testInterpreter("""
                1000 GOTO 20
                1010 CT=15:CN=2
                1020 SR=100.423:GOSUB 310:PRINT SR$;
                1030 SR=-4444.765:GOSUB 310:PRINT SR$
                1040 GOTO 950
                """, """
                         100.42       -4444.77
                """);
    }

    @Test
    public void testFormatNumberFailing() {
        testInterpreter("""
                1000 GOTO 20
                1010 CT=5
                1020 CN=0:SR=100000:GOSUB 310:PRINT SR$
                1030 CN=2:SR=1000.23:GOSUB 310:PRINT SR$
                1040 CN=2:SR=-999.42:GOSUB 310:PRINT SR$
                1050 CN=0:SR=-99999:GOSUB 310:PRINT SR$
                1060 GOTO 950
                """, """
                *****
                *****
                *****
                *****
                """);
    }

}
