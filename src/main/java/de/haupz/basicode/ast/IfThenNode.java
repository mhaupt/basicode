package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * <p>{@code IF ... THEN ...}. The node encapsulates both the condition and the <em>first</em> statement of the
 * {@code THEN} branch.</p>
 *
 * <p>If the condition is met, the first statement of the {@code THEN} branch will be executed. After that, execution
 * will simply proceed as usual with the following statement.</p>
 *
 * <p>If the condition is not met, the remainder of the line will be skipped by
 * {@linkplain InterpreterState#requestSkipLine() signalling this to the interpreter}.</p>
 */
public class IfThenNode extends StatementNode {

    /**
     * An expression representing the {@code IF} condition.
     */
    private final ExpressionNode condition;

    /**
     * The first statement of the {@code THEN} branch.
     */
    private final StatementNode then;

    public IfThenNode(ExpressionNode condition, StatementNode then) {
        this.condition = condition;
        this.then = then;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public StatementNode getThen() {
        return then;
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
