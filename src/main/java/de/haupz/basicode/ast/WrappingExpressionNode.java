package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

public abstract class WrappingExpressionNode extends ExpressionNode {

    private final ExpressionNode expression;

    public WrappingExpressionNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Object eval(InterpreterState state) {
        Object value = expression.eval(state);
        return evalWithTypes(value).
                orElseThrow(() -> new IllegalStateException("unexpected expression type " + value.getClass().getName()));
    }

    abstract Optional<Object> evalWithTypes(Object value);

}
