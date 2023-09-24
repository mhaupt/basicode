package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class IntegerNode extends ValueNode {

    private final int value;

    public IntegerNode(int value) {
        this.value = value;
    }

    @Override
    public Object eval(InterpreterState state) {
        return value;
    }

}
