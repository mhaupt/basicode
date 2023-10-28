package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramNode extends BasicNode {

    private final List<LineNode> lines;

    private List<StatementNode> statements = new ArrayList<>();

    private Map<Integer, Integer> lineNumberToStatementIndex = new HashMap<>();

    private final List<Object> dataList;

    public ProgramNode(List<LineNode> lines, List<Object> dataList) {
        this.lines = List.copyOf(lines);
        lines.forEach(line -> {
            // A new line: the index of its first statement is the current size of the statements array.
            lineNumberToStatementIndex.put(line.getLineNumber(), statements.size());
            statements.addAll(List.copyOf(line.getStatements()));
        });
        this.dataList = List.copyOf(dataList);
    }

    @Override
    public void run(InterpreterState state) {
        StatementNode statement;
        while (!state.shouldEnd()) {
            statement = statements.get(state.getStatementIndex());
            statement.run(state);
            if (state.isLineJumpNext()) {
                resolveJump(state);
            } else if (state.isReturnNext()) {
                state.setNextStatement(state.getReturnIndex());
                state.returnDone();
            } else if (state.isBackedgeNext()) {
                state.setNextStatement(state.getBackedgeTarget());
                state.backedgeDone();
            } else {
                state.incStatementIndex();
                if (state.getStatementIndex() >= statements.size()) {
                    state.terminate();
                }
            }
        }
    }

    private void resolveJump(InterpreterState state) {
        try {
            state.setNextStatement(lineNumberToStatementIndex.get(state.getLineJumpTarget()));
        } catch (NullPointerException npe) {
            throw new IllegalStateException("line not found: " + state.getLineJumpTarget());
        }
        state.lineJumpDone();
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a ProgramNode should not be evaluated");
    }

    public List<Object> getDataList() {
        return dataList;
    }

}
