package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * {@code ON ... GOTO}. The semantics are largely like those of the {@link GotoNode}; the main difference is that the
 * jump target is computed using {@link DependentJumpNode#computeTarget(InterpreterState)}.
 */
public class OnGotoNode extends DependentJumpNode {

    public OnGotoNode(int startPosition, ExpressionNode expression, List<Integer> targets) {
        super(startPosition, expression, targets);
    }

    @Override
    protected StatementNode makeJumpNode(int target) {
        return new GotoNode(getStartPosition(), target);
    }

}
