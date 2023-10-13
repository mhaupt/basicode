package de.haupz.basicode.ast;

import java.util.Optional;

public class NotNode extends WrappingExpressionNode {

    public NotNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            return Optional.of(~i);
        }
        if (value instanceof Double d) {
            return Optional.of(~d.intValue());
        }
        return Optional.empty();
    }

}
