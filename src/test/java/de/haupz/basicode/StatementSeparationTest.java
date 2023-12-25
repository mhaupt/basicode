package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class StatementSeparationTest extends InterpreterTest {

    @Test
    public void testSingleColon() {
        testInterpreter("""
                1000 PRINT "A":PRINT "B"
                """, """
                A
                B
                """);
    }

    @Test
    public void testDoubleColon() {
        testInterpreter("""
                1000 PRINT "A"::PRINT "B"
                """, """
                A
                B
                """);
    }

    @Test
    public void testManyColons() {
        testInterpreter("""
                1000 PRINT "A":::::::PRINT "B"
                """, """
                A
                B
                """);
    }

    @Test
    public void testTrailingColon() {
        testInterpreter("""
                1000 PRINT 1:PRINT 2:
                1010 PRINT 3
                """, """
                1
                2
                3
                """);
    }

}
