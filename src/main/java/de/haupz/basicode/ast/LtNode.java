package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * Less-than comparison ({@code <}) for numbers or strings.
 */
public class LtNode extends TypeAdjustingNode {

    public LtNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    Optional<Object> evalWithTypes(Object value1, Object value2) {
        if (value1 instanceof Double d && value2 instanceof Double e) {
            return Optional.of(d < e ? -1.0 : 0.0);
        }
        if (value1 instanceof String s && value2 instanceof String t) {
            return Optional.of(s.compareTo(t) < 0 ? -1.0 : 0.0);
        }
        return Optional.empty();
    }

}
