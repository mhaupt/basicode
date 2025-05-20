package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class SimpleInterpreterTest extends InterpreterTest {

    @Test
    public void testSingleLineHello() {
        testInterpreter("10 PRINT \"Hello\"", "Hello\n");
    }

    @Test
    public void testMultipleLineHello() {
        testInterpreter("""
                10 PRINT "Hello"
                20 PRINT "boop":PRINT "moop"
                """,
                """
                Hello
                boop
                moop
                """);
    }

    @Test
    public void testRemNoOutput() {
        testInterpreter("10 REM mieps mieps", "");
    }

    @Test
    public void testRemPrintNoOutput() {
        testInterpreter("10 REM mieps mieps:PRINT \"nope\"", "");
    }

    @Test
    public void testPrintRem() {
        testInterpreter("10 PRINT \"Hello\":REM mieps mieps", "Hello\n");
    }

    @Test
    public void testPrintRemPrintNoOutput() {
        testInterpreter("10 PRINT \"Hello\":REM mieps mieps:PRINT \"nope\"", "Hello\n");
    }

    @Test
    public void testPrintRemPrint() {
        testInterpreter("""
                10 PRINT "Hello"
                20 REM nope nothing to see here
                30 PRINT "world"
                """,
                """
                Hello
                world
                """);
    }

    @Test
    public void testHelloWorld() {
        testInterpreter("10 PRINT \"Hello, world!\"", "Hello, world!\n");
    }

    @Test
    public void testInteger() {
        testInterpreter("10 PRINT 23", " 23 \n");
    }

    @Test
    public void testExpression() {
        testInterpreter("10 PRINT 3+4", " 7 \n");
    }

    @Test
    public void testAssignments() {
        testInterpreter("""
                10 AA=23
                20 BB=42
                30 CC=AA+BB
                40 PRINT CC
                """, """
                 65\s
                """);
    }

    @Test
    public void testUpperCaseLowerCaseIdentifier() {
        testInterpreter("""
                10 FOR I=1 TO 3
                20 PRINT i;I
                30 NEXT I
                """, """
                 1  1\s
                 2  2\s
                 3  3\s
                """);
    }

    @Test
    public void testLowerCaseUpperCaseIdentifier() {
        testInterpreter("""
                10 FOR i=1 TO 3
                20 PRINT i;I
                30 NEXT i
                """, """
                 1  1\s
                 2  2\s
                 3  3\s
                """);
    }

}
