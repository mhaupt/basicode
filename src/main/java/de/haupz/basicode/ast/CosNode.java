package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code COS}.
 */
public class CosNode extends WrappingExpressionNode {

    public CosNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(Math.cos(d));
        }
        return Optional.empty();
    }

}
