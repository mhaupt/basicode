package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code RIGHT$}.
 */
public class RightsNode extends ExpressionNode {

    /**
     * The string from which to extract a suffix.
     */
    private final ExpressionNode expression1;

    /**
     * An expression to yield the length of the suffix.
     */
    private final ExpressionNode expression2;

    public RightsNode(ExpressionNode expression1, ExpressionNode expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value1 = expression1.eval(state);
        Object value2 = expression2.eval(state);
        if (value1 instanceof String s) {
            int n;
            if (value2 instanceof Double d) {
                n = d.intValue();
            } else {
                throw new IllegalStateException("LEFT$ expects number as second argument");
            }
            if (n == 0 || s.isEmpty()) {
                return "";
            }
            return s.substring(Math.max(0, s.length() - n), s.length());
        } else {
            throw new IllegalStateException("LEFT$ expects string as first argument");
        }
    }

}
