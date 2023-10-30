package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class PrintTest extends InterpreterTest {

    @Test
    public void testPrintComma() {
        testInterpreter("""
                10 PRINT 1,2,3
                """, """
                1
                2
                3
                """);
    }

    @Test
    public void testPrintSemicolon() {
        testInterpreter("""
                10 PRINT 1;2;3
                """, "123\n");
    }

    @Test
    public void testPrintSemicolonNoNewLine() {
        testInterpreter("""
                10 PRINT 1;2;3;
                """, "123");
    }

}
