package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code RESTORE}. The node calls {@link InterpreterState#resetDataPtr()}.
 */
public class RestoreNode extends StatementNode {

    public RestoreNode(int startPosition) {
        super(startPosition);
    }

    @Override
    public void run(InterpreterState state) {
        state.resetDataPtr();
    }

}
