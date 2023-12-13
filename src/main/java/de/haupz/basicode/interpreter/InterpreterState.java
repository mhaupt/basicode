package de.haupz.basicode.interpreter;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.io.BasicInput;
import de.haupz.basicode.io.BasicOutput;

import java.util.*;

/**
 * A representation of the BASIC interpreter's runtime state. This includes control flow, variables, and I/O channels.
 */
public class InterpreterState {

    /**
     * The root node of the program whose state this instance represents.
     */
    private final ProgramNode program;

    /**
     * The running program's output channel.
     */
    private final BasicOutput out;

    /**
     * The running program's input channel.
     */
    private final BasicInput in;

    /**
     * The interpreter configuration. This is controllable using command line flags.
     */
    private final Configuration configuration;

    /**
     * Variables used in the program. Note that variables and arrays don't share a namespace, hence the {@link #arrays}
     * field contains the latter.
     */
    private final Map<String, Object> vars = new HashMap<>();

    /**
     * Arrays used in the program. Note that variables and arrays don't share a namespace, hence the {@link #vars}
     * field contains the former.
     */
    private final Map<String, BasicArray> arrays = new HashMap<>();

    /**
     * The call stack, for {@code GOSUB}/{@code RETURN} handling.
     */
    private final Stack<Integer> callStack = new Stack<>();

    /**
     * A triple to keep track of running {@code FOR} loops.
     *
     * @param startIndex the index, into the {@link #program}'s statements list, of the {@code FOR} statement that is
     *                   the head of the loop.
     * @param end the end value of the loop iterator.
     * @param step the step by which to increase/decrease the loop iterator.
     */
    public record For(int startIndex, Number end, Number step) {}

    /**
     * Map the names of running loops' iterator variables to their {@linkplain For loop records}.
     */
    private final Map<String, For> runningForLoops = new HashMap<>();

    /**
     * The index, into the {@link #program}'s statements list, of the next statement to execute.
     */
    private int statementIndex = 0;

    /**
     * If {@code true}, notifies the interpreter that it should terminate execution.
     */
    private boolean end = false;

    /**
     * If {@code true}, notifies the interpreter that it needs to execute a jump.
     */
    private boolean lineJump = false;

    /**
     * If {@code true}, notifies the interpreter that it needs to execute a return.
     */
    private boolean ret = false;

    /**
     * If {@code true}, notifies the interpreter that it needs to execute a loop backedge.
     */
    private boolean backedge = false;

    /**
     * If {@code true}, notifies the interpreter that it needs to skip the remainder of statements on the current line
     * of code.
     */
    private boolean skipLine = false;

    /**
     * The target line of a jump the interpreter needs to execute. Used when {@link #lineJump} is {@code trye}.
     */
    private int lineJumpTarget;

    /**
     * The target of a loop backedge jump the interpreter needs to execute. Used when {@link #backedge} is {@code true}.
     */
    private int backedgeTarget;

    /**
     * The pointer into the program's {@code DATA} collection. It always contains the index the next {@code READ}
     * statement would read from.
     */
    private int dataPtr = 0;

    /**
     * Create an interpreter state, and initialise BASICODE standard variables.
     *
     * @param program the program whose run this interpreter state represents.
     * @param in the input channel for the program.
     * @param out the output channel for the program.
     * @param configuration the interpreter configuration used for the execution of the program.
     */
    public InterpreterState(ProgramNode program, BasicInput in, BasicOutput out, Configuration configuration) {
        this.program = program;
        this.in = in;
        this.out = out;
        this.configuration = configuration;
        initialiseStandardVariables();
    }

    /**
     * <p>Initialise BASICODE standard variables.</p>
     *
     * <p>The following variables are initialised:<ul>
     *     <li>{@code CC}: a numerical array containing two numbers representing the default foreground ({@code CC(0)})
     *     and background ({@code CC(1)}) colours. The foreground colour is initialised to yellow; the background
     *     colour, to blue.</li>
     *     <li>{@code CN}: a number controlling whether, in graphics mode, text will be drawn using the foreground
     *     colour. It is initialised to 0, implying that the foreground colour will be used.</li>
     * </ul></p>
     */
    private void initialiseStandardVariables() {
        // colours
        BasicArray1D cc = new BasicArray1D(ArrayType.NUMBER, 2);
        cc.setAt(0, -1, 6.0); // foreground: yellow
        cc.setAt(1, -1, 1.0); // background: blue
        setArray("CC", cc);
        // drawing text defaults to using the foreground colour
        setVar("CN", 0.0);
    }

    /**
     * @return the {@link #program}'s input channel.
     */
    public BasicInput getInput() {
        return in;
    }

    /**
     * @return the {@link #program}'s output channel.
     */
    public BasicOutput getOutput() {
        return out;
    }

    /**
     * @return the interpreter configuration.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Set a variable. If the variable does not exist, it will be created; if it exists, its previous value will be
     * overwritten.
     *
     * @param id the variable's name.
     * @param value the variable's new value.
     */
    public void setVar(String id, Object value) {
        vars.put(id, value);
    }

    /**
     * Retrieve a variable, if it exists.
     *
     * @param id the variable's name.
     * @return the variable's value wrapped in an {@link Optional}, if it exists; otherwise, {@code empty}.
     */
    public Optional<Object> getVar(String id) {
        return Optional.ofNullable(vars.get(id));
    }

    public Number getStdVar(String id) {
        return (Number) vars.get(id);
    }

    public void setArray(String id, BasicArray value) {
        arrays.put(id, value);
    }

    public Optional<BasicArray> getArray(String id) {
        return Optional.ofNullable(arrays.get(id));
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

}
