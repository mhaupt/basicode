package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class ChrsNode extends ExpressionNode {

    private final ExpressionNode expression;

    public ChrsNode(ExpressionNode expression) {
        this.expression = expression;
    }

    interface O2O {
        Object apply(Object o);
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof Integer i) {
            return chrs((char) i.intValue());
        }
        if (value instanceof Double d) {
            return chrs((char) d.intValue());
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
