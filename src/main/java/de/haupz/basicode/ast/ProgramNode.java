package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.interpreter.ProgramInfo;

import java.util.*;
import java.util.stream.Collectors;

import static de.haupz.basicode.interpreter.ProgramInfo.LineAndStatement;

/**
 * <p>The {@code ProgramNode} class represents BASICODE programs, and also implements the main interpreter logic,
 * mapping statements onto lines, and some debugging support.</p>
 *
 * <p>This node qualifies as a statement, by virtue of overriding the {@link BasicNode#run(InterpreterState)} method,
 * but stands on its own in the class hierarchy.</p>
 */
public class ProgramNode extends BasicNode {

    /**
     * The base slowdown delay. After executing a line-level statement, execution will be paused by this amount of
     * milliseconds multiplied with the {@link de.haupz.basicode.interpreter.Configuration#slowness() slowdown factor}.
     */
    public static long BASE_SLOWDOWN = 1L;

    /**
     * All lines of the BASICODE program.
     */
    private final List<LineNode> lines;

    /**
     * The flattened representation of all statements of the program, for easier execution.
     */
    private List<StatementNode> statements = new ArrayList<>();

    /**
     * Map BASIC line numbers to indices in the flattened {@link #statements} list. The statement index for any line
     * number is the index of the first statement on the respective line.
     */
    private Map<Integer, Integer> lineNumberToStatementIndex = new HashMap<>();

    /**
     * Map statement indices from the {@link #statements} list back to line numbers and statements. This is used for
     * debugging support.
     */
    private Map<Integer, LineAndStatement> statementIndexToLineNumberAndStatement = new HashMap<>();

    /**
     * The flattened list of all elements from {@code DATA} lines, in the order they appear in in the BASIC source code.
     */
    private final List<Object> dataList;

    /**
     * <p>Construct a program from a list of line nodes and data elements.</p>
     *
     * <p>Aside from initialising the {@link #lines} and {@link #dataList}, the constructor also builds the flattened
     * {@link #statements} list and populates the {@link #lineNumberToStatementIndex} and
     * {@link #statementIndexToLineNumberAndStatement} maps.</p>
     *
     * @param lines the {@link LineNode}s representing the source code.
     * @param dataList the {@code DATA} elements from the source code.
     */
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

