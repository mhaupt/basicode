package de.haupz.basicode.ast;

import java.util.Optional;

public class NotNode extends WrappingExpressionNode {

    public NotNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        throw new IllegalStateException("not yet implemented");
    }

}
