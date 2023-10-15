package de.haupz.basicode.interpreter;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InterpreterState {

    private final PrintStream out;

    private final Map<String, Object> vars = new HashMap<>();

    private int lineIndex = 0;

    private int statementIndex = 0;

    private boolean end = false;

    private boolean jump = false;

    private int jumpTarget;

    public InterpreterState(PrintStream out) {
        this.out = out;
    }

    public PrintStream getOutput() {
        return out;
    }

    public void setVar(String id, Object value) {
        vars.put(id, value);
    }

    public Optional<Object> getVar(String id) {
        return Optional.ofNullable(vars.get(id));
    }

    public void terminate() {
        end = true;
    }

    public boolean shouldEnd() {
        return end;
    }

    public boolean isJumpNext() {
        return jump;
    }

    public void requestJump() {
        jump = true;
    }

    public void jumpDone() {
        jump = false;
    }

    public int getJumpTarget() {
        return jumpTarget;
    }

    public void setJumpTarget(int target) {
        jumpTarget = target;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void incLineIndex() {
        ++lineIndex;
    }

    public void setNextLine(int nextLine) {
        lineIndex = nextLine;
    }

    public int getStatementIndex() {
        return statementIndex;
    }

    public void incStatementIndex() {
        ++statementIndex;
    }

    public void setNextStatement(int nextStmt) {
        statementIndex = nextStmt;
    }

}
