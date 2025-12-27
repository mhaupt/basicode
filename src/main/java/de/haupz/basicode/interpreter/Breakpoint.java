package de.haupz.basicode.interpreter;

import de.haupz.basicode.ast.ExpressionNode;

import java.util.List;
import java.util.Optional;

/**
 * <p>A breakpoint in a BASICODE program.</p>
 *
 * <p>A breakpoint will be triggered when its execution reaches its line number and statement index (starting at 0), and
 * <em>before</em> the execution of that statement.</p>
 */
public class Breakpoint {

    /**
     * The unique ID of this watchpoint; numbering starts at 1.
     */
    private final int id;

    /**
     * The line on which this breakpoint is set.
     */
    private final int line;

    /**
     * The statement index on the line at which this breakpoint is set.
     */
    private final int statementIndex;

    /**
     * Which variable values and array contents to display when this breakpoint is triggered.
     */
    private final List<String> displayInfo;

    /**
     * Denote whether this breakpoint is currently active.
     */
    private boolean isActive = true;

    /**
     * This breakpoint's condition, if one is given.
     */
    private final Optional<ExpressionNode> condition;

    public Breakpoint(int id, int line, int statementIndex, List<String> displayInfo, Optional<ExpressionNode> condition) {
        this.id = id;
        this.line = line;
        this.statementIndex = statementIndex;
        this.displayInfo = displayInfo;
        this.condition = condition;
    }

    public int getId() {
        return id;
    }

    public int getLine() {
        return line;
    }

    public int getStatementIndex() {
        return statementIndex;
    }

    public List<String> getDisplayInfo() {
        return displayInfo;
    }

    /**
     * Check whether the breakpoint should be triggered. This is relevant only if there is a condition.
     *
     * @param state the interpreter state needed to evaluate the condition.
     * @return {@code true} if execution should be intercepted.
     */
    public boolean shouldIntercept(InterpreterState state) {
        return condition
                .map(c -> c.eval(state) instanceof Number n && n.doubleValue() != 0.0)
                .orElse(true); // no condition means the breakpoint triggers
    }

    /**
     * Set this breakpoint's activation state.
     *
     * @param active {@code true} if this breakpoint should be active; {@code false} otherwise.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

}
