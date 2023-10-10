package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class DivideNode extends ExpressionNode {

    private ExpressionNode expression1;

    private ExpressionNode expression2;

    public DivideNode(ExpressionNode expression1, ExpressionNode expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("not yet implemented");
    }

}
