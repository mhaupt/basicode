package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code END}. Flush the output and terminate execution.
 */
public class EndNode extends StatementNode {

    public EndNode() {}

    @Override
    public void run(InterpreterState state) {
        state.getOutput().flush();
        state.terminate();
    }

}
