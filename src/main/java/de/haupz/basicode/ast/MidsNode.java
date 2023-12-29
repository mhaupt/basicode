package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code MID$}.
 */
public class MidsNode extends SubstringNode {

    /**
     * An expression to yield the length of the substring.
     */
    private final ExpressionNode expression3;

    public MidsNode(ExpressionNode expression1, ExpressionNode expression2, ExpressionNode expression3) {
        super(expression1, expression2);
        this.expression3 = expression3;
    }

    @Override
    public Object eval(InterpreterState state) {
        String s = argToString(expression1.eval(state));
        int x = Math.max(1, argToInt(expression2.eval(state), "second"));
        int y = expression3 == null ? s.length() - x + 1 : argToInt(expression3.eval(state), "third");
        if (y == 0 || x > s.length() || s.isEmpty()) {
            return "";
        }
        if (y < 0) {
            y = s.length();
        }
        return s.substring(x - 1, Math.min(x + y - 1, s.length()));
    }

}
