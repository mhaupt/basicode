package de.haupz.basicode.interpreter;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.io.BasicInput;
import de.haupz.basicode.io.BasicOutput;

import java.util.*;

public class InterpreterState {

    private final ProgramNode program;

    private final BasicOutput out;

    private final BasicInput in;

    private int currentOutputColumn = 0;

    private final Map<String, Object> vars = new HashMap<>();

    private final Stack<Integer> callStack = new Stack<>();

    public record For(int startIndex, Number end, Number step) {}

    private final Map<String, For> runningForLoops = new HashMap<>();

    private int statementIndex = 0;

    private boolean end = false;

    private boolean lineJump = false;

    private boolean ret = false;

    private boolean backedge = false;

    private boolean skipLine = false;

    private int lineJumpTarget;

    private int backedgeTarget;

    private int dataPtr = 0;

    public InterpreterState(ProgramNode program, BasicInput in, BasicOutput out) {
        this.program = program;
        this.in = in;
        this.out = out;
        initialiseStandardVariables();
    }

    private void initialiseStandardVariables() {
        // colours
        BasicArray1D cc = new BasicArray1D(ArrayType.NUMBER, 2);
        cc.setAt(0, -1, 6.0); // foreground: yellow
        cc.setAt(1, -1, 1.0); // background: blue
        setVar("CC", cc);
    }

    public BasicInput getInput() {
        return in;
    }

    public BasicOutput getOutput() {
        return out;
    }

    public void setVar(String id, Object value) {
        vars.put(id, value);
    }

    public Optional<Object> getVar(String id) {
        return Optional.ofNullable(vars.get(id));
    }

    public Number getStdVar(String id) {
        return (Number) vars.get(id);
    }

    public void removeVar(String id) {
        vars.remove(id);
    }

    public void clearVars() {
        vars.clear();
    }

    public void terminate() {
        end = true;
    }

    public boolean shouldEnd() {
        return end;
    }

    public boolean isLineJumpNext() {
        return lineJump;
    }

    public void requestLineJump() {
        lineJump = true;
    }

    public void lineJumpDone() {
        lineJump = false;
    }

    public int getLineJumpTarget() {
        return lineJumpTarget;
    }

    public void requestReturn() {
        ret = true;
    }

    public void returnDone() {
        ret = false;
    }

    public boolean isReturnNext() {
        return ret;
    }

    public void setLineJumpTarget(int target) {
        lineJumpTarget = target;
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

    public void pushReturnIndex() {
        callStack.push(statementIndex + 1);
    }

    public int getReturnIndex() {
        return callStack.pop();
    }

    public void clearCallStack() {
        callStack.removeAllElements();
    }

    public Stack<Integer> getCallStack() {
        return callStack;
    }

    public boolean isRunningLoop(String id) {
        return runningForLoops.containsKey(id);
    }

    public void startLoop(String id, Number end, Number step) {
        runningForLoops.put(id, new For(statementIndex, end, step));
    }

    public void stopLoop(String id) {
        runningForLoops.remove(id);
    }

    public For getLoop(String id) {
        return runningForLoops.get(id);
    }

    public void requestBackedge() {
        backedge = true;
    }

    public void backedgeDone() {
        backedge = false;
    }

    public boolean isBackedgeNext() {
        return backedge;
    }

    public void setBackedgeTarget(int b) {
        backedgeTarget = b;
    }

    public int getBackedgeTarget() {
        return backedgeTarget;
    }

    public void requestSkipLine() {
        skipLine = true;
    }

    public boolean isSkipLine() {
        return skipLine;
    }

    public void skipLineDone() {
        skipLine = false;
    }

    public void resetDataPtr() {
        dataPtr = 0;
    }

    public Object readNextDataItem() {
        List<Object> dataList = program.getDataList();
        if (dataPtr >= dataList.size()) {
            throw new IllegalStateException(String.format("read index %d exceeds size %d", dataPtr, dataList.size()));
        }
        return dataList.get(dataPtr++);
    }

    public void resetOutputColumn() {
        currentOutputColumn = 0;
    }

    public int getCurrentOutputColumn() {
        return currentOutputColumn;
    }

    public void increaseOutputColumn(int w) {
        currentOutputColumn += w;
    }

}
