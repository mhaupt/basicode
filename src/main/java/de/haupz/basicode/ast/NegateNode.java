package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * An expression to negate another. This is used for both negative numbers and for negating actual expressions.
 */
public class NegateNode extends WrappingExpressionNode {

    public NegateNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(-d);
        }
        return Optional.empty();
    }

}
