package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

public class CosNode extends WrappingExpressionNode {

    public CosNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            return Optional.of(Math.cos(i));
        }
        if (value instanceof Double d) {
            return Optional.of(Math.cos(d));
        }
        return Optional.empty();
    }

}
