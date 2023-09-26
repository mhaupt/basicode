package de.haupz.basicode.ast;

import java.util.Optional;

public class IntNode extends WrappingExpressionNode {

    public IntNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            return Optional.of(i);
        }
        if (value instanceof Double d) {
            return Optional.of((int) Math.floor(d));
        }
        return Optional.empty();
    }

}
