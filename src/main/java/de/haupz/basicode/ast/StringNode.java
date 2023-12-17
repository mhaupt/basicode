package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * A string literal value.
 */
public class StringNode extends ValueNode {

    private final String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public Object eval(InterpreterState state) {
        return value;
    }

}
