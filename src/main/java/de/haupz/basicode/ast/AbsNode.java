package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class AbsNode extends ExpressionNode {

    private final ExpressionNode expression;

    public AbsNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Object eval(InterpreterState state) {
        return Math.abs((int) expression.eval(state));
    }

}
