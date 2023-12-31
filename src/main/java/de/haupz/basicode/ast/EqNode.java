package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * Equality comparison ({@code =}) for numbers or strings.
 */
public class EqNode extends TypeAdjustingNode {

    public EqNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    Optional<Object> evalWithTypes(Object value1, Object value2) {
        if (value1 instanceof Double d && value2 instanceof Double e) {
            return Optional.of(d.equals(e) ? -1.0 : 0.0);
        }
        if (value1 instanceof String s && value2 instanceof String t) {
            return Optional.of(s.equals(t) ? -1.0 : 0.0);
        }
        return Optional.empty();
    }

}
