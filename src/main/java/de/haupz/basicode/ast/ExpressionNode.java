package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.Token;

import java.util.List;

public abstract class ExpressionNode extends BasicNode {

    @Override
    public final void run(InterpreterState state) {
        throw new IllegalStateException(this.getClass().getName() + " is a ValueNode and should not be run");
    }

}
