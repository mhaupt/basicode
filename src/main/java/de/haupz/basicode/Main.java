package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.BasicFrame;
import de.haupz.basicode.parser.BasicParser;
import de.haupz.basicode.ui.BasicContainer;

import javax.swing.*;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    static BasicContainer bc;

    static BasicFrame bf;

    public static void run(String code) throws Throwable {
        System.out.println("================================================================================");
        System.out.println(code);
        System.out.println("--------------------------------------------------------------------------------");
        final var parser = new BasicParser(new StringReader(code));
        ProgramNode prog = parser.program();
        InterpreterState state = new InterpreterState(prog, bc, bc);
        prog.run(state);
        System.out.println("================================================================================");
    }

    public static void main(String[] args) throws Throwable {
        if (args.length < 1) {
            throw new IllegalStateException("no file given");
        }
        String filename = args[0];
        Path path = Paths.get(filename);
        List<String> sourceLines = Files.readAllLines(path);
        String source = sourceLines.stream().collect(Collectors.joining("\n"));

        bc = new BasicContainer();
        SwingUtilities.invokeLater(() -> {
            bf = new BasicFrame(bc);
            bf.setVisible(true);
        });

        run(source);
        System.out.println("done.");
        bc.shutdown();
        bf.dispose();
    }

}