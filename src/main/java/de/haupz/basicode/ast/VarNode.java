package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * Retrieving a variable. This works for both ordinary variables and arrays.
 */
public class VarNode extends ExpressionNode {

    private final String id;

    private final boolean isArray;

    public VarNode(String id, boolean isArray) {
        this.id = id;
        this.isArray = isArray;
    }

    @Override
    public Object eval(InterpreterState state) {
        return isArray ?
                state.getArray(id).orElseThrow(() -> new IllegalStateException("no such array: " + id)) :
                state.getVar(id).orElseThrow(() -> new IllegalStateException("no such variable: " + id));
    }

}
