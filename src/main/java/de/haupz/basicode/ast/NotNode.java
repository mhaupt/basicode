package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code NOT}.
 */
public class NotNode extends WrappingExpressionNode {

    public NotNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(Double.valueOf(~d.intValue()));
        }
        return Optional.empty();
    }

}
