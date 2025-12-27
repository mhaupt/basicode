package de.haupz.basicode.interpreter;

import de.haupz.basicode.ast.ExpressionNode;

/**
 * <p>A watchpoint in a BASICODE program.</p>
 *
 * <p>A watchpoint will be triggered when its condition flips from "unmet" to "met" after the execution of a statement.
 * It is thus not bound to a particular location in source code: that's a breakpoint, which may also be conditional.</p>
 */
public class Watchpoint {

    /**
     * The unique ID of this watchpoint; numbering starts at 1.
     */
    private final int id;

    /**
     * This watchpoint's condition - if it flips from "unmet" to "met", that will trigger the watchpoint. The condition
     * is represented as a BASICODE expression.
     */
    private final ExpressionNode condition;

    /**
     * This is {@code false} while the {@link #condition condition} is unmet. As soon as the condition is met, this is
     * set to {@code true} and stays like that while the condition is met. This is to ensure the watchpoint triggers
     * only once.
     */
    private boolean triggered = false;

    public Watchpoint(int id, ExpressionNode condition) {
        this.id = id;
        this.condition = condition;
    }

    public int getId() {
        return id;
    }

    /**
     * Check whether the watchpoint should intercept execution. This is the case <em>iff</em> the condition just flipped
     * from "unmet" to "met", i.e., if the watchpoint is not yet marked as {@link #triggered triggered} and the
     * condition is now {@code true}.
     *
     * @param state the interpreter state needed to evaluate the condition.
     * @return {@code true} if execution should be intercepted.
     */
    public boolean shouldIntercept(InterpreterState state) {
        Object cond = condition.eval(state);
        boolean isConditionMet = cond instanceof Number n && n.doubleValue() != 0.0;
        if (isConditionMet) {
            if (triggered) {
                return false;
            } else {
                triggered = true;
                return true;
            }
        } else {
            triggered = false;
            return false;
        }
    }

}
