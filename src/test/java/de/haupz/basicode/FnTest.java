package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class FnTest extends InterpreterTest {

    @Test
    public void testFnNoSpaceCallNoSpace() {
        testInterpreter("""
                10 DEF FNMU(X)=X*3
                20 PRINT FNMU(2)
                """, """
                6
                """);
    }

    @Test
    public void testFnSpaceCallNoSpace() {
        testInterpreter("""
                10 DEF FN MU(X)=X*3
                20 PRINT FNMU(2)
                """, """
                6
                """);
    }

    @Test
    public void testFnNoSpaceCallSpace() {
        testInterpreter("""
                10 DEF FNMU(X)=X*3
                20 PRINT FN MU(2)
                """, """
                6
                """);
    }

    @Test
    public void testFnSpaceCallSpace() {
        testInterpreter("""
                10 DEF FN MU(X)=X*3
                20 PRINT FN MU(2)
                """, """
                6
                """);
    }

    @Test
    public void testCaseInsensitive() {
        testInterpreter("""
                10 DEF FNMU(X)=X*3
                20 PRINT FNmu(2)
                """, """
                6
                """);
        testInterpreter("""
                10 DEF FNmu(X)=X*3
                20 PRINT FNMU(2)
                """, """
                6
                """);
    }

}
