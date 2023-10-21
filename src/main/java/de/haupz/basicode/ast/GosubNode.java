package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class GosubNode extends StatementNode {

    private int target;

    public GosubNode(int target) {
        this.target = target;
    }

    @Override
    public void run(InterpreterState state) {
        throw new IllegalStateException("not yet implemented");
    }

}
