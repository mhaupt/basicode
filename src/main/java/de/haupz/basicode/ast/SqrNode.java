package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code SQR}.
 */
public class SqrNode extends WrappingExpressionNode {

    public SqrNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            if (d < 0) {
                throw new ArithmeticException();
            }
            return Optional.of(Math.sqrt(d));
        }
        return Optional.empty();
    }

}
