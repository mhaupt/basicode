package de.haupz.basicode.interpreter;

public interface BreakpointHandler {

    /**
     * Execute a breakpoint. This should be called whenever the interpreter needs to interrupt execution because a
     * breakpoint or watchpoint is triggered.
     *
     * @param state the interpreter state.
     * @param info text to be displayed in some way.
     */
    void breakRun(InterpreterState state, String info);

}
