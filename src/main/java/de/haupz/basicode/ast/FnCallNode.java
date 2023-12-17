package de.haupz.basicode.ast;

import de.haupz.basicode.fn.Function;
import de.haupz.basicode.interpreter.InterpreterState;

/**
 * A function call. This works for functions defined via {@code DEF FN} (see {@link DefFnNode}).
 */
public class FnCallNode extends ExpressionNode {

    /**
     * The name of the function.
     */
    private final String id;

    /**
     * An operation to retrieve the function from the variables.
     */
    private final VarNode getFn;

    /**
     * An expression to yield the argument that's to be passed to the function.
     */
    private final ExpressionNode argument;

    public FnCallNode(String id, ExpressionNode argument) {
        this.id = id;
        getFn = new VarNode(id, false);
        this.argument = argument;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object idv = getFn.eval(state);
        if (idv instanceof Function fn) {
            Object arg = argument.eval(state);
            return fn.apply(state, arg);
        }
        throw new IllegalStateException(String.format("not a function: %s, but a %s", id, idv.getClass().getName()));
    }

}
