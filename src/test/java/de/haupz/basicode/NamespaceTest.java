package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class NamespaceTest extends InterpreterTest {

    @Test
    public void testSameNameVariableAndArray() {
        testInterpreter("""
                1000 GOTO 20
                1010 X=23
                1020 DIM X(10)
                1030 X(1)=42
                1040 PRINT X
                1050 PRINT X(1)
                1060 GOTO 950
                """, """
                 23\s
                 42\s
                """);
    }

    @Test
    public void testSameNameArrayAndVariable() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM X(10)
                1020 X=23
                1030 X(1)=42
                1040 PRINT X
                1050 PRINT X(1)
                1060 GOTO 950
                """, """
                 23\s
                 42\s
                """);
    }

}
