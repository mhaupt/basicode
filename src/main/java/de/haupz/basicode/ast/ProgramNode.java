package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.*;
import java.util.stream.Collectors;

public class ProgramNode extends BasicNode {

    private final List<LineNode> lines;

    private List<StatementNode> statements = new ArrayList<>();

    private Map<Integer, Integer> lineNumberToStatementIndex = new HashMap<>();

    record LineAndStatement(int line, int statement) {}

    private Map<Integer, LineAndStatement> statementIndexToLineNumberAndStatement = new HashMap<>();

    private final List<Object> dataList;

    public ProgramNode(List<LineNode> lines, List<Object> dataList) {
        this.lines = List.copyOf(lines);
        lines.forEach(line -> {
            // A new line: the index of its first statement is the current size of the statements array.
            lineNumberToStatementIndex.put(line.getLineNumber(), statements.size());
            statements.addAll(List.copyOf(line.getStatements()));
            int nStatements = statements.size();
            int nStatementsOnLine = line.getStatements().size();
            int lineNum = line.getLineNumber();
            for (int s = 0; s < nStatementsOnLine; ++s) {
                statementIndexToLineNumberAndStatement.put(
                        nStatements - nStatementsOnLine + s, new LineAndStatement(lineNum, s));
            }
        });
        this.dataList = List.copyOf(dataList);
    }

    @Override
    public void run(InterpreterState state) {
        StatementNode statement;
        while (!state.shouldEnd()) {
            statement = statements.get(state.getStatementIndex());
            try {
                statement.run(state);
            } catch (Exception e) {
                Stack<Integer> stack = state.getCallStack();
                String stackDump = "";
                LineAndStatement las = statementIndexToLineNumberAndStatement.get(state.getStatementIndex());
                stackDump = String.format("at line %d, statement %d", las.line, las.statement);
                if (!stack.isEmpty()) {
                    stackDump += stack.reversed().stream().map(stmt -> {
                        LineAndStatement sdlas = statementIndexToLineNumberAndStatement.get(stmt - 1);
                        return String.format("\nat line %d, statement %d", sdlas.line, sdlas.statement);
                    }).collect(Collectors.joining("\n"));
                }
                throw new IllegalStateException(stackDump, e);
            }
            if (state.isLineJumpNext()) {
                resolveJump(state);
            } else if (state.isReturnNext()) {
                state.setNextStatement(state.getReturnIndex());
                state.returnDone();
            } else if (state.isBackedgeNext()) {
                state.setNextStatement(state.getBackedgeTarget());
                state.backedgeDone();
            } else if (state.isSkipLine()) {
                int stmt = state.getStatementIndex();
                int lineToSkip = statementIndexToLineNumberAndStatement.get(stmt).line;
                do {
                    ++stmt;
                } while (lineToSkip == statementIndexToLineNumberAndStatement.get(stmt).line);
                state.setNextStatement(stmt);
                state.skipLineDone();
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
