package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.Configuration;
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

    public static void run(String code, Configuration configuration) throws Throwable {
        System.out.println("================================================================================");
        System.out.println(code);
        System.out.println("--------------------------------------------------------------------------------");
        final var parser = new BasicParser(new StringReader(code));
        ProgramNode prog = parser.program();
        InterpreterState state = new InterpreterState(prog, bc, bc, configuration);
        bc.registerStopKeyHandler(() -> state.terminate());
        prog.run(state);
        System.out.println("================================================================================");
    }

    public static void main(String[] args) throws Throwable {
        boolean nowait = false;
        boolean nosound = false;
        boolean hold = false;
        String filename = "";
        for (String arg : args) {
            switch (arg) {
                case "-nowait" -> nowait = true;
                case "-nosound" -> nosound = true;
                case "-hold" -> hold = true;
                default -> filename = arg;
            }
        }

        Configuration configuration = new Configuration(nowait, nosound, hold);

        if (filename.isEmpty()) {
            throw new IllegalStateException("no file given");
        }
        Path path = Paths.get(filename);
        List<String> sourceLines = Files.readAllLines(path);
        String source = sourceLines.stream().collect(Collectors.joining("\n"));

        bc = new BasicContainer();
        SwingUtilities.invokeLater(() -> {
            bf = new BasicFrame(bc);
            bf.setVisible(true);
        });

        run(source, configuration);
        System.out.println("done.");
        bc.shutdown();
        bf.dispose();
    }

}