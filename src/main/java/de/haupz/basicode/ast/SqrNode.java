package de.haupz.basicode.ast;

import java.util.Optional;

public class SqrNode extends WrappingExpressionNode {

    public SqrNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            if (i < 0) {
                throw new ArithmeticException();
            }
            return Optional.of(Math.sqrt(i));
        }
        if (value instanceof Double d) {
            if (d < 0) {
                throw new ArithmeticException();
            }
            return Optional.of(Math.sqrt(d));
        }
        return Optional.empty();
    }

}
