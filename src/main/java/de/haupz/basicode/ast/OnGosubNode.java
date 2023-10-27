package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class OnGosubNode extends DependentJumpNode {

    public OnGosubNode(ExpressionNode expression, List<Integer> targets) {
        super(expression, targets);
    }

    @Override
    public void run(InterpreterState state) {
        int target = computeTarget(state);
        state.pushReturnIndex();
        state.setJumpTarget(target);
        state.requestJump();
    }

}
