package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class LeftsNode extends ExpressionNode {

    private final ExpressionNode expression1;

    private final ExpressionNode expression2;

    public LeftsNode(ExpressionNode expression1, ExpressionNode expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value1 = expression1.eval(state);
        Object value2 = expression2.eval(state);
        if (value1 instanceof String s) {
            int n;
            if (value2 instanceof Integer i) {
                n = i;
            } else if (value2 instanceof Double d) {
                n = d.intValue();
            } else {
                throw new IllegalStateException("LEFT$ expects number as second argument");
            }
            if (n == 0 || s.isEmpty()) {
                return "";
            }
            return s.substring(0, Math.min(n, s.length()));
        } else {
            throw new IllegalStateException("LEFT$ expects string as first argument");
        }
    }

}
