package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class AscNode extends ExpressionNode {

    private final ExpressionNode expression;

    public AscNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof String) {
            String s = (String) value;
            return s.length() == 0 ? -1 : (int) s.charAt(0);
        }
        throw new IllegalStateException("unexpected expression type " + value.getClass().getName());
    }

}
