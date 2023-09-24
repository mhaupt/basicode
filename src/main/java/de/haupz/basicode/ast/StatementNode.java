package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public abstract class StatementNode extends BasicNode {

    @Override
    public final Object eval(InterpreterState state) {
        throw new IllegalStateException(this.getClass().getName() + " is a StatementNode and should not be evaluated");
    }

}
