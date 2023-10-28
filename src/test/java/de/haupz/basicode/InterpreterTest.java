package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public abstract class InterpreterTest {

    ByteArrayOutputStream bytesOut;

    private InterpreterState state;

    void setUpState(ProgramNode prog) {
        bytesOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytesOut, true);
        state = new InterpreterState(prog, out);
    }

    private ProgramNode buildProgram(String source) {
        BasicParser parser = new BasicParser(new StringReader(source));
        try {
            return parser.program();
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    void testInterpreter(String source, String expectedOutput) {
        ProgramNode prog = buildProgram(source);
        setUpState(prog);
        prog.run(state);
        assertEquals(expectedOutput, bytesOut.toString());
    }

    void testInterpreterThrows(String source, Class<? extends Throwable> exceptionClass) {
        ProgramNode prog = buildProgram(source);
        setUpState(prog);
        assertThrows(exceptionClass, () -> prog.run(state));
    }

}
