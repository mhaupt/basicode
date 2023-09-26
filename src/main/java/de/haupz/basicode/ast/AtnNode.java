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
        if (value instanceof Integer i) {
            return Math.atan(i);
        }
        if (value instanceof Double d) {
            return Math.atan(d);
        }
        throw new IllegalStateException("unexpected expression type " + value.getClass().getName());
    }

}
