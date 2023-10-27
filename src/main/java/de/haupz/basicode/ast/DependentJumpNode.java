package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public abstract class DependentJumpNode extends StatementNode {

    private final ExpressionNode expression;

    private final List<Integer> targets;

    public DependentJumpNode(ExpressionNode expression, List<Integer> targets) {
        this.expression = expression;
        this.targets = targets;
    }

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
