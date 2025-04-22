package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.Configuration;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.Parser;
import de.haupz.basicode.ui.BasicFrame;
import de.haupz.basicode.ui.BasicContainer;
import de.haupz.basicode.ui.ErrorDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main entry point for running BASICODE programs. This class, when run, accepts a collection of
 * {@linkplain Configuration command line flags} as well as the name of a BASICODE source file, and will run that file,
 * displaying any output in the BASICODE GUI.
 */
public class Main {

    /**
     * The GUI component.
     */
    static BasicContainer bc;

    /**
     * The main frame for the GUI component.
     */
    static BasicFrame bf;

    /**
     * Whether to play the intro or not.
     */
    static boolean playIntro = false;

    /**
     * A record type to serve as a tuple for processed command line arguments.
     *
     * @param filename a file name.
     * @param config configuration parameters.
     */
    record FilenameAndConfig(String filename, Configuration config) {}

    /**
     * Parse the command line arguments (passed in from {@link #main(String[])} and return a
     * {@linkplain FilenameAndConfig tuple} of filename and interpreter configuration.
     *
     * @param args the command line arguments to parse.
     * @return a filename-and-configuration.
     */
    private static FilenameAndConfig parseArguments(String[] args) {
        boolean nowait = false;
        boolean nosound = false;
        boolean hold = false;
        boolean enforceBoundaries = false;
        boolean showMapKeys = false;
        int slowness = 0;
        String filename = "";
        for (String arg : args) {
            if (arg.matches("^-slo+w$")) {
                slowness = arg.length() - 4; // subtract -, s, l, w; keep the o's
            }
            switch (arg) {
                case "-nowait" -> nowait = true;
                case "-nosound" -> nosound = true;
                case "-hold" -> hold = true;
                case "-enforceBoundaries" -> enforceBoundaries = true;
                case "-showMapKeys" -> showMapKeys = true;
                case "-intro" -> playIntro = true;
                default -> filename = arg;
            }
        }
        return new FilenameAndConfig(filename,
                new Configuration(nowait, nosound, hold, enforceBoundaries, showMapKeys, slowness));
    }

    /**
     * Let the user pick a {@code .bas} file to run.
     *
     * @return the absolute path to the chosen file, or the empty string in case no file was chosen.
     */
    private static String chooseFile(String dirName) {
        File dir = new File(dirName);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("BASIC files", "bas");
        JFileChooser fc = new JFileChooser(dir);
        fc.setFileFilter(filter);
        int choice = fc.showOpenDialog(null);
        return choice == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile().getAbsolutePath() : "";
    }

    /**
     * Read a BASIC source file and return its contents.
     *
     * @param filename the file name (possibly an absolute path).
     * @return the contents of the file.
     * @throws IOException in case anything goes wrong with opening and reading the file.
     */
    private static String getSource(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<String> sourceLines = Files.readAllLines(path);
        String source = sourceLines.stream().collect(Collectors.joining("\n"));
        return source;
    }

    /**
     * Run a BASICODE program.
     *
     * @param code the source code to run, as a string.
     * @param configuration the configuration for the interpreter.
     * @throws Exception in case anything goes wrong.
     */
    public static void run(String code, Configuration configuration) {
        try {
            bc = new BasicContainer(configuration);
            SwingUtilities.invokeAndWait(() -> {
                bf = new BasicFrame(bc);
                bf.setVisible(true);
            });
            final Parser parser = new Parser(new StringReader(code));
            ProgramNode prog = parser.program();
            InterpreterState state = new InterpreterState(prog, bc, bc, configuration);
            bc.registerStopKeyHandler(state::terminate);
            prog.run(state);
            state.closeFiles();
            bc.shutdown();
            bf.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog ed = new ErrorDialog(bf, e);
            ed.setVisible(true);
        }
    }

    /**
     * Play the intro. This is the file {@code intro.bas} stored in the builtin resources.
     */
    private static void playIntro() throws Exception {
        String source = new String(Main.class.getResourceAsStream("/intro.bas").readAllBytes());
        Configuration introConfig = new Configuration(true, true, false, true, false, 0);
        run(source, introConfig);
    }

    public static void main(String[] args) throws Exception {
        FilenameAndConfig fnc = parseArguments(args);
        String filename = fnc.filename;
        if (filename.isEmpty()) {
            filename = chooseFile(System.getProperty("user.dir"));
        } else if (Files.isDirectory(Paths.get(filename))) {
            filename = chooseFile(filename);
        }
        if (!filename.isEmpty()) {
            if (playIntro) {
                playIntro();
            }
            String source = getSource(filename);
            run(source, fnc.config);
        }
    }

}