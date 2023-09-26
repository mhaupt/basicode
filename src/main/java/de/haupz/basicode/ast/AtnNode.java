package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

public class AtnNode extends WrappingExpressionNode {

    public AtnNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            return Optional.of(Math.atan(i));
        }
        if (value instanceof Double d) {
            return Optional.of(Math.atan(d));
        }
        return Optional.empty();
    }

}
