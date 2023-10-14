package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class VarNode extends ExpressionNode {

    private final String id;

    public VarNode(String id) {
        this.id = id;
    }

    @Override
    public Object eval(InterpreterState state) {
        return state.getVar(id).orElseThrow(() -> new IllegalStateException("no such variable: " + id));
    }

}
