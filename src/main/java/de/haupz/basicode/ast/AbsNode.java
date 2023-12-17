package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code ABS}.
 */
public class AbsNode extends WrappingExpressionNode {

    public AbsNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(Math.abs(d));
        }
        return Optional.empty();
    }

}
