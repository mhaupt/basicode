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
                 23\s
                Hello.
                 42.2\s
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
                 23\s
                Hello.
                 42.2\s
                 23\s
                Hello.
                 42.2\s
                """);
    }

    @Test
    public void testReadList() {
        testInterpreter("""
                10 READ A,B$,C
                20 PRINT A:PRINT B$:PRINT C
                30 DATA 23,"Hello.",42.2
                """, """
                 23\s
                Hello.
                 42.2\s
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
                 23\s
                Hello.
                 42.2\s
                 23\s
                Hello.
                 42.2\s
                """);
    }

    @Test
    public void readNegativeNumbers() {
        testInterpreter("""
                10 READ A,B
                20 PRINT A:PRINT B
                30 DATA -4,-3.2
                """, """
                -4\s
                -3.2\s
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

    @Test
    public void readArray1D() {
        testInterpreter("""
                1000 DIM AR(3)
                1010 FOR I=1 TO 3:READ AR(I):NEXT I
                1020 FOR J=1 TO 3:PRINT AR(J):NEXT J
                1030 DATA 23,42,148
                """, """
                 23\s
                 42\s
                 148\s
                """);
    }

    @Test
    public void readArray2D() {
        testInterpreter("""
                1000 DIM AR(3,3)
                1010 FOR I=1 TO 3:READ AR(I,I):NEXT I
                1020 FOR J=1 TO 3:PRINT AR(J,J):NEXT J
                1030 DATA 23,42,148
                """, """
                 23\s
                 42\s
                 148\s
                """);
    }

}
