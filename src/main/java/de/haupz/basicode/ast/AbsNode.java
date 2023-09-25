package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class AbsNode extends ExpressionNode {

    private final ExpressionNode expression;

    public AbsNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof Integer) {
            return Math.abs((int) value);
        }
        if (value instanceof Double) {
            return Math.abs((double) value);
        }
        throw new IllegalStateException("unexpected expression type " + value.getClass().getName());
    }

}
