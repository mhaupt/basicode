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

}
