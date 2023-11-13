package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class ForNode extends StatementNode {

    private final String id;

    private final ExpressionNode init;

    private final ExpressionNode end;

    private final ExpressionNode step;

    public ForNode(String id, ExpressionNode init, ExpressionNode end, ExpressionNode step) {
        this.id = id;
        this.init = init;
        this.end = end;
        this.step = step == null ? new DoubleNode(1.0) : step;
    }

    @Override
    public void run(InterpreterState state) {
        if (!state.isRunningLoop(id)) {
            if (id.endsWith("$")) {
                throw new IllegalStateException("FOR loop cannot iterate over a string: " + id);
            }
            Number i = ensureNumber(init, state);
            Number e = ensureNumber(end, state);
            Number s = ensureNumber(step, state);
            state.setVar(id, i);
            state.startLoop(id, e, s);
        }
    }

    private Number ensureNumber(ExpressionNode expr, InterpreterState state) {
        Object value = expr.eval(state);
        if (value instanceof Number n) {
            return n;
        } else {
            throw new IllegalStateException("FOR init is not a number: " + value);
        }
    }

}
