package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class PrintNode extends StatementNode {

    private final ExpressionNode value;

    public PrintNode(ExpressionNode value) {
        this.value = value;
    }

    @Override
    public void run(InterpreterState state) {
        state.getOutput().println(value.eval(state));
    }

}
