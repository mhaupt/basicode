package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * <p>The super class of all AST nodes representing BASICODE statements. It overrides
 * {@link BasicNode#eval(InterpreterState)} to throw an exception, as this method will not be used in the entire
 * hierarchy.</p>
 *
 * <p>Statements also hold their start position on the line they occur on.</p>
 */
public abstract class StatementNode extends BasicNode {

    private final int startPosition;

    public StatementNode(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public final Object eval(InterpreterState state) {
        throw new IllegalStateException(this.getClass().getName() + " is a StatementNode and should not be evaluated");
    }

    public int getStartPosition() {
        return startPosition;
    }

}
