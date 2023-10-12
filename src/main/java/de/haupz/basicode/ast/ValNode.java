package de.haupz.basicode.ast;

import java.util.Optional;

public class ValNode extends WrappingExpressionNode {

    public ValNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof String s) {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException nfe) {
                return Optional.of(Double.parseDouble(s));
            }
        }
        return Optional.empty();
    }

}