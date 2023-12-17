package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code INT}.
 */
public class IntNode extends WrappingExpressionNode {

    public IntNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(Math.floor(d));
        }
        return Optional.empty();
    }

}
