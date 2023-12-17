package de.haupz.basicode.interpreter;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.io.BasicInput;
import de.haupz.basicode.io.BasicOutput;

import java.io.BufferedReader;
import java.io.PrintStream;
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
     * The currently open file output, or {@code null} if none is open.
     */
    private PrintStream currentOutFile;

    /**
     * The currently open file input, or {@code null} if none is open.
     */
    private BufferedReader currentInFile;

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

    /**
     * Set an array. This is <em>not</em> setting a value at some index in an array, but rather binding an array to a
     * variable name.
     *
     * @param id the array variable's name.
     * @param value the array.
     */
    public void setArray(String id, BasicArray value) {
        arrays.put(id, value);
    }

    /**
     * Retrieve an array, if it exists.
     *
     * @param id the array's variable name.
     * @return the array, wrapped in an {@link Optional}, if it exists; otherwise, {@code empty}.
     */
    public Optional<BasicArray> getArray(String id) {
        return Optional.ofNullable(arrays.get(id));
    }

    /**
     * Remove a single variable. Do nothing if it doesn't exist.
     *
     * @param id the variable's name.
     */
    public void removeVar(String id) {
        vars.remove(id);
    }

    /**
     * Remove all variables.
     */
    public void clearVars() {
        vars.clear();
    }

    /**
     * Note that program execution should terminate upon the next possible occasion.
     */
    public void terminate() {
        end = true;
    }

    /**
     * @return {@code true} if the interpreter has been notified to terminate.
     */
    public boolean shouldEnd() {
        return end;
    }

    /**
     * @return {@code true} if the next operation the interpreter should execute is a jump to a specific line.
     */
    public boolean isLineJumpNext() {
        return lineJump;
    }

    /**
     * Note that a jump should be executed. The target of the jump should be set using {@link #setLineJumpTarget(int)}.
     */
    public void requestLineJump() {
        lineJump = true;
    }

    /**
     * Note that a jump has been completed, and execution can proceed normally.
     */
    public void lineJumpDone() {
        lineJump = false;
    }

    /**
     * @return the target line number of a jump. This value is valid only while {@link #requestLineJump()} is
     * {@code true}.
     */
    public int getLineJumpTarget() {
        return lineJumpTarget;
    }

    /**
     * Note that a return from a {@code GOSUB} should be executed.
     */
    public void requestReturn() {
        ret = true;
    }

    /**
     * Note that a return from a {@code GOSUB} has been completed.
     */
    public void returnDone() {
        ret = false;
    }

    /**
     * @return {@code true} if the next operation the interpreter should execute is a return from a {@code GOSUB}.
     */
    public boolean isReturnNext() {
        return ret;
    }

    /**
     * Set the target line number (as given in the BASIC source code) of a jump.
     *
     * @param target the line number.
     */
    public void setLineJumpTarget(int target) {
        lineJumpTarget = target;
    }

    /**
     * @return the index of the next statement to be executed in the {@link #program}'s statement list.
     */
    public int getStatementIndex() {
        return statementIndex;
    }

    /**
     * Increment the {@link #program}'s statement index.
     */
    public void incStatementIndex() {
        ++statementIndex;
    }

    /**
     * Set the index of the next statement to be executed in the {@link #program}'s statement list.
     *
     * @param nextStmt the index of the next statement to be executed.
     */
    public void setNextStatement(int nextStmt) {
        statementIndex = nextStmt;
    }

    /**
     * Push a "return address" on the call stack. This is the next statement after a {@code GOSUB} statement, for
     * instance.
     */
    public void pushReturnIndex() {
        callStack.push(statementIndex + 1);
    }

    /**
     * @return the statement index from the top of the call stack.
     */
    public int getReturnIndex() {
        return callStack.pop();
    }

    /**
     * Clear the entire call stack.
     */
    public void clearCallStack() {
        callStack.removeAllElements();
    }

    /**
     * @return the interpreter's call stack.
     */
    public Stack<Integer> getCallStack() {
        return callStack;
    }

    /**
     * Check whether a loop using a given iterator variable is currently running.
     *
     * @param id the name of the iterator variable.
     * @return {@code true} if the variable name is an iterator variable in a running {@code FOR} loop.
     */
    public boolean isRunningLoop(String id) {
        return runningForLoops.containsKey(id);
    }

    /**
     * Note the start of a {@code FOR} loop by memorising its iterator variable, end value, and step width in a
     * {@link For} record.
     *
     * @param id the name of the iterator variable used in the loop.
     * @param end the end value of the loop's iterator variable.
     * @param step the step width by which the iterator variable is to be incremented/decremented after each iteration.
     */
    public void startLoop(String id, Number end, Number step) {
        runningForLoops.put(id, new For(statementIndex, end, step));
    }

    /**
     * Note that the loop with the given iterator variable has ended.
     *
     * @param id the name of the iterator variable.
     */
    public void stopLoop(String id) {
        runningForLoops.remove(id);
    }

    /**
     * Retrieve the {@link For} record for a running loop with the given iterator variable.
     *
     * @param id the iterator variable for which the loop information is to be retrieved.
     * @return the corresponding {@link For} record, or {@code null}, if there is no running loop with the given
     * iterator variable.
     */
    public For getLoop(String id) {
        return runningForLoops.get(id);
    }

    /**
     * Note that the interpreter should next execute a loop backedge jump.
     */
    public void requestBackedge() {
        backedge = true;
    }

    /**
     * Note that a backedge jump has just been completed.
     */
    public void backedgeDone() {
        backedge = false;
    }

    /**
     * @return {@code true} if the next operation to be performed by the interpreter should be a loop backedge jump.
     */
    public boolean isBackedgeNext() {
        return backedge;
    }

    /**
     * Note the statement index of a loop head ({@code FOR} statement), to be targeted by a loop backedge jump.
     *
     * @param b the statement index of the {@code FOR} statement to jump to.
     */
    public void setBackedgeTarget(int b) {
        backedgeTarget = b;
    }

    /**
     * @return the current backedge target (a statement index referencing a {@code FOR} statement).
     */
    public int getBackedgeTarget() {
        return backedgeTarget;
    }

    /**
     * Note that the interpreter should skip the execution of the remainder of the current line. This is relevant for
     * {@code IF} statements, for example, if the condition does not hold but the {@code THEN} branch consists of
     * multiple statements separated by colons.
     */
    public void requestSkipLine() {
        skipLine = true;
    }

    /**
     * @return {@code trye} if the next operation the interpreter should perform is to skip the remainder of the current
     * line of code.
     */
    public boolean isSkipLine() {
        return skipLine;
    }

    /**
     * Note that the interpreter has finished skipping the remainder of the current line of code.
     */
    public void skipLineDone() {
        skipLine = false;
    }

    /**
     * Reset the {@code DATA} pointer to 0, so that the next {@code READ} statement will read from the first
     * {@code DATA} element again.
     */
    public void resetDataPtr() {
        dataPtr = 0;
    }

    /**
     * @return the next {@code DATA} element to be read, and increment the {@link #dataPtr}.
     */
    public Object readNextDataItem() {
        List<Object> dataList = program.getDataList();
        if (dataPtr >= dataList.size()) {
            throw new IllegalStateException(String.format("read index %d exceeds size %d", dataPtr, dataList.size()));
        }
        return dataList.get(dataPtr++);
    }

    /**
     * @return the current output file.
     */
    public PrintStream getCurrentOutFile() {
        return currentOutFile;
    }

    /**
     * Set the current output file.
     * @param currentOutFile the output file.
     */
    public void setCurrentOutFile(PrintStream currentOutFile) {
        this.currentOutFile = currentOutFile;
    }

    /**
     * @return the current input file.
     */
    public BufferedReader getCurrentInFile() {
        return currentInFile;
    }

    /**
     * Set the current input file.
     *
     * @param currentInFile the current input file.
     */
    public void setCurrentInFile(BufferedReader currentInFile) {
        this.currentInFile = currentInFile;
    }
}
