package de.haupz.basicode.ast;

import de.haupz.basicode.fn.Function;
import de.haupz.basicode.interpreter.InterpreterState;

public class FnCallNode extends ExpressionNode {

    private final String id;

    private final VarNode getFn;

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
