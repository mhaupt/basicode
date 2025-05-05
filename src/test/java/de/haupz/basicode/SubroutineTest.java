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
                 39\s
                 24\s
                 320\s
                 200\s
                 15\s
                """);
    }

    @Test
    public void testGosub330() {
        testInterpreter("""
                1000 GOTO 20
                1010 SR$="abcDEF{{}}[[]]"
                1020 GOSUB 330
                1030 PRINT SR$
                """, """
                ABCDEF[[]][[]]
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

    @Test
    public void testGotoInsteadOfGosub() {
        testInterpreter("""
                1000 GOTO 20
                1010 GOSUB 2000
                1020 PRINT "Returned."
                1030 GOTO 950
                2000 PRINT "Hello."
                2010 GOTO 120
                2020 PRINT "Not to be seen."
                2030 RETURN
                """,
                """
                Hello.
                Returned.
                """);
    }

}
