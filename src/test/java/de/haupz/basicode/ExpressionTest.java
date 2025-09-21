package de.haupz.basicode;

import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.interpreter.Configuration;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.Parser;
import de.haupz.basicode.parser.ParserException;
import org.junit.jupiter.api.BeforeEach;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ExpressionTest extends PseudoProgramTest {

    InterpreterState state;

    @BeforeEach
    void setUp() {
        state = new InterpreterState(PSEUDO_PROGRAM, null, null, new Configuration());
    }

    ExpressionNode parseExpression(String expression) {
        Parser parser = new Parser(new StringReader(expression));
        ExpressionNode expr;
        try {
            expr = parser.expression();
        } catch (ParserException pe) {
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
