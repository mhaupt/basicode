package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class ReadNode extends StatementNode {

    private final List<String> ids;

    public ReadNode(List<String> ids) {
        this.ids = List.copyOf(ids);
    }

    @Override
    public void run(InterpreterState state) {
        for(String id : ids) {
            Object value = state.readNextDataItem();
            boolean expectsString = id.endsWith("$");
            if ((expectsString && value instanceof Number) || (!expectsString && value instanceof String)) {
                throw new IllegalStateException("data type mismatch: read " + value + ", expected " +
                        (expectsString ? "string" : "number"));
            }
            state.setVar(id, value);
        }
    }

}
