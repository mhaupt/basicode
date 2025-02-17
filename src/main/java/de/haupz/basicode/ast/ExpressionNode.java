package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * The super class of all AST nodes representing BASICODE expressions. It overrides
 * {@link BasicNode#run(InterpreterState)} to throw an exception, as this method will not be used in the entire
 * hierarchy.
 */
public abstract class ExpressionNode extends BasicNode {

    @Override
    public final void run(InterpreterState state) {
        throw new IllegalStateException(this.getClass().getName() + " is a ValueNode and should not be run");
    }

}
