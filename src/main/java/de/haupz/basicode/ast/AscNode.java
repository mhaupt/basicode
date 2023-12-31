package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code ASC}.
 */
public class AscNode extends WrappingExpressionNode {

    public AscNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof String s) {
            return Optional.of(Double.valueOf(s.length() == 0 ? -1 : s.charAt(0)));
        }
        return Optional.empty();
    }

}
