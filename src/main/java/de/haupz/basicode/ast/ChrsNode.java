package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class ChrsNode extends ExpressionNode {

    private final ExpressionNode expression;

    public ChrsNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof Integer) {
            return chrs((char) (int) value);
        }
        if (value instanceof Double) {
            return chrs((char) (double) value);
        }
        throw new IllegalStateException("unexpected expression type " + value.getClass().getName());
    }

    private String chrs(char c) {
        if (c < 0 || c > 127) {
            throw new IllegalStateException("out of range: " + (int) c);
        }
        return new String(new char[] {c});
    }

}
