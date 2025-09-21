package de.haupz.basicode;

import de.haupz.basicode.ast.StatementNode;
import de.haupz.basicode.interpreter.Configuration;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.BasicInput;
import de.haupz.basicode.io.BasicOutput;
import de.haupz.basicode.io.BufferedReaderInput;
import de.haupz.basicode.io.PrintStreamOutput;
import de.haupz.basicode.parser.Parser;
import de.haupz.basicode.parser.ParserException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;

public abstract class StatementTest extends PseudoProgramTest {

    InterpreterState state;

    void setUpState(String input) {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bytesOut, true);
        BasicOutput out = new PrintStreamOutput(ps);
        BufferedReader br = new BufferedReader(new StringReader(input));
        BasicInput in = new BufferedReaderInput(br);
        state = new InterpreterState(PSEUDO_PROGRAM, null, in, out, new Configuration());
    }

    void run(String source) {
        run(List.of(source), "");
    }

    void run(List<String> sources) {
        run(sources, "");
    }

    void run(String source, String providedInput) {
        run(List.of(source), providedInput);
    }

    void run(List<String> sources, String providedInput) {
        setUpState(providedInput);
        for (String source : sources) {
            Parser parser = new Parser(new StringReader(source));
            StatementNode stmt;
            try {
                stmt = parser.statement(0);
            } catch (ParserException pe) {
                throw new RuntimeException(pe);
            }
            stmt.run(state);
        }
    }

}
