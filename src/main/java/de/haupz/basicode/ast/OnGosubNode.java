package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * {@code ON ... GOSUB}. The semantics are largely like those of the {@link GosubNode}; the main difference is that the
 * call target is computed using {@link DependentJumpNode#computeTarget(InterpreterState)}.
 */
public class OnGosubNode extends DependentJumpNode {

    public OnGosubNode(int startPosition, ExpressionNode expression, List<Integer> targets) {
        super(startPosition, expression, targets);
    }

    @Override
    public void run(InterpreterState state) {
        int target = computeTarget(state);
        state.pushReturnIndex();
        state.setLineJumpTarget(target);
        state.requestLineJump();
    }

}
