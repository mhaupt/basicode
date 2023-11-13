package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

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
                .orElseThrow(() -> new IllegalStateException("could not match types " + value1.getClass().getName() +
                        " and " + value2.getClass()));
    }

}
