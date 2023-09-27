package de.haupz.basicode.ast;

import java.util.Optional;

public class SinNode extends WrappingExpressionNode {

    public SinNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            return Optional.of(Math.sin(i));
        }
        if (value instanceof Double d) {
            return Optional.of(Math.sin(d));
        }
        return Optional.empty();
    }

}
