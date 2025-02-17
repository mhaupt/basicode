package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

/**
 * <p>A superclass for expressions with a single argument that must have a certain type. This applies to many
 * mathematical functions.</p>
 *
 * <p>The node, when executed, will evaluate its single argument, and wrap execution of the actual functionality in a
 * call to {@link #evalWithTypes(Object)}. If that call returns {@link Optional#empty()}, an exception will be
 * thrown.</p>
 */
public abstract class WrappingExpressionNode extends ExpressionNode {

    private final ExpressionNode expression;

    public WrappingExpressionNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value = expression.eval(state);
        return evalWithTypes(value).
                orElseThrow(() -> new IllegalStateException("unexpected expression type " + value.getClass().getName() +
                        " in " + getClass().getSimpleName()));
    }

    /**
     * Perform the actual evaluation of the expression.
     *
     * @param value the argument the expression should be applied to.
     * @return an actual result if the argument type is as expected, or {@link Optional#empty()} if it isn't.
     */
    abstract Optional<Object> evalWithTypes(Object value);

}
