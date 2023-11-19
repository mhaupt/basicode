package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class KeyboardTest extends InterpreterTest {

    @Test
    public void testYesNo() {
        testInterpreter("""
                1000 PRINT "(Y)es/(N)o? ";
                1010 GOSUB 210:PRINT
                1020 IF IN$="Y" THEN PRINT "Yooooo"
                1030 IF IN$="N" THEN PRINT "nope"
                1040 GOTO 950
                """, """
                Y
                """, """
                (Y)es/(N)o? Y
                Yooooo
                """);
    }

}
