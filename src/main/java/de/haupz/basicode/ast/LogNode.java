package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code LOG}.
 */
public class LogNode extends WrappingExpressionNode {

    public LogNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            if (d <= 0) {
                throw new IllegalStateException("illegal LOG argument: " + d);
            }
            return Optional.of(Math.log(d));
        }
        return Optional.empty();
    }

}
