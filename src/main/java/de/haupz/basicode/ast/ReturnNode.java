package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class ReturnNode extends StatementNode {

    public ReturnNode() {}

    @Override
    public void run(InterpreterState state) {
        throw new IllegalStateException("not yet implemented");
    }

}
