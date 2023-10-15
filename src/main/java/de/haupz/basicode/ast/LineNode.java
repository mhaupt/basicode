package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class LineNode extends BasicNode {

    private final int lineNumber;

    private final List<StatementNode> statements;

    private StatementNode currentStatement;

    public LineNode(int lineNumber, List<StatementNode> statements) {
        this.lineNumber = lineNumber;
        this.statements = List.copyOf(statements);
    }

    @Override
    public void run(InterpreterState state) {
        currentStatement = statements.get(state.getStatementIndex());
        while (!state.shouldEnd() && !state.isJumpNext()) {
            currentStatement.run(state);
            state.incStatementIndex();
            if (state.getStatementIndex() < statements.size()) {
                currentStatement = statements.get(state.getStatementIndex());
            } else {
                return;
            }
        }
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a LineNode should not be evaluated");
    }

    public int getLineNumber() {
        return lineNumber;
    }

}
