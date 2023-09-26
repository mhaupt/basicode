package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

public class AbsNode extends WrappingExpressionNode {

    public AbsNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            return Optional.of(Math.abs(i));
        }
        if (value instanceof Double d) {
            return Optional.of(Math.abs(d));
        }
        return Optional.empty();
    }

}
