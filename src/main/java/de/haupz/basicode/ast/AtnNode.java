package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class AtnNode extends ExpressionNode {

    private final ExpressionNode expression;

    public AtnNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof Integer) {
            return Math.atan((int) value);
        }
        if (value instanceof Double) {
            return Math.atan((double) value);
        }
        throw new IllegalStateException("unexpected expression type " + value.getClass().getName());
    }

}
