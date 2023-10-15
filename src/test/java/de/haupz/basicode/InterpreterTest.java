package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class InterpreterTest {

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

}
