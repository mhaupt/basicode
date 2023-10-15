package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleInterpreterTests {

    void testInterpreter(String source, String expectedOutput) {
        BasicParser parser = new BasicParser(new StringReader(source));
        ProgramNode prog;
        try {
            prog = parser.program();
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytesOut, true);
        InterpreterState state = new InterpreterState(out);
        prog.run(state);
        assertEquals(expectedOutput, bytesOut.toString());
    }

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
        testInterpreter("10 PRINT 23", "23\n");
    }

    @Test
    public void testExpression() {
        testInterpreter("10 PRINT 3+4", "7\n");
    }

    @Test
    public void testAssignments() {
        testInterpreter("""
                10 AA=23
                20 BB=42
                30 CC=AA+BB
                40 PRINT CC
                """, "65\n");
    }

    @Test
    public void testGoto() {
        testInterpreter("""
                10 PRINT "Hello."
                20 GOTO 40
                30 PRINT "You will not see this."
                40 PRINT "Good bye."
                """, """
                Hello.
                Good bye.
                """);
    }

    @Test
    public void testGotoBackAndForth() {
        testInterpreter("""
                10 GOTO 40
                20 PRINT "Two."
                30 GOTO 60
                40 PRINT "One."
                50 GOTO 20
                60 PRINT "Three."
                """, """
                One.
                Two.
                Three.
                """);
    }

}
