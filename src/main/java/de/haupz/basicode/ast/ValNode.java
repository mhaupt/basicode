package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code VAL}.
 */
public class ValNode extends WrappingExpressionNode {

    public ValNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof String s) {
            return Optional.of(Double.parseDouble(s));
        }
        return Optional.empty();
    }

}
