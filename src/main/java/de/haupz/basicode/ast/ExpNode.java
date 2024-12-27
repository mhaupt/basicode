package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code LOG}.
 */
public class ExpNode extends WrappingExpressionNode {

    public ExpNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(Math.exp(d));
        }
        return Optional.empty();
    }

}
