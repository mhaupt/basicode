package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.ast.StatementNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

public abstract class StatementTest {

    InterpreterState state;

    void run(String source) {
        BasicParser parser = new BasicParser(new StringReader(source));
        StatementNode stmt;
        try {
            stmt = parser.statement();
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytesOut, true);
        state = new InterpreterState(out);
        stmt.run(state);
    }

}
