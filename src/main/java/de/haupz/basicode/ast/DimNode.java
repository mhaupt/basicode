package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * {@code DIM}. A single {@code DIM} statement can consist of several array initialisations. Each of these is
 * represented by an instance of {@link DimCreateNode}. This node simply references a list of these, and when run,
 * executes all of those array creations.
 */
public class DimNode extends StatementNode {

    private final List<DimCreateNode> dims;

    public DimNode(int startPosition, List<DimCreateNode> dims) {
        super(startPosition);
        this.dims = List.copyOf(dims);
    }

    public List<DimCreateNode> getDims() {
        return dims;
    }

    @Override
    public void run(InterpreterState state) {
        dims.forEach(d -> d.run(state));
    }

}
