package de.haupz.basicode.ast;

import de.haupz.basicode.fn.Function;
import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code DEF FN}. A node that defines a {@link Function} when executed.
 */
public class DefFnNode extends StatementNode {

    /**
     * The name of the function.
     */
    private final String id;

    /**
     * The name of the function's sole argument.
     */
    private final String arg;

    /**
     * The expression constituting the function's body.
     */
    private final ExpressionNode expression;

    public DefFnNode(String id, String arg, ExpressionNode expression) {
        this.id = id.toUpperCase();
        this.arg = arg;
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public String getArg() {
        return arg;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public void run(InterpreterState state) {
        Function fn = new Function(arg, expression);
        state.setVar(id, fn);
    }

}
