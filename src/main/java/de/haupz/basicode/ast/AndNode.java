package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code AND}.
 */
public class AndNode extends TypeAdjustingNode {

    public AndNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    Optional<Object> evalWithTypes(Object value1, Object value2) {
        if (value1 instanceof Double d && value2 instanceof Double e) {
            return Optional.of(Double.valueOf(d.intValue() & e.intValue()));
        }
        return Optional.empty();
    }

}
