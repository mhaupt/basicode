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
        boolean isString = id.endsWith("$");

        // LET initialises the variable if it's not yet present
        if (state.getVar(id).isEmpty()) {
            state.setVar(id, isString ? "" : Integer.valueOf(0));
        }

        Object value = expression.eval(state);
        if (value instanceof String && !isString) {
            throw new IllegalStateException("can't assign a string to a variable named " + id);
        }
        if (!(value instanceof String) && isString) {
            throw new IllegalStateException("can't assign a non-string to a variable named " + id);
        }
        state.setVar(id, value);
    }

}
