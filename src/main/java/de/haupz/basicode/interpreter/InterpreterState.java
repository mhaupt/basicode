package de.haupz.basicode.interpreter;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InterpreterState {

    private final PrintStream out;

    private final Map<String, Object> vars = new HashMap<>();

    public InterpreterState(PrintStream out) {
        this.out = out;
    }

    public PrintStream getOutput() {
        return out;
    }

    public void setVar(String id, Object value) {
        vars.put(id, value);
    }

    public Optional<Object> getVar(String id) {
        return Optional.ofNullable(vars.get(id));
    }

}
