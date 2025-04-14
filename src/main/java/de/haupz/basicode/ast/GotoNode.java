package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.subroutines.Subroutines;

/**
 * <p>{@code GOTO}. The implementation performs a {@linkplain InterpreterState#requestLineJump() jump} to the
 * {@linkplain InterpreterState#setLineJumpTarget(int)} target line.</p>
 *
 * <p>In case the target line number is less than 1000, a jump to a {@linkplain Subroutines subroutine} is executed
 * instead of the normal {@code GOTO}.</p>
 */
public class GotoNode extends StatementNode {

    private final int target;

    public GotoNode(int startPosition, int target) {
        super(startPosition);
        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public void run(InterpreterState state) {
        if (target < 1000) {
            Subroutines.runGoto(target, state);
        } else {
            state.setLineJumpTarget(target);
            state.requestLineJump();
        }
    }

}
