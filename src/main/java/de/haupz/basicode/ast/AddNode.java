package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

public class AddNode extends TypeAdjustingNode {

    private ExpressionNode expression1;

    private ExpressionNode expression2;

    public AddNode(ExpressionNode expression1, ExpressionNode expression2) {
        super(expression1, expression2);
    }

    @Override
    Optional<Object> evalWithTypes(Object value1, Object value2) {
        if (value1 instanceof Integer i && value2 instanceof Integer j) {
            return Optional.of(i + j);
        }
        if (value1 instanceof Double d && value2 instanceof Double e) {
            return Optional.of(d + e);
        }
        if (value1 instanceof String s && value2 instanceof String t) {
            return Optional.of(s + t);
        }
        return Optional.empty();
    }

}