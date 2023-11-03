package de.haupz.basicode.fn;

import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

public class Function {

    private final String argName;

    private final ExpressionNode expression;

    public Function(String argName, ExpressionNode expression) {
        this.argName = argName;
        this.expression = expression;
    }

    public Object apply(InterpreterState state, Object arg) {
        Optional<Object> savedArg = state.getVar(argName);
        state.setVar(argName, arg);
        Object result = expression.eval(state);
        if (savedArg.isPresent()) {
            state.setVar(argName, savedArg.get());
        } else {
            state.removeVar(argName);
        }
        return result;
    }

}
