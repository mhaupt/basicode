package de.haupz.basicode;

import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ExpressionTest {

    InterpreterState state;

    @BeforeEach
    void setUp() {
        state = new InterpreterState(null, null, null);
    }

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

    void testExpression(String expression, Object expectedResult, Class<?> expectedClass) {
        Object actualResult = parseExpression(expression).eval(state);
        assertEquals(expectedClass, actualResult.getClass());
        if (expectedClass == Double.class) {
            assertEquals((double) expectedResult, (double) actualResult, 0.000001);
        } else {
            assertEquals(expectedResult, actualResult);
        }
    }

    void testExpressionThrows(String expression, Class<? extends Throwable> exceptionClass) {
        assertThrowsExactly(exceptionClass, () -> parseExpression(expression).eval(state));
    }

}
