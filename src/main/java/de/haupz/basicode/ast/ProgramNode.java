package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class ProgramNode extends BasicNode {

    private final List<LineNode> lines;

    private int lineIndex = 0;

    LineNode currentLine;

    public ProgramNode(List<LineNode> lines) {
        this.lines = List.copyOf(lines);
    }

    @Override
    public void run(InterpreterState state) {
        currentLine = lines.get(lineIndex);
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
        for (int idx = 0; idx < lines.size(); ++idx) {
            if (state.getJumpTarget() == lines.get(idx).getLineNumber()) {
                lineIndex = idx;
                currentLine = lines.get(idx);
                state.jumpDone();
                return;
            }
        }
        throw new IllegalStateException("line not found: " + state.getJumpTarget());
    }

    private void advanceLine(InterpreterState state) {
        ++lineIndex;
        if (lineIndex < lines.size()) {
            currentLine = lines.get(lineIndex);
        } else {
            state.terminate();
        }
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a ProgramNode should not be evaluated");
    }

}
