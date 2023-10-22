package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class LetNode extends StatementNode {

    private String id;

    private ExpressionNode expression;

    public LetNode(String id, ExpressionNode expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public void run(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof String && !isString()) {
            throw new IllegalStateException("can't assign a string to a variable named " + id);
        }
        if (!(value instanceof String) && isString()) {
            throw new IllegalStateException("can't assign a non-string to a variable named " + id);
        }
        state.setVar(id, value);
    }

    boolean isString() {
        return id.endsWith("$");
    }

}
