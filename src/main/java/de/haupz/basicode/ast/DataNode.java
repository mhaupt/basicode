package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class DataNode extends StatementNode {

    private final List<Object> data;

    public DataNode(List<Object> data) {
        this.data = List.copyOf(data);
    }

    @Override
    public void run(InterpreterState state) {
        // naught
    }

}
