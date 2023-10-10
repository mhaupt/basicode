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
        Class<?> class1 = value1.getClass();
        Class<?> class2 = value2.getClass();
        if (!class1.equals(class2)) {
            if (class1.equals(Double.class) && class2.equals(Integer.class)) {
                value2 = ((Integer) value2).doubleValue();
            }
            if (class2.equals(Double.class) && class1.equals(Integer.class)) {
                value1 = ((Integer) value1).doubleValue();
            }
        }
        Object a = value1;
        Object b = value2;
        return evalWithTypes(value1, value2)
                .orElseThrow(() -> new IllegalStateException("could not match types " + a.getClass().getName() +
                        " and " + b.getClass()));
    }

}
