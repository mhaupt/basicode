package de.haupz.basicode.ui;

import de.haupz.basicode.interpreter.BreakpointHandler;
import de.haupz.basicode.interpreter.InterpreterState;

/**
 * A breakpoint handler that shows relevant information in a modal dialogue.
 */
public class DialogueBreakpointHandler implements BreakpointHandler {

    @Override
    public void breakRun(InterpreterState state, String info) {
        BreakpointDialog bpd = new BreakpointDialog(state.getFrame(), info);
        bpd.setVisible(true);
    }

}
