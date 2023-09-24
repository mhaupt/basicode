package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class LineNode extends BasicNode {

    private final int lineNumber;

    private final List<StatementNode> statements;

    public LineNode(int lineNumber, List<StatementNode> statements) {
        this.lineNumber = lineNumber;
        this.statements = List.copyOf(statements);
    }

    @Override
    public void run(InterpreterState state) {
        statements.forEach(st -> st.run(state));
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a LineNode should not be evaluated");
    }

}
