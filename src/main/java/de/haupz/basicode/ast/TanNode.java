package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code TAN}.
 */
public class TanNode extends WrappingExpressionNode {

    public TanNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(Math.tan(d));
        }
        return Optional.empty();
    }

}
