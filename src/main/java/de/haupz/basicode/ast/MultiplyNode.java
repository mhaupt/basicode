package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * Multiplication ({@code *}).
 */
public class MultiplyNode extends TypeAdjustingNode {

    public MultiplyNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    Optional<Object> evalWithTypes(Object value1, Object value2) {
        if (value1 instanceof Double d && value2 instanceof Double e) {
            return Optional.of(d * e);
        }
        return Optional.empty();
    }

}
