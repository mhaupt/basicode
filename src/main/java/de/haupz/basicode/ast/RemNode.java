package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class RemNode extends StatementNode {

    private final String rem;

    public RemNode(String rem) {
        this.rem = rem;
    }

    @Override
    public void run(InterpreterState state) {
        // naught
    }

}
