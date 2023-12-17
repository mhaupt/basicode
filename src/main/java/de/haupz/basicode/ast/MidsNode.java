package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code MID$}.
 */
public class MidsNode extends ExpressionNode {

    /**
     * An expression to yield the string from which to extract a substring.
     */
    private final ExpressionNode expression1;

    /**
     * An expression to yield the beginning index of the substring.
     */
    private final ExpressionNode expression2;

    /**
     * An expression to yield the length of the substring.
     */
    private final ExpressionNode expression3;

    public MidsNode(ExpressionNode expression1, ExpressionNode expression2, ExpressionNode expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    private int argToInt(Object arg, String pos) {
        if (arg instanceof Double d) {
            return d.intValue();
        } else {
            throw new IllegalStateException("RIGHT$ expects number as " + pos + " argument");
        }
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value1 = expression1.eval(state);
        Object value2 = expression2.eval(state);
        Object value3 = expression3.eval(state);
        if (value1 instanceof String s) {
            int x = Math.max(1, argToInt(value2, "second"));
            int y = argToInt(value3, "third");
            if (y == 0 || x >= s.length() || s.isEmpty()) {
                return "";
            }
            if (y < 0) {
                y = s.length();
            }
            return s.substring(x - 1, Math.min(x + y - 1, s.length()));
        } else {
            throw new IllegalStateException("LEFT$ expects string as first argument");
        }
    }

}