    /**
     * <p>The main interpreter loop.</p>
     *
     * <p>Until the interpreter state is {@linkplain InterpreterState#shouldEnd() notified about termination}, the
     * interpreter fetches the {@linkplain InterpreterState#getStatementIndex() next statement} from the
     * {@link #statements} list and runs it.</p>
     *
     * <p>Statements may set specific flags in the {@linkplain InterpreterState interpreter state}. After each statement
     * execution, the interpreter checks if any of the flags is set, and handles it accordingly before resetting it
     * again. The flags are as follows:<ul>
     *     <li>{@link InterpreterState#isLineJumpNext()}: a jump to another line needs to be performed, e.g., because a
     *     {@code GOTO} statement was encountered. The interpreter will set the target statement using
     *     {@link #resolveJump(InterpreterState)}.</li>
     *     <li>{@link InterpreterState#isReturnNext()}: the interpreter needs to {@code RETURN} from a {@code GOSUB}
     *     call. It will {@linkplain InterpreterState#getReturnIndex() retrieve the return address from the call stack}
     *     and continue execution there.</li>
     *     <li>{@link InterpreterState#isBackedgeNext()}: the interpreter needs to jump from a {@code NEXT} statement to
     *     the head of the corresponding loop (a {@code FOR} statement). It will
     *     {@linkplain InterpreterState#getBackedgeTarget() retrieve the loop head's statement index} and continue
     *     execution there.</li>
     *     <li>{@link InterpreterState#isSkipLine()}: the remainder of a line needs to be skipped, and execution needs
     *     to proceed at the beginning of the next line.</li>
     * </ul>If no flag is set, the interpreter will simply move on to the next statement, if there is one. If there
     * isn't, it will {@linkplain InterpreterState#terminate() mark the interpreter state for termination}.</p>
     *
     * <p>In case the execution of a statement throws an exception, the interpreter will provide some helpful details
     * about the location in the BASIC source code where the exception originated. It will also print the BASIC call
     * stack (in case any {@code GOSUB} routines were being run). Finally, it will dump the Java call stack of the
     * interpreter itself.</p>
     *
     * @param state the interpreter state.
     */
    @Override
    public void run(InterpreterState state) {
        StatementNode statement;
        while (!state.shouldEnd()) {
            statement = statements.get(state.getStatementIndex());
            try {
                statement.run(state);
                if (state.getConfiguration().slowness() > 0) {
                    try {
                        Thread.sleep(BASE_SLOWDOWN * state.getConfiguration().slowness());
                    } catch (InterruptedException e) {
                        throw new RuntimeException("slowdown interrupted", e);
                    }
                }
            } catch (Exception e) {
                String stackDump = getStackDump(state);
                String values = getValues(state);
                throw new IllegalStateException(e.getMessage() + stackDump + "\n" + values, e);
            }

            // In case the statement execution has led to program termination, don't bother.
            if (state.shouldEnd()) {
                continue;
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
                int lineToSkip = statementIndexToLineNumberAndStatement.get(stmt).line();
                do {
                    ++stmt;
                } while (lineToSkip == statementIndexToLineNumberAndStatement.get(stmt).line());
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

    /**
     * Helper method to generate a textual representation of the BASICODE call stack for debugging purposes.
     *
     * @param state the interpreter state.
     * @return the stack dump.
     */
    private String getStackDump(InterpreterState state) {
        Stack<Integer> stack = state.getCallStack();
        String stackDump = "";
        LineAndStatement las = statementIndexToLineNumberAndStatement.get(state.getStatementIndex());
        stackDump = "\n" + stackTraceEntry(las, state.getStatementIndex());
        if (!stack.isEmpty()) {
            stackDump += '\n' + stack.reversed().stream().map(stmt -> {
                LineAndStatement sdlas = statementIndexToLineNumberAndStatement.get(stmt - 1);
                return stackTraceEntry(sdlas, stmt - 1);
            }).collect(Collectors.joining("\n"));
        }
        return stackDump;
    }

    /**
     * Helper method to generate a textual representation of the BASICODE program's variables for debugging purposes.
     *
     * @param state the interpreter state.
     * @return all variable and array values at the current state.
     */
    private String getValues(InterpreterState state) {
        StringBuilder values = new StringBuilder();
        values.append("== variables ==\n");
        state.getVarStream().forEach(
                v -> values.append(v.getKey()).append(" = ").append(v.getValue()).append('\n'));
        values.append("== arrays ==\n");
        state.getArrayStream().forEach(
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
        LineNode line = lines.stream().filter(l -> l.getLineNumber() == las.line()).findFirst().orElse(null);
        String pointer = "-".repeat(statements.get(statementIndex).getStartPosition()) +"^";
        return String.format("at line %d, statement %d\n%s\n%s", las.line(), las.statement(), line.getLineText(), pointer);
    }

    /**
     * Resolve a jump by {@linkplain #lineNumberToStatementIndex retrieving the index of the first statement} of the
     * {@linkplain InterpreterState#getLineJumpTarget() target line}, and
     * {@linkplain InterpreterState#setNextStatement(int) setting that to be the next statement to execute}.
     *
     * @param state the interpreter state.
     */
    private void resolveJump(InterpreterState state) {
        try {
            state.setNextStatement(lineNumberToStatementIndex.get(state.getLineJumpTarget()));
        } catch (NullPointerException npe) {
            throw new IllegalStateException("line not found: " + state.getLineJumpTarget());
        }
        state.lineJumpDone();
    }

    /**
     * This method is overridden to throw an exception, as programs aren't expressions.
     *
     * @param state the interpreter state.
     * @return nothing; this implementation will throw an exception.
     */
    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a ProgramNode should not be evaluated");
    }

    /**
     * @return the list of {@ode DATA} elements.
     */
    public List<Object> getDataList() {
        return dataList;
    }

}
