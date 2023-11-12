package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class ReadNode extends StatementNode {

    private static class ReadOp extends ExpressionNode {
        @Override
        public Object eval(InterpreterState state) {
            return state.readNextDataItem();
        }
    }

    private static final ReadOp READ_OP = new ReadOp();

    private final List<LetNode> lets;

    public ReadNode(List<LetNode.LHS> lhss) {
        lets = lhss.stream().map(lhs -> new LetNode(lhs, READ_OP)).toList();
    }

    @Override
    public void run(InterpreterState state) {
        lets.forEach(let -> let.run(state));
    }

}
