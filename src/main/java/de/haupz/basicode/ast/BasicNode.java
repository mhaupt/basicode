package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * <p>The root class of all AST nodes used by the BASICODE interpreter.</p>
 *
 * <p>Each AST node either represents a statement (yielding no value) or an expression (yielding a value). Depending on
 * the nature of a node, subclasses will override the {@link #run(InterpreterState)} or {@link #eval(InterpreterState)}
 * method.</p>
 */
public abstract class BasicNode {

    /**
     * Execute a statement.
     *
     * @param state the interpreter state.
     */
    public abstract void run(InterpreterState state);

    /**
     * Evaluate an expression and return its result.
     *
     * @param state the interpreter state.
     * @return the value of the expression, given the interpreter state.
     */
    public abstract Object eval(InterpreterState state);

}
