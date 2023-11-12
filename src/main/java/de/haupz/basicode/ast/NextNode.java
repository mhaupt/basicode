package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

public class NextNode extends StatementNode {

    private final String id;

    public NextNode(String id) {
        this.id = id;
    }

    @Override
    public void run(InterpreterState state) {
        if (!state.isRunningLoop(id)) {
            throw new IllegalStateException("no loop with " + id);
        }
        Number it = (Number) state.getVar(id).get();
        InterpreterState.For frec = state.getLoop(id);
        boolean shouldEnd = false;
        if (frec.isIntIterator()) {
            int step = frec.step().intValue();
            int nextit = it.intValue() + step;
            int end = frec.end().intValue();
            shouldEnd = step >= 0 ? nextit > end : nextit < end;
            state.setVar(id, nextit);
        } else {
            double step = frec.step().doubleValue();
            double nextit = it.doubleValue() + step;
            double end = frec.end().doubleValue();
            shouldEnd = step>= 0 ? nextit > end : nextit < end;
            state.setVar(id, nextit);
        }
        if (shouldEnd) {
            state.stopLoop(id);
        } else {
            state.setBackedgeTarget(frec.startIndex());
            state.requestBackedge();
        }
    }

}
