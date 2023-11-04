package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.BufferedReaderInput;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.ui.BasicContainer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

public class Main {

    static BasicContainer bc;

    public static void run(String code) throws Throwable {
        System.out.println("================================================================================");
        System.out.println(code);
        System.out.println("--------------------------------------------------------------------------------");
        final var parser = new BasicParser(new StringReader(code));
        ProgramNode prog = parser.program();
        InterpreterState state = new InterpreterState(prog,
                new BufferedReaderInput(new BufferedReader(new InputStreamReader(System.in))),
                bc);
        prog.run(state);
        System.out.println("================================================================================");
    }

    public static void main(String[] args) throws Throwable {
        bc = new BasicContainer();
        SwingUtilities.invokeLater(() -> gui());
        run("10 PRINT \"Hello, world!\"");
    }

    public static void gui() {
        JFrame frame = new JFrame("BASICODE");
        frame.setSize(bc.getSize());
        frame.getContentPane().add(bc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}