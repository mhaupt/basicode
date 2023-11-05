package de.haupz.basicode.subroutines;

import de.haupz.basicode.interpreter.InterpreterState;

public class GotoRoutines {

    public static void goto20(InterpreterState state) {
        throw new IllegalStateException("not yet implemented");
    }

    public static void goto950(InterpreterState state) {
        state.terminate();
    }

    public static void runRoutine(int target, InterpreterState state) {
        switch (target) {
            case 20 -> goto20(state);
            case 950 -> goto950(state);
            default -> throw new IllegalStateException("undefined target");
        }
    }

}
