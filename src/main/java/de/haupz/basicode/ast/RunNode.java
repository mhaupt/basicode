package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class RunNode extends StatementNode {

    @Override
    public void run(InterpreterState state) {
        state.clearVars();
        state.clearCallStack();
        state.resetDataPtr();
        state.setNextStatement(0);
    }

}
