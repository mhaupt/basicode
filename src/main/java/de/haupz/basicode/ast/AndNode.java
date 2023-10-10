package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class AndNode extends ExpressionNode {

    private ExpressionNode expression1;

    private List<ExpressionNode> expressions;

    public AndNode(ExpressionNode expression1, List<ExpressionNode> expressions) {
        this.expression1 = expression1;
        this.expressions = expressions;
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("not yet implemented");
    }

}
