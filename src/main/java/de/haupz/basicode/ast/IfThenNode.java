package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class IfThenNode extends StatementNode {

    private final ExpressionNode condition;

    private final List<StatementNode> thenBranch;

    public IfThenNode(ExpressionNode condition, List<StatementNode> thenBranch) {
        this.condition = condition;
        this.thenBranch = List.copyOf(thenBranch);
    }

    @Override
    public void run(InterpreterState state) {
        Object cond = condition.eval(state);
        if (cond instanceof Number n) {
            if (n.doubleValue() != 0.0) {
                for (StatementNode stmt : thenBranch) {
                    stmt.run(state);
                    if (state.shouldEnd() || state.isLineJumpNext() || state.isBackedgeNext() || state.isReturnNext()) {
                        return;
                    }
                }
            }
        } else {
            throw new IllegalStateException("IF condition must be a number: " + cond);
        }
    }

}
