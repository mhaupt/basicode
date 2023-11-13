package de.haupz.basicode.ast;

import java.util.Optional;

public class NeqNode extends TypeAdjustingNode {

    public NeqNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    Optional<Object> evalWithTypes(Object value1, Object value2) {
        if (value1 instanceof Double d && value2 instanceof Double e) {
            return Optional.of(d.equals(e) ? 0 : -1);
        }
        if (value1 instanceof String s && value2 instanceof String t) {
            return Optional.of(s.equals(t) ? 0 : -1);
        }
        return Optional.empty();
    }

}
