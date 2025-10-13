package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.interpreter.ProgramInfo;
import de.haupz.basicode.interpreter.StatementIterator;
import de.haupz.basicode.subroutines.Subroutines;

import java.util.*;

/**
 * <p>The {@code ProgramNode} class represents BASICODE programs, and also implements the main interpreter logic, and
 * some debugging support, with the help of {@link StatementIterator} and {@link ProgramInfo}.</p>
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
     * The flattened list of all elements from {@code DATA} lines, in the order they appear in in the BASIC source code.
     */
    private final List<Object> dataList;

    /**
     * <p>Construct a program from a list of line nodes and data elements.</p>
     *
     * @param lines the {@link LineNode}s representing the source code.
     * @param dataList the {@code DATA} elements from the source code.
     */
    public ProgramNode(List<LineNode> lines, List<Object> dataList) {
        this.lines = List.copyOf(lines);
        this.dataList = List.copyOf(dataList);
    }

    /**
     * <p>The main interpreter loop.</p>
     *
     * <p>Until the interpreter state is {@linkplain InterpreterState#shouldEnd() notified about termination}, the
     * interpreter fetches the {@linkplain StatementIterator#getNext() next statement} from the interpreter state and
     * runs it.</p>
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
            statement = state.getStatementIterator().getNext();
            try {
                statement.run(state);
                state.getProgramInfo().watchpoints().forEach(watchpoint -> {
                    Object cond = watchpoint.condition().eval(state);
                    if (cond instanceof Number n && n.doubleValue() != 0.0) {
                        Subroutines.gosub964(state);
                    }
                });
                if (state.getConfiguration().slowness() > 0) {
                    try {
                        Thread.sleep(BASE_SLOWDOWN * state.getConfiguration().slowness());
                    } catch (InterruptedException e) {
                        throw new RuntimeException("slowdown interrupted", e);
                    }
                }
            } catch (Exception e) {
                String stackDump = state.getStackDump(false);
                String values = state.getValues();
                throw new IllegalStateException(e.getMessage() + "\n" + stackDump + "\n" + values, e);
            }

            // In case the statement execution has led to program termination, don't bother.
            if (state.shouldEnd()) {
                continue;
            }

            if (state.isLineJumpNext()) {
                resolveJump(state);
            } else if (state.isReturnNext()) {
                state.getStatementIterator().setIndex(state.getReturnIndex());
                state.returnDone();
            } else if (state.isBackedgeNext()) {
                state.getStatementIterator().setIndex(state.getBackedgeTarget());
                state.backedgeDone();
            } else if (state.isSkipLine()) {
                int stmt = state.getStatementIterator().getNextIndex() - 1;
                int lineToSkip = state.getProgramInfo().locateStatement(stmt).line();
                do {
                    ++stmt;
                } while (lineToSkip == state.getProgramInfo().locateStatement(stmt).line());
                state.getStatementIterator().setIndex(stmt);
                state.skipLineDone();
            } else {
                if (!state.getStatementIterator().hasNext()) {
                    state.terminate();
                }
            }
        }
    }

    /**
     * Resolve a jump by {@linkplain ProgramInfo#getLineStartStamentIndex(int) retrieving the index of the first
     * statement} of the {@linkplain InterpreterState#getLineJumpTarget() target line}, and
     * {@linkplain de.haupz.basicode.interpreter.StatementIterator#setIndex(int) setting that} to be the next statement
     * to execute}.
     *
     * @param state the interpreter state.
     */
    private void resolveJump(InterpreterState state) {
        try {
            state.getStatementIterator().setIndex(
                    state.getProgramInfo().getLineStartStamentIndex(state.getLineJumpTarget()));
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

    /**
     * @return the lines of the program.
     */
    public List<LineNode> getLines() {
        return lines;
    }

}
