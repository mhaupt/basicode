package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class SubroutineTest extends InterpreterTest {

    @Test
    public void testGoto20() {
        testInterpreter("""
                1000 GOTO 20
                1010 PRINT HO
                1020 PRINT VE
                1030 PRINT HG
                1040 PRINT VG
                1050 PRINT SV
                """, """
                39
                24
                320
                200
                15
                """);
    }

    @Test
    public void testGoto950() {
        testInterpreter("""
                1000 PRINT "Hello."
                1010 GOTO 950
                1020 PRINT "Not to be seen."
                """, """
                Hello.
                """);
    }

}
