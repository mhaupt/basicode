package de.haupz.basicode.fn;

import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

/**
 * A {@code Function} instance represents a BASIC function, defined using {@code DEF FN}. It also provides means to
 * call the respective function, which is facilitated by {@link de.haupz.basicode.ast.FnCallNode} instances.
 */
public class Function {

    /**
     * The name of the argument to this function.
     */
    private final String argName;

    /**
     * The expression embodying this function.
     */
    private final ExpressionNode expression;

    /**
     * Construct a function from an argument name and expression.
     *
     * @param argName the name of the function's sole argument.
     * @param expression the AST root representing this function.
     */
    public Function(String argName, ExpressionNode expression) {
        this.argName = argName;
        this.expression = expression;
    }

    /**
     * Apply this function to a given argument.
     *
     * @param state the interpreter state.
     * @param arg the argument to the function call.
     * @return the result of applying this function to the argument.
     */
    public Object apply(InterpreterState state, Object arg) {
        Optional<Object> savedArg = state.getVar(argName);
        state.setVar(argName, arg);
        Object result = expression.eval(state);
        if (savedArg.isPresent()) {
            state.setVar(argName, savedArg.get());
        } else {
            state.removeVar(argName);
        }
        return result;
    }

}
