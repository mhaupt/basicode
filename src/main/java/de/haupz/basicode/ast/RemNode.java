package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code REM}. This node has no semantics; it exists merely for debugging purposes and stores, for that reason, the
 * text from the {@code REM} line in the source code.
 */
public class RemNode extends StatementNode {

    private final String rem;

    public RemNode(String rem) {
        this.rem = rem;
    }

    public String getRem() {
        return rem;
    }

    @Override
    public void run(InterpreterState state) {
        // naught
    }

}
