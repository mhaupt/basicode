package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code FOR ... TO ... STEP}. This node implements BASICODE's {@code FOR} loop heads. It is constructed from four
 * different elements: the name of the iterator variable, an expression to initialise the iterator, an expression that
 * yields the end value, and an expression that yields the step width. Each of these expressions is evaluated exactly
 * once, i.e., when this node is first evaluated and the loop is not yet running. The node is also the jump target of
 * the loop's backedge, and will be executed as such, but ensures that the initialisation of the three aforementioned
 * state parts is not repeated while the loop is running.
 */
public class ForNode extends StatementNode {

    /**
     * The name of the loop iterator variable.
     */
    private final String id;

    /**
     * An expression used to initialise the loop iterator.
     */
    private final ExpressionNode init;

    /**
     * An expression to determine the end value of the loop iterator.
     */
    private final ExpressionNode end;

    /**
     * An expression to determine the loop iterator's step width.
     */
    private final ExpressionNode step;

    public ForNode(int startPosition, String id, ExpressionNode init, ExpressionNode end, ExpressionNode step) {
        super(startPosition);
        this.id = id;
        this.init = init;
        this.end = end;
        this.step = step == null ? new DoubleNode(1.0) : step;
    }

    public String getId() {
        return id;
    }

    public ExpressionNode getInit() {
        return init;
    }

    public ExpressionNode getEnd() {
        return end;
    }

    public ExpressionNode getStep() {
        return step;
    }

    @Override
    public void run(InterpreterState state) {
        if (!state.isRunningLoop(id)) {
            if (id.endsWith("$")) {
                throw new IllegalStateException("FOR loop cannot iterate over a string: " + id);
            }
            Number i = ensureNumber(init, state);
            Number e = ensureNumber(end, state);
            Number s = ensureNumber(step, state);
            state.setVar(id, i);
            state.startLoop(id, e, s);
        }
    }

    private Number ensureNumber(ExpressionNode expr, InterpreterState state) {
        Object value = expr.eval(state);
        if (value instanceof Number n) {
            return n;
        } else {
            throw new IllegalStateException("FOR init is not a number: " + value);
        }
    }

}
