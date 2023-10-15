package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramNode extends BasicNode {

    private final List<LineNode> lines;

    LineNode currentLine;

    private Map<Integer, Integer> jumpTable = new HashMap<>();

    public ProgramNode(List<LineNode> lines) {
        this.lines = List.copyOf(lines);
        for (int i = 0; i < lines.size(); ++i) {
            jumpTable.put(lines.get(i).getLineNumber(), i);
        }
    }

    @Override
    public void run(InterpreterState state) {
        currentLine = lines.get(state.getLineIndex());
        while (!state.shouldEnd()) {
            currentLine.run(state);
            if (state.isJumpNext()) {
                resolveJump(state);
            } else {
                advanceLine(state);
            }
        }
    }

    private void resolveJump(InterpreterState state) {
        try {
            state.setNextLine(jumpTable.get(state.getJumpTarget()));
        } catch (NullPointerException npe) {
            throw new IllegalStateException("line not found: " + state.getJumpTarget());
        }
        currentLine = lines.get(state.getLineIndex());
        state.jumpDone();
    }

    private void advanceLine(InterpreterState state) {
        state.incLineIndex();
        if (state.getLineIndex() < lines.size()) {
            currentLine = lines.get(state.getLineIndex());
        } else {
            state.terminate();
        }
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a ProgramNode should not be evaluated");
    }

}
