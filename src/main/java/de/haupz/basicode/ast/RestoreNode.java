package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class RestoreNode extends StatementNode {

    public RestoreNode() {
    }

    @Override
    public void run(InterpreterState state) {
        state.resetDataPtr();
    }

}
