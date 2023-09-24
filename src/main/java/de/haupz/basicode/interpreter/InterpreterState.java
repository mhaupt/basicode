package de.haupz.basicode.interpreter;

import java.io.PrintStream;

public class InterpreterState {

    private final PrintStream out;

    public InterpreterState(PrintStream out) {
        this.out = out;
    }

    public PrintStream getOutput() {
        return out;
    }

}
