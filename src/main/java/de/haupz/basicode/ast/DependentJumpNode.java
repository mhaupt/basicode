package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * A node class to represent {@code ON ... GOTO} and {@code ON ... GOSUB} statements. It provides a shared method to
 * determine the jump target from a value and list of targets. The subclasses override
 * {@link BasicNode#run(InterpreterState)} to implement the actual jump.
 */
public abstract class DependentJumpNode extends StatementNode {

    /**
     * The expression based on which value a jump is to be executed.
     */
    private final ExpressionNode expression;

    /**
     * The list of target source code lines.
     */
    private final List<Integer> targets;

    public DependentJumpNode(ExpressionNode expression, List<Integer> targets) {
        this.expression = expression;
        this.targets = targets;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public List<Integer> getTargets() {
        return targets;
    }

    /**
     * Compute a jump target from a value derived from evaluating {@link #expression}.
     *
     * @param state the interpreter state.
     * @return the target line number, retrieved from {@link #targets}.
     */
    protected int computeTarget(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof Number n) {
            int index = n.intValue();
            if (index < 1 || index > targets.size()) {
                throw new IllegalStateException(String.format("ON index %d exceeds (1,%d)", index, targets.size()));
            }
            return targets.get(index - 1);
        } else {
            throw new IllegalStateException("ON statement requires a number: " + value.getClass().getName());
        }
    }

}
