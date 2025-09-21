package de.haupz.basicode;

import de.haupz.basicode.ast.LineNode;
import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.ast.RemNode;

import java.util.List;

public abstract class PseudoProgramTest {

    static final ProgramNode PSEUDO_PROGRAM;

    static {
        RemNode rem = new RemNode(2, "REM Test");
        LineNode line = new LineNode(1, List.of(rem), "1 REM Test");
        PSEUDO_PROGRAM = new ProgramNode(List.of(line), List.of());
    }

}
