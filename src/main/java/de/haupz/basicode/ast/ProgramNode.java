package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

public class ProgramNode extends BasicNode {

    private final List<LineNode> lines;

    public ProgramNode(List<LineNode> lines) {
        this.lines = List.copyOf(lines);
    }

    @Override
    public void run(InterpreterState state) {
        lines.forEach(l -> l.run(state));
    }

    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a ProgramNode should not be evaluated");
    }

}
