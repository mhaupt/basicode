package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class GotoNode extends StatementNode {

    private final int target;

    public GotoNode(int target) {
        this.target = target;
    }

    @Override
    public void run(InterpreterState state) {
        state.setJumpTarget(target);
        state.setNextStatement(0);
        state.requestJump();
    }

}
