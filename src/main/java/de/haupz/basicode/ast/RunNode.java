package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code RUN}. Delete variables and call stack, reset the data pointer, and restart the program.
 */
public class RunNode extends StatementNode {

    public RunNode(int startPosition) {
        super(startPosition);
    }

    @Override
    public void run(InterpreterState state) {
        state.clearVars();
        state.clearCallStack();
        state.resetDataPtr();
        state.setNextStatement(0);
    }

}
