package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.subroutines.GotoRoutines;

public class GotoNode extends StatementNode {

    private final int target;

    public GotoNode(int target) {
        this.target = target;
    }

    @Override
    public void run(InterpreterState state) {
        if (target < 1000) {
            GotoRoutines.runRoutine(target, state);
        } else {
            state.setLineJumpTarget(target);
            state.requestLineJump();
        }
    }

}
