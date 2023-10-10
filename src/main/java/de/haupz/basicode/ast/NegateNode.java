package de.haupz.basicode.ast;

import java.util.Optional;

public class NegateNode extends WrappingExpressionNode {

    public NegateNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        throw new IllegalStateException("not yet implemented");
    }

}
