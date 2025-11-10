package de.haupz.basicode.util;

import de.haupz.basicode.interpreter.BreakpointHandler;
import de.haupz.basicode.interpreter.InterpreterState;

/**
 * A breakpoint handler that simply outputs breakpoint information and then terminates execution.
 */
public class TestBreakpointHandler implements BreakpointHandler {

    @Override
    public void breakRun(InterpreterState state, String info) {
        state.getOutput().print(info);
        state.terminate();
    }

}
