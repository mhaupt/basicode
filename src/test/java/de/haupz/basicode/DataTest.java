package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class DataTest extends InterpreterTest {

    @Test
    public void testRead() {
        testInterpreter("""
                10 READ A:READ B$:READ C
                20 PRINT A:PRINT B$:PRINT C
                30 DATA 23,"Hello.",42.2
                """, """
                23
                Hello.
                42.2
                """);
    }

    @Test
    public void testRestore() {
        testInterpreter("""
                10 READ A:READ B$:READ C
                20 RESTORE
                30 READ D:READ E$:READ F
                40 PRINT A:PRINT B$:PRINT C
                50 PRINT D:PRINT E$:PRINT F
                60 DATA 23,"Hello.",42.2
                70 DATA 0,"nope",0
                """, """
                23
                Hello.
                42.2
                23
                Hello.
                42.2
                """);
    }

    @Test
    public void testReadList() {
        testInterpreter("""
                10 READ A,B$,C
                20 PRINT A:PRINT B$:PRINT C
                30 DATA 23,"Hello.",42.2
                """, """
                23
                Hello.
                42.2
                """);
    }

    @Test
    public void testRestoreList() {
        testInterpreter("""
                10 READ A,B$,C
                20 RESTORE
                30 READ D,E$,F
                40 PRINT A:PRINT B$:PRINT C
                50 PRINT D:PRINT E$:PRINT F
                60 DATA 23,"Hello.",42.2
                70 DATA 0,"nope",0
                """, """
                23
                Hello.
                42.2
                23
                Hello.
                42.2
                """);
    }

    @Test
    public void readNegativeNumbers() {
        testInterpreter("""
                10 READ A,B
                20 PRINT A:PRINT B
                30 DATA -4,-3.2
                """, """
                -4
                -3.2
                """);
    }

    @Test
    public void readMismatch() {
        testInterpreterThrows("""
                10 READ A
                20 DATA "oops"
                """, IllegalStateException.class);
        testInterpreterThrows("""
                10 READ A$
                20 DATA 123
                """, IllegalStateException.class);
        testInterpreterThrows("""
                10 READ A$
                20 DATA 123.4
                """, IllegalStateException.class);
    }

    @Test
    public void readTooMuch() {
        testInterpreterThrows("""
                10 READ A,B
                20 DATA 23
                """, IllegalStateException.class);
    }

}
