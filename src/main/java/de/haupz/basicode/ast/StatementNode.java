package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * The super class of all AST nodes representing BASICODE statements. It overrides
 * {@link BasicNode#eval(InterpreterState)} to throw an exception, as this method will not be used in the entire
 * hierarchy.
 */
public abstract class StatementNode extends BasicNode {

    @Override
    public final Object eval(InterpreterState state) {
        throw new IllegalStateException(this.getClass().getName() + " is a StatementNode and should not be evaluated");
    }

}
