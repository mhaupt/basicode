package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.subroutines.Subroutines;

public class GosubNode extends StatementNode {

    private int target;

    public GosubNode(int target) {
        this.target = target;
    }

    @Override
    public void run(InterpreterState state) {
        state.pushReturnIndex();
        if (target < 1000) {
            Subroutines.runGosub(target, state);
        } else {
            state.setLineJumpTarget(target);
            state.requestLineJump();
        }
    }

}
