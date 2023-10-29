package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class IfThenTest extends InterpreterTest {

    @Test
    public void testIfThenLineNumber() {
        testInterpreter("""
                10 A=1
                20 IF A=1 THEN 40
                30 PRINT "not to be seen"
                40 PRINT "Hello."
                """, """
                Hello.
                """);
    }

    @Test
    public void testIfThenGoto() {
        testInterpreter("""
                10 A=1
                20 IF A=1 THEN GOTO 40
                30 PRINT "not to be seen"
                40 PRINT "Hello."
                """, """
                Hello.
                """);
    }

    @Test
    public void testIfThenNotLineNumber() {
        testInterpreter("""
                10 A=1
                20 IF A=2 THEN 40
                30 PRINT "First"
                40 PRINT "Second"
                """, """
                First
                Second
                """);
    }

    @Test
    public void testIfThenNotGoto() {
        testInterpreter("""
                10 A=1
                20 IF A=2 THEN GOTO 40
                30 PRINT "First"
                40 PRINT "Second"
                """, """
                First
                Second
                """);
    }

    @Test
    public void testIfThenMultipleStatements() {
        testInterpreter("""
                10 A=1
                20 IF A=1 THEN PRINT "First":PRINT "Second"
                30 PRINT "Third"
                """, """
                First
                Second
                Third
                """);
    }

    @Test
    public void testIfThenNotMultipleStatements() {
        testInterpreter("""
                10 A=1
                20 IF A=2 THEN PRINT "nope":PRINT "also nope"
                30 PRINT "yep"
                """, """
                yep
                """);
    }

    @Test
    public void testIfThenMultipleStatementsNotAll() {
        testInterpreter("""
                10 A=1
                20 IF A=1 THEN PRINT "yep":GOTO 30:PRINT "nope"
                30 PRINT "out"
                """, """
                yep
                out
                """);
    }

    @Test
    public void testWrongCondition() {
        testInterpreterThrows("""
                10 IF "boop" THEN PRINT "nope"
                """, IllegalStateException.class);
    }

}