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
        state.setVar(id, value);
    }

}
