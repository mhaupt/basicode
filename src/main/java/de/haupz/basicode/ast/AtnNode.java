package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code ATN}.
 */
public class AtnNode extends WrappingExpressionNode {

    public AtnNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(Math.atan(d));
        }
        return Optional.empty();
    }

}
