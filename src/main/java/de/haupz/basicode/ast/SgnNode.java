package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code SGN}.
 */
public class SgnNode extends WrappingExpressionNode {

    public SgnNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of((int) Math.signum(d));
        }
        return Optional.empty();
    }

}
