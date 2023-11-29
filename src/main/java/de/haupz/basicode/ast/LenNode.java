package de.haupz.basicode.ast;

import java.util.Optional;

public class LenNode extends WrappingExpressionNode {

    public LenNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof String s) {
            return Optional.of(Double.valueOf(s.length()));
        }
        return Optional.empty();
    }

}
