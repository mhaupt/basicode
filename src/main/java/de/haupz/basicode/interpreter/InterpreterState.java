package de.haupz.basicode.interpreter;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.ast.LineNode;
import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.io.BasicInput;
import de.haupz.basicode.io.BasicOutput;
import de.haupz.basicode.ui.BasicFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.haupz.basicode.interpreter.ProgramInfo.LineAndStatement;

/**
 * A representation of the BASIC interpreter's runtime state. This includes control flow, variables, and I/O channels.
 */
public class InterpreterState {

    /**
     * The root node of the program whose state this instance represents.
     */
    private final ProgramNode program;

    /**
     * The Swing frame for the BASICODE GUI.
     */
    private final BasicFrame frame;

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
     * The iterator for statement retrieval from a flattened list of all statements in the program.
     */
    private final StatementIterator statementIterator;

    /**
     * Additional metadata about the program.
     */
    private final ProgramInfo programInfo;

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
     * The "printer".
     */
    private PrintStream printer;

    /**
     * Create an interpreter state, and initialise BASICODE standard variables.
     *
     * @param program the program whose run this interpreter state represents.
     * @param frame the Swing frame for the BASICODE GUI.
     * @param in the input channel for the program.
     * @param out the output channel for the program.
     * @param configuration the interpreter configuration used for the execution of the program.
     */
    public InterpreterState(ProgramNode program, BasicFrame frame, BasicInput in, BasicOutput out,
                            Configuration configuration) {
        this.program = program;
        this.frame = frame;
        this.statementIterator = new StatementIterator(program.getLines());
        this.programInfo = new ProgramInfo(program.getLines());
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
     * overwritten. Note that the variable name will be converted to upper case if it isn't.
     *
     * @param id the variable's name.
     * @param value the variable's new value.
     */
    public void setVar(String id, Object value) {
        vars.put(id.toUpperCase(), value);
    }

    /**
     * Retrieve a variable, if it exists. Note that the variable name will be converted to upper case if it isn't.
     *
     * @param id the variable's name.
     * @return the variable's value wrapped in an {@link Optional}, if it exists; otherwise, {@code empty}.
     */
    public Optional<Object> getVar(String id) {
        return Optional.ofNullable(vars.get(id.toUpperCase()));
    }

    /**
     * @return a stream of all variable bindings, sorted by variable name.
     */
    public Stream<Map.Entry<String, Object>> getVarStream() {
        return vars.entrySet().stream().sorted(Map.Entry.comparingByKey());
    }

    /**
     * Set an array. This is <em>not</em> setting a value at some index in an array, but rather binding an array to a
     * variable name. Note that the array name will be converted to upper case if it isn't.
     *
     * @param id the array variable's name.
     * @param value the array.
     */
    public void setArray(String id, BasicArray value) {
        arrays.put(id.toUpperCase(), value);
    }

    /**
     * Retrieve an array, if it exists. Note that the array name will be converted to upper case if it isn't.
     *
     * @param id the array's variable name.
     * @return the array, wrapped in an {@link Optional}, if it exists; otherwise, {@code empty}.
     */
    public Optional<BasicArray> getArray(String id) {
        return Optional.ofNullable(arrays.get(id.toUpperCase()));
    }

    /**
     * @return a stream of all array bindings, sorted by array name.
     */
    public Stream<Map.Entry<String, BasicArray>> getArrayStream() {
        return arrays.entrySet().stream().sorted(Map.Entry.comparingByKey());
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
     * Push a "return address" on the call stack. This is the next statement after a {@code GOSUB} statement, for
     * instance.
     */
    public void pushReturnIndex() {
        callStack.push(statementIterator.getNextIndex());
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
        runningForLoops.put(id, new For(statementIterator.getNextIndex(), end, step));
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

    /**
     * @return the printer.
     */
    public PrintStream getPrinter() {
        return printer;
    }

    /**
     * Set the printer.
     *
     * @param printer the printer.
     */
    public void setPrinter(PrintStream printer) {
        this.printer = printer;
    }

    /**
     * Close all open files maintained by the interpreter state. This is about the {@link #currentInFile},
     * {@link #currentOutFile}, and {@link #printer}.
     */
    public void closeFiles() {
        if (null != currentInFile) {
            try {
                currentInFile.close();
            } catch (IOException e) {
                // ignore
            }
        }
        if (null != currentOutFile) {
            currentOutFile.flush();
            currentOutFile.close();
        }
        if (null != printer) {
            printer.flush();
            printer.close();
        }
    }

    /**
     * @return the iterator over the flattened list of statements in the program.
     */
    public StatementIterator getStatementIterator() {
        return statementIterator;
    }

    /**
     * @return additional metadata about the program.
     */
    public ProgramInfo getProgramInfo() {
        return programInfo;
    }

    /**
     * @return the Swing frame for the BASICODE GUI.
     */
    public BasicFrame getFrame() {
        return frame;
    }

    /**
     * Helper method to generate a textual representation of the BASICODE call stack for debugging purposes.
     *
     * @param suppressNativeFrame {@code true} if the native frame should be omitted from the stack dump, e.g., when
     *                            this is called from a {@link de.haupz.basicode.subroutines.Subroutines subroutine}.
     * @return the stack dump.
     */
    public String getStackDump(boolean suppressNativeFrame) {
        Stack<Integer> stack = getCallStack();
        String stackDump = "";
        if (!suppressNativeFrame) {
            LineAndStatement las = getProgramInfo().locateStatement(getStatementIterator().getNextIndex() - 1);
            stackDump = stackTraceEntry(las, getStatementIterator().getNextIndex() - 1);
        }
        if (!stack.isEmpty()) {
            stackDump += (!suppressNativeFrame?"\n":"") + stack.reversed().stream().map(stmt -> {
                LineAndStatement sdlas = getProgramInfo().locateStatement(stmt - 1);
                return stackTraceEntry(sdlas, stmt - 1);
            }).collect(Collectors.joining("\n"));
        }
        return stackDump;
    }

    /**
     * Helper method to generate a textual representation of the BASICODE program's variables for debugging purposes.
     *
     * @return all variable and array values at the current state.
     */
    public String getValues() {
        StringBuilder values = new StringBuilder();
        values.append("== variables ==\n");
        getVarStream().forEach(
                v -> values.append(v.getKey()).append(" = ").append(v.getValue()).append('\n'));
        values.append("== arrays ==\n");
        getArrayStream().forEach(
                a -> values.append(a.getKey()).append(" = ").append(a.getValue()).append('\n'));
        return values.toString();
    }

    /**
     * <p>Helper method to generate a textual representation of a stack trace entry for debugging purposes. This
     * consists of the following:<ul>
     *     <li>the BASICODE line number and statement index;</li>
     *     <li>the complete BASICODE source code line on which the statement from the stack trace is found;</li>
     *     <li>a pointer to the statement on the source code line.</li>
     * </ul></p>
     *
     * @param las the {@link LineAndStatement BASICODE line number and statement index} of the statement from the trace.
     * @param statementIndex the index of the traced statement in the global statement list.
     * @return a textual representation of the stack trace entry.
     */
    private String stackTraceEntry(LineAndStatement las, int statementIndex) {
        LineNode line = program.getLines().stream().
                filter(l -> l.getLineNumber() == las.line()).findFirst().orElse(null);
        String pointer = "-".repeat(getStatementIterator().peek(statementIndex).getStartPosition()) +"^";
        return String.format("at line %d, statement %d\n%s\n%s", las.line(), las.statement(), line.getLineText(), pointer);
    }

}
