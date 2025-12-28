package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * A node class to represent {@code ON ... GOTO} and {@code ON ... GOSUB} statements. It provides a shared method to
 * determine the jump target from a value and list of targets. The subclasses provide factory methods for creating the
 * actual jump nodes, which the implementation of {@link #run(InterpreterState)} delegates to.
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

    /**
     * The list of jump nodes.
     */
    private final List<StatementNode> jumps;

    public DependentJumpNode(int startPosition, ExpressionNode expression, List<Integer> targets) {
        super(startPosition);
        this.expression = expression;
        this.targets = targets;
        this.jumps = targets.stream().map(this::makeJumpNode).toList();
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
     * @return the target index for the jump.
     */
    private int computeTarget(InterpreterState state) {
        Object value = expression.eval(state);
        if (value instanceof Number n) {
            int index = n.intValue();
            if (index < 1 || index > targets.size()) {
                throw new IllegalStateException(String.format("ON index %d exceeds (1,%d)", index, targets.size()));
            }
            return index - 1;
        } else {
            throw new IllegalStateException("ON statement requires a number: " + value.getClass().getName());
        }
    }

    /**
     * Create a jump node for the given target line number.
     * @param target the line number.
     * @return a jump node - either a {@link GotoNode} or {@link GosubNode}.
     */
    protected abstract StatementNode makeJumpNode(int target);

    @Override
    public void run(InterpreterState state) {
        int targetIndex = computeTarget(state);
        jumps.get(targetIndex).run(state);
    }

}
