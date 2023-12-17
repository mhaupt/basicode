package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

/**
 * <p>A superclass for expressions with two arguments that must have certain types. This applies to many mathematical
 * operations.</p>
 *
 * <p>The node, when executed, will evaluate its arguments, and wrap execution of the actual functionality in a call to
 * {@link #evalWithTypes(Object,Object)}. If that call returns {@link Optional#empty()}, an exception will be
 * thrown.</p>
 */
public abstract class TypeAdjustingNode extends ExpressionNode {

    private final ExpressionNode expression1;

    private final ExpressionNode expression2;

    public TypeAdjustingNode(ExpressionNode expression1, ExpressionNode expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    abstract Optional<Object> evalWithTypes(Object value1, Object value2);

    @Override
    public Object eval(InterpreterState state) {
        Object value1 = expression1.eval(state);
        Object value2 = expression2.eval(state);
        return evalWithTypes(value1, value2)
                .orElseThrow(() -> new IllegalStateException(getClass().getSimpleName() + " could not match types " +
                        value1.getClass().getName() + " and " + value2.getClass()));
    }

}
