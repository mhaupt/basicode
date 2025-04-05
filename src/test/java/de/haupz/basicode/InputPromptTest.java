package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class InputPromptTest extends InterpreterTest {

    @Test
    public void testPrompt() {
        testInterpreter("""
                1000 INPUT "Your name";N$
                1010 PRINT "Hello, ";N$;"!"
                """, """
                world
                """,
                // Note that the BasicInput subclass used for testing does not mirror input to output, so that the
                // output jumps right to the result.
                """
                Your name? Hello, world!
                """);
    }

}
