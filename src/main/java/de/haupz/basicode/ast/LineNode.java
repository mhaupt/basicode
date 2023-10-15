package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class LineNode extends BasicNode {

    private final int lineNumber;

    private final List<StatementNode> statements;

    private int statementIndex = 0;

    private StatementNode currentStatement;

    public LineNode(int lineNumber, List<StatementNode> statements) {
        this.lineNumber = lineNumber;
        this.statements = List.copyOf(statements);
    }

    @Override
    public void run(InterpreterState state) {
        currentStatement = statements.get(statementIndex);
        while (!state.shouldEnd() && !state.isJumpNext()) {
            currentStatement.run(state);
            ++statementIndex;
            if (statementIndex < statements.size()) {
                currentStatement = statements.get(statementIndex);
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
