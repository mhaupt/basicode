package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public abstract class BasicNode {

    public abstract void run(InterpreterState state);

    public abstract Object eval(InterpreterState state);

}
