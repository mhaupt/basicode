package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class OnGotoNode extends DependentJumpNode {

    public OnGotoNode(ExpressionNode expression, List<Integer> targets) {
        super(expression, targets);
    }

    @Override
    public void run(InterpreterState state) {
        int target = computeTarget(state);
        state.setLineJumpTarget(target);
        state.requestLineJump();
    }

}
