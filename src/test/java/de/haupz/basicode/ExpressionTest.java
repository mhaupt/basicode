package de.haupz.basicode;

import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExpressionTest {

    void testExpression(String expression, Object expectedResult, Class<?> expectedClass) {
        BasicParser parser = new BasicParser(new StringReader(expression));
        ExpressionNode expr;
        try {
            expr = parser.expression();
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytesOut, true);
        InterpreterState state = new InterpreterState(out);
        Object actualResult = expr.eval(state);
        assertEquals(expectedClass, actualResult.getClass());
        assertEquals(expectedResult, actualResult);
    }

}
