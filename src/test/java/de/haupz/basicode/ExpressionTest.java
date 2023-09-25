package de.haupz.basicode;

import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ExpressionTest {

    ExpressionNode parseExpression(String expression) {
        BasicParser parser = new BasicParser(new StringReader(expression));
        ExpressionNode expr;
        try {
            expr = parser.expression();
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        return expr;
    }

    InterpreterState getState() {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytesOut, true);
        return new InterpreterState(out);
    }

    void testExpression(String expression, Object expectedResult, Class<?> expectedClass) {
        Object actualResult = parseExpression(expression).eval(getState());
        assertEquals(expectedClass, actualResult.getClass());
        assertEquals(expectedResult, actualResult);
    }

    void testExpressionThrows(String expression, Class<? extends Throwable> exceptionClass) {
        assertThrowsExactly(exceptionClass, () -> parseExpression(expression).eval(getState()));
    }

}
