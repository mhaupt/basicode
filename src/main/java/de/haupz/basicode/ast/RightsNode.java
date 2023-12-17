package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code RIGHT$}.
 */
public class RightsNode extends SubstringNode {

    public RightsNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    public Object eval(InterpreterState state) {
        String s = argToString(expression1.eval(state));
        int n = argToInt(expression2.eval(state), "second");
        if (n == 0 || s.isEmpty()) {
            return "";
        }
        return s.substring(Math.max(0, s.length() - n), s.length());
    }

}
