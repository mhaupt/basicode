package de.haupz.basicode.ast;

import java.util.Optional;

public class PowerNode extends TypeAdjustingNode {

    public PowerNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    Optional<Object> evalWithTypes(Object value1, Object value2) {
        if (value1 instanceof Double d && value2 instanceof Double e) {
            return Optional.of(Math.pow(d, e));
        }
        return Optional.empty();
    }

}
