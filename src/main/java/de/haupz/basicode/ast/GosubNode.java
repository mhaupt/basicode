package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.subroutines.Subroutines;

/**
 * <p>{@code GOSUB}. The implementation pushes the {@linkplain InterpreterState#getStatementIndex() statement index} of
 * the return address and performs a {@linkplain InterpreterState#requestLineJump() jump} to the
 * {@linkplain InterpreterState#setLineJumpTarget(int)} target line.</p>
 *
 * <p>In case the target line number is less than 1000, a call to a {@linkplain Subroutines subroutine} is executed
 * instead of the normal {@code GOSUB}.</p>
 */
public class GosubNode extends StatementNode {

    /**
     * The target line number.
     */
    private int target;

    public GosubNode(int startPosition, int target) {
        super(startPosition);
        this.target = target;
    }

    public int getTarget() {
        return target;
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
