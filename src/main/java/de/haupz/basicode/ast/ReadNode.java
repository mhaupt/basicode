package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * <p>{@code READ}. This node can be seen as an assignment, or a list of assignments, where only the left-hand sides are
 * given, while the right-hand sides are implicit thanks to {@code DATA} lines and the
 * {@linkplain InterpreterState#dataPtr interpreter state's data pointer}.</p>
 *
 * <p>When constructed, the node will build an internal list of {@link LetNode}s to leverage its ability to represent
 * left-hand sides by virtue of {@link de.haupz.basicode.ast.LetNode.LHS}. These "know" how to perform an
 * assignment.</p>
 *
 * <p>The values for the right-hand sides of these assignments are retrieved using a singleton {@link ReadOp}, an
 * {@link ExpressionNode} that simply retrieves the next data element when executed.</p>
 */
public class ReadNode extends StatementNode {

    /**
     * An expression that simply retrieves the next {@code DATA} element when executed.
     */
    private static class ReadOp extends ExpressionNode {
        @Override
        public Object eval(InterpreterState state) {
            return state.readNextDataItem();
        }
    }

    /**
     * The singleton instance of {@link ReadOp}.
     */
    private static final ReadOp READ_OP = new ReadOp();

    /**
     * The list of internal assignments from {@code DATA} elements.
     */
    private final List<LetNode> lets;

    public ReadNode(List<LetNode.LHS> lhss) {
        lets = lhss.stream().map(lhs -> new LetNode(lhs, READ_OP)).toList();
    }

    public List<LetNode> getLets() {
        return lets;
    }

    @Override
    public void run(InterpreterState state) {
        lets.forEach(let -> let.run(state));
    }

}
