package de.haupz.basicode.ast;

import de.haupz.basicode.fn.Function;
import de.haupz.basicode.interpreter.InterpreterState;

public class DefFnNode extends StatementNode {

    private final String id;

    private final String arg;

    private final ExpressionNode expression;

    public DefFnNode(String id, String arg, ExpressionNode expression) {
        this.id = id;
        this.arg = arg;
        this.expression = expression;
    }

    @Override
    public void run(InterpreterState state) {
        Function fn = new Function(arg, expression);
        state.setVar(id, fn);
    }

}
