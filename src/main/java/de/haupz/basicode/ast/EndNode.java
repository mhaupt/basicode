package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class EndNode extends StatementNode {

    public EndNode() {}

    @Override
    public void run(InterpreterState state) {
        state.getOutput().flush();
        state.terminate();
    }

}
