package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * {@code DATA}. When executed, this node does nothing at all; the data elements are assembled during parsing and
 * initialised when the {@link ProgramNode} is created. The node still stores the data elements from its code line for
 * debugging purposes.
 */
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
