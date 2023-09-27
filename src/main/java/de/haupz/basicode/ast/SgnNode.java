package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

public class SgnNode extends WrappingExpressionNode {

    public SgnNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Integer i) {
            return Optional.of(Integer.signum(i));
        }
        if (value instanceof Double d) {
            return Optional.of((int) Math.signum(d));
        }
        return Optional.empty();
    }

}
