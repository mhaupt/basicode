package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class DimNode extends StatementNode {

    private final List<DimCreateNode> dims;

    public DimNode(List<DimCreateNode> dims) {
        this.dims = List.copyOf(dims);
    }

    @Override
    public void run(InterpreterState state) {
        dims.forEach(d -> d.run(state));
    }

}
