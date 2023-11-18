package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class IfThenNode extends StatementNode {

    private final ExpressionNode condition;

    private final StatementNode then;

    public IfThenNode(ExpressionNode condition, StatementNode then) {
        this.condition = condition;
        this.then = then;
    }

    @Override
    public void run(InterpreterState state) {
        Object cond = condition.eval(state);
        if (cond instanceof Number n) {
            if (n.doubleValue() != 0.0) {
                then.run(state);
            } else {
                state.requestSkipLine();
            }
        } else {
            throw new IllegalStateException("IF condition must be a number: " + cond);
        }
    }

}
