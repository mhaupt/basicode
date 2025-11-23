package de.haupz.basicode.subroutines;

import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.ConsoleConfiguration;
import de.haupz.basicode.io.GraphicsCursor;
import de.haupz.basicode.io.TextCursor;
import de.haupz.basicode.parser.Parser;
import de.haupz.basicode.ui.BreakpointDialog;
import de.haupz.basicode.ui.Sound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * <p>The {@code Subroutines} class holds all implementations of the BASICODE standard subroutines. Most of these are
 * called with {@code GOSUB} statements; some, using {@code GOTO}.</p>
 *
 * <p>Any subroutine implementation must follow a few conventions:<ul>
 *     <li>it must be declared as {@code public static};</li>
 *     <li>its return type must be {@code void};</li>
 *     <li>it must accept a single argument of type {@link InterpreterState};</li>
 *     <li>its name must begin with either {@code gosub} or {@code goto}, depending on whether it is meant to be called
 *     by {@code GOSUB} or {@code GOTO}, respectively; and this prefix must be immediately followed by a number
 *     representing the line number the aforementioned call or jump should be directed to.</li>
 * </ul></p>
 *
 * <p>During startup, as soon as the {@code Subroutines} class is initialised, all of its methods adhering to the above
 * convention will be automatically registered.</p>
 */
public class Subroutines {

    /**
     * The {@link Map} holding all subroutines. The keys are the line numbers the respective subroutines are called at.
     * The values are method handles representing the subroutine methods.
     */
    private static final Map<Integer, MethodHandle> ROUTINES = new HashMap<>();

    /**
     * The method signature for any subroutine, according to the convention described in the class comment.
     */
    private static final MethodType ROUTINE_TYPE = MethodType.methodType(void.class, InterpreterState.class);

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    /**
     * Look up a subroutine method identified by its name, and return a method handle representing it if successful.
     *
     * @param methodName the name of the subroutine method, following the convention described in the class comment.
     * @return a method handle representing the subroutine method.
     */
    private static MethodHandle lookupTarget(String methodName) {
        try {
            return LOOKUP.findStatic(Subroutines.class, methodName, ROUTINE_TYPE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Register a subroutine in the {@link Subroutines#ROUTINES ROUTINES} map. The line number, used as a key in the
     * map, will be extracted from the method name.
     *
     * @param method a subroutine method.
     */
    private static void registerRoutine(Method method) {
        String name = method.getName();
        int target = Integer.parseInt(name.substring(name.startsWith("goto")?4:5));
        ROUTINES.put(target, lookupTarget(name));
    }

    /**
     * Register all subroutine methods in the {@code Subroutines} class. The method will find all methods adhering to
     * the convention described in the class comment, and register them in the {@link Subroutines#ROUTINES ROUTINES}
     * map.
     */
    static void registerRoutines() {
        Arrays.stream(Subroutines.class.getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> m.getName().matches("(goto|gosub)\\d+"))
                .forEach(Subroutines::registerRoutine);
    }

    static {
        registerRoutines();
    }

    /**
     * Run a {@code GOTO} subroutine.
     *
     * @param target the line number for the subroutine.
     * @param state the {@link InterpreterState} to be used in the subroutine's execution.
     */
    public static void runGoto(int target, InterpreterState state) {
        MethodHandle routine = ROUTINES.get(target);
        if (routine == null) {
            throw new IllegalStateException("subroutine not implemented: goto " + target);
        }
        try {
            routine.invoke(state);
        } catch (Throwable t) {
            throw new IllegalStateException("error in goto " + target, t);
        }
    }

    /**
     * Run a {@code GOSUB} subroutine.
     *
     * @param target the line number for the subroutine.
     * @param state the {@link InterpreterState} to be used in the subroutine's execution.
     */
    public static void runGosub(int target, InterpreterState state) {
        MethodHandle routine = ROUTINES.get(target);
        if (routine == null) {
            throw new IllegalStateException("subroutine not implemented: gosub " + target);
        }
        try {
            routine.invoke(state);
            state.requestReturn();
        } catch (Throwable t) {
            throw new IllegalStateException("error in gosub " + target, t);
        }
    }

    /**
     * Random number generator used by {@link Subroutines#gosub260}.
     */
    private static final Random RND = new Random();

    /**
     * The subroutines for graphics output use this to paint lines and dots. Its width is chosen to arrive at a somewhat
     * pleasant looking line width.
     */
    private static final Stroke STROKE = new BasicStroke(3);

    /**
     * The name of the file printer output will be written to. BASICODE supports printing on an actual printer, which
     * this implementation emulates by writing to a file.
     */
    public static final String PRINTER_FILE = "BASICODE-printer.txt";

    /**
     * Retrieve a numeric "standard" variable from the interpreter state. This is a slightly unsafe method, insofar as
     * it assumes that (1) the variable exists, and (2) the variable is a number.
     *
     * @param state the interpreter state to retrieve the variable from.
     * @param id the name of the variable.
     * @return the variable's numerical value.
     */
    private static Number getStdVar(InterpreterState state, String id) {
        return (Number) state.getVar(id).get();
    }

    /**
     * <p>Set the foreground and background colours from the {@code CC} standard array stored in the
     * {@linkplain InterpreterState interpreter state}. Also, return the actual painting colour as determined by
     * {@code CN}.</p>
     *
     * <p>The variable {@code CN} can be used to control this. If it has the value 0, the foreground colour will be used
     * for drawing. If it has the value 1, the background colour will be used instead. This can be used to erase
     * something that has been drawn.</p>
     *
     * @param state the current interpreter state, at the time of execution.
     * @return the actual painting colour.
     */
    private static Color establishColours(InterpreterState state) {
        BasicArray1D cc = (BasicArray1D) state.getArray("CC").get();
        int fg = ((Double) cc.at(0, -1)).intValue();
        int bg = ((Double) cc.at(1, -1)).intValue();
        state.getOutput().setColours(fg, bg);
        int cn = getStdVar(state, "CN").intValue();
        Color c = cn == 0 ? state.getOutput().getForegroundColour() : state.getOutput().getBackgroundColour();
        return c;
    }

    /**
     * Convert a single character to upper case in the BASICODE convention, where curly brackets are lower-case, and
     * square brackets are upper-case.
     *
     * @param lowerCase a lower-case character.
     * @return an upper-case character according to the BASICODE convention.
     */
    private static char basicodeToUpperCase(char lowerCase) {
        return lowerCase == '{' ? '[' :
                lowerCase == '}' ? ']' :
                        Character.toUpperCase(lowerCase);
    }

    /**
     * <p>{@code GOTO 20}: initialise some default variables and jump to line 1010. This subroutine is, by convention, called at
     * the beginning of every BASICODE program in line 1000, and continues execution in line 1010.</p>
     *
     * <p>The subroutine initialises the following standard variables:<ul>
     *     <li>{@code HO} and {@code VE}, to 39 and 24, respectively. These initially hold the maximum horizontal and
     *     vertical coordinates in text mode, and are later used to control the cursor position in text mode.</li>
     *     <li>{@code HG} and {@code VG}, to 320 and 200. These initially hold the horizontal and vertical resolution
     *     for graphics mode.</li>
     *     <li>{@code SV}, to 15. This is used to control the volume of audio output, and may range from 0 to 15.</li>
     * </ul></p>
     *
     * @param state the interpreter state.
     */
    public static void goto20(InterpreterState state) {
        state.setVar("HO", 39.0);
        state.setVar("VE", 24.0);
        state.setVar("HG", 320.0);
        state.setVar("VG", 200.0);
        state.setVar("SV", 15.0);
        state.setLineJumpTarget(1010);
        state.requestLineJump();
    }

    /**
     * {@code GOSUB 100}: switch to text mode, and initialise the colours to the contents of the {@code CC} array.
     * {@code CC(0)} will determine the foreground colour; {@code CC(1)}, the background colour.
     *
     * @param state the interpreter state.
     */
    public static void gosub100(InterpreterState state) {
        establishColours(state);
        state.getOutput().textMode();
    }

    /**
     * {@code GOSUB 110}: in text mode, position the cursor at the coordinates passed in {@code HO} and {@code VE}.
     * Also, set the foreground and background colours as determined by the values in {@code CC(0)} and {@code CC(1)},
     * respectively.
     *
     * @param state the interpreter state.
     */
    public static void gosub110(InterpreterState state) {
        int ho = getStdVar(state, "HO").intValue();
        int ve = getStdVar(state, "VE").intValue();
        if (ho >= ConsoleConfiguration.COLUMNS) {
            ho = ConsoleConfiguration.COLUMNS - 1;
            state.setVar("HO", Double.valueOf(ho));
        }
        if (ve >= ConsoleConfiguration.LINES) {
            ve = ConsoleConfiguration.LINES - 1;
            state.setVar("VE", Double.valueOf(ve));
        }
        state.getOutput().setTextCursor(ho, ve);
        BasicArray1D cc = (BasicArray1D) state.getArray("CC").get();
        int fg = ((Double) cc.at(0, -1)).intValue();
        int bg = ((Double) cc.at(1, -1)).intValue();
        state.getOutput().setPrintColours(fg, bg);
    }

    /**
     * {@code GOSUB 120}: in text mode, store the current coordinates of the cursor in the {@code HO} and {@code VE}
     * variables.
     *
     * @param state the interpreter state.
     */
    public static void gosub120(InterpreterState state) {
        TextCursor coordinates = state.getOutput().getTextCursor();
        state.setVar("HO", Double.valueOf(coordinates.col()));
        state.setVar("VE", Double.valueOf(coordinates.row()));
    }

    /**
     * {@code GOSUB 150}: in text mode, print the string passed in {@code SR$} in reverse mode, with three spaces
     * prepended and appended to it.
     *
     * @param state the interpreter state.
     */
    public static void gosub150(InterpreterState state) {
        String sr = "   " + (String) state.getVar("SR$").get() + "   ";
        state.getOutput().printReverse(sr);
    }

    /**
     * {@code GOSUB 200}: if, at the time of the execution of this subroutine, a key is pressed, return its character
     * code in the {@code IN} variable; and the corresponding character, in {@code IN$}. This subroutine is
     * non-blocking.
     *
     * @param state the interpreter state.
     */
    public static void gosub200(InterpreterState state) {
        char input = (char) state.getInput().lastChar();
        state.setVar("IN", Double.valueOf(basicodeToUpperCase(input)));
        state.setVar("IN$", input == 0 ? "" : "" + input);
    }

    /**
     * {@code GOSUB 210}: wait for the next key to be pressed. The key's character code will be returned in the
     * {@code IN} variable; and the corresponding character, in {@code IN$}. This subroutine is blocking.
     *
     * @param state the interpreter state.
     */
    public static void gosub210(InterpreterState state) {
        char input;
        try {
            input = (char) state.getInput().readChar();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        state.setVar("IN$", "" + input);
        state.setVar("IN", Double.valueOf(basicodeToUpperCase(input)));
    }

    /**
     * {@code GOSUB 220}: in text mode, return, in the {@code IN} variable, the code of the character currently
     * displayed at the horizontal and vertical position indicated by {@code HO} and {@code VE}. For an empty position
     * (one where no character is visible), {@code IN} will contain 32 (to represent a space character).
     *
     * @param state the interpreter state.
     */
    public static void gosub220(InterpreterState state) {
        int ho = getStdVar(state, "HO").intValue();
        int ve = getStdVar(state, "VE").intValue();
        char c = ho < 0 || ho >= ConsoleConfiguration.COLUMNS || ve < 0 || ve >= ConsoleConfiguration.LINES ?
                0 :
                state.getOutput().getCharAt(ho, ve);
        state.setVar("IN", Double.valueOf(basicodeToUpperCase(c)));
    }

    /**
     * <p>{@code GOSUB 250}: beep. Play an A (440 Hz) for 250 ms at full volume.</p>
     *
     * <p><b>Implementation note:</b> This can be disabled by passing the {@code -nosound} command line argument, or by
     * using the {@link de.haupz.basicode.interpreter.Configuration#nosound nosound} interpreter configuration.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub250(InterpreterState state) {
        if (state.getConfiguration().nosound()) {
            return;
        }
        // 69 corresponds to A in MIDI.
        Sound.play(69, 250, 127);
    }

    /**
     * {@code GOSUB 260}: return, in the {@code RV} variable, a random number in the range 0 <= {@code RV} < 1.
     *
     * @param state the interpreter state.
     */
    public static void gosub260(InterpreterState state) {
        state.setVar("RV", RND.nextDouble());
    }

    /**
     * <p>{@code GOSUB 270}: return, in the {@code FR} variable, the amount of free memory in bytes.</p>
     *
     * <p><b>Implementation note:</b> The free memory returned by this implementation is that of available memory on the
     * Java heap. For expectable BASICODE usage, it will usually be a ridiculously large number, as memory can be
     * expected to be ample on machines that run this BASICODE environment.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub270(InterpreterState state) {
        state.setVar("FR", Double.valueOf(Runtime.getRuntime().freeMemory()));
    }

    /**
     * {@code GOSUB 280}: control the BASICODE interpreter's sensitivity to the escape key. Pressing the escape key will
     * terminate execution by default. If {@code FR} is 1 when this subroutine is called, the escape key will be ignored
     * instead. If {@code FR} is 0 when this subroutine is called, the interpreter will react to the escape key again.
     *
     * @param state the interpreter state.
     */
    public static void gosub280(InterpreterState state) {
        int fr = getStdVar(state, "FR").intValue();
        state.getInput().toggleAcceptStopKey(fr == 0);
    }

    /**
     * Helper method: create a string of the given length, filled with the given character.
     *
     * @param length the desired length of the string.
     * @param c the character to fill the string with.
     * @return a string of the given length, containing the given character {@code length} times.
     */
    private static String fill(int length, char c) {
        char[] a = new char[length];
        Arrays.fill(a, c);
        return new String(a);
    }

    /**
     * Helper method: create a decimal formatter string with the given amount of decimal places.
     *
     * @param decimalPlaces the desired number of decimal places.
     * @return a decimal formatter string with the number of decimal places as given in {@code decimalPlaces}.
     */
    private static String decimalFormat(int decimalPlaces) {
        return "#" + (decimalPlaces > 0 ? "." + fill(decimalPlaces, '#') : "");
    }

    /**
     * The standard format for printing decimal numbers.
     */
    private static final DecimalFormat STANDARD_DECIMAL_FORMAT =
            new DecimalFormat(decimalFormat(9), DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    /**
     * {@code GOSUB 300}: convert a number passed in the {@code SR} variable to a string returned in {@code SR$}.
     *
     * @param state the interpreter state.
     */
    public static void gosub300(InterpreterState state) {
        double sr = getStdVar(state, "SR").doubleValue();
        String str = STANDARD_DECIMAL_FORMAT.format(sr);
        state.setVar("SR$", str);
    }

    /**
     * Helper method: determine whether a number can be displayed with a given amount of decimal places in a given
     * number of characters.
     *
     * @param sr the number to be displayed.
     * @param cn the number of decimal places to display.
     * @param ct the total number of characters, including sign and decimal point, that can be used to display
     * {@code sr}.
     * @return {@code true} if the number does <em>not</em> fit in the given number of characters.
     */
    private static boolean cannotDisplay(double sr, int cn, int ct) {
        int intDigits = (int) (Math.log10(Math.abs(sr)) + 1);
        int totalDigits = intDigits + cn;
        if (totalDigits > 9) {
            return false;
        }
        int sign = sr < 0 ? 1 : 0; // negative number -> need to take the sign into account
        int decimalPoint = cn > 0 ? 1 : 0; // fractional digits expected -> need to count the decimal point
        return sign + intDigits + decimalPoint + cn > ct;
    }

    /**
     * {@code GOSUB 310}: convert a number passed in {@code SR} to a string returned in {@code SR$}, using {@code CN}
     * decimal places, and using <em>at most</em> {@code CT} characters. If the number, including sign and decimal
     * point, does not fit in that many characters, {@code SR$} will still be {@code CT} characters long, but will be
     * filled with {@code *} characters.
     *
     * @param state the interpreter state.
     */
    public static void gosub310(InterpreterState state) {
        double sr = getStdVar(state, "SR").doubleValue();
        int cn = getStdVar(state, "CN").intValue();
        int ct = getStdVar(state, "CT").intValue();
        String s;
        // Special case: if no decimal places are given, and SR is negative, and SR would then evaluate to 0 for
        // printing, we must avoid printing SR as "-0" because BASIC does not know negative zero.
        if (cn == 0 && sr < 0 && Math.ceil(sr) == 0.0) {
            sr = -sr;
        }
        if (cannotDisplay(sr, cn, ct)) {
            s = fill(ct, '*');
        } else {
            s = new DecimalFormat(decimalFormat(cn), DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(sr);
        }
        if (s.length() < ct) {
            s = fill(ct - s.length(), ' ') + s;
        }
        state.setVar("SR$", s);
    }

    /**
     * {@code GOSUB 330}: convert all lower-case characters in {@code SR$} to upper-case characters.
     *
     * @param state the interpreter state.
     */
    public static void gosub330(InterpreterState state) {
        String srs = (String) state.getVar("SR$").get();
        // {} are lower-case in the BASICODE world, and [] are upper-case
        state.setVar("SR$", srs.toUpperCase().replace('{', '[').replace('}', ']'));
    }

    /**
     * Helper method: ensure the interpreter state has a valid printer reference.
     *
     * @param state the interpreter state.
     * @return the printer.
     */
    private static PrintStream ensurePrinter(InterpreterState state) {
        PrintStream printer = state.getPrinter();
        if (null == printer) {
            try {
                printer = new PrintStream(Files.newOutputStream(Paths.get(PRINTER_FILE)));
            } catch (IOException ioe) {
                throw new IllegalStateException("could not open printer file " + PRINTER_FILE, ioe);
            }
            state.setPrinter(printer);
        }
        return printer;
    }

    /**
     * {@code GOSUB 350}: write the text contained in {@code SR$} to the printer, without a newline at the end.
     *
     * @param state the interpreter state.
     */
    public static void gosub350(InterpreterState state) {
        String sr = (String) state.getVar("SR$").get();
        PrintStream printer = ensurePrinter(state);
        printer.print(sr);
    }

    /**
     * {@code GOSUB 360}: write a newline to the printer.
     *
     * @param state the interpreter state.
     */
    public static void gosub360(InterpreterState state) {
        PrintStream printer = ensurePrinter(state);
        printer.println();
    }

    /**
     * <p>{@code GOSUB 400}: play a sound. The frequency is controlled by {@code SP}; the duration, by {@code SD}; and the
     * volume, by {@code SV}. The subroutine is blocking.</p>
     *
     * <p>The roles of the parameters are as follows:<ul>
     *     <li>{@code SP} can be in the range 0 <= {@code SP} <= 127. A value of 69 indicates a pitch of 440 Hz (tone
     *     "A"). A difference of 1 in {@code SP} maps to a half-tone in pitch.</li>
     *     <li>{@code SD} is a value in the range 1 <= {@code SD} <= 255, and is interpreted as a multiple of 0.1
     *     seconds.</li>
     *     <li>{@code SV}, with 0 <= {@code SV} <= 15, indicates the volume, where 0 is inaudible, and 15 is the
     *     loudest.</li>
     * </ul></p>
     *
     * <p><b>Implementation note:</b> no sound will be played if the {@code -nosound} command line flag is used, or if
     * the {@link de.haupz.basicode.interpreter.Configuration#nosound nosound} interpreter configuration is set.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub400(InterpreterState state) {
        if (state.getConfiguration().nosound()) {
            return;
        }
        int sp = getStdVar(state, "SP").intValue();
        double sd = getStdVar(state, "SD").doubleValue();
        int sv = getStdVar(state, "SV").intValue();
        int duration = (int) (100 * sd); // sd is in 0.1 s (100 ms)
        int volume = (int) (127/15.0 * sv); // volume is 0..127, mapping from 0..15
        Sound.play(sp, duration, volume);
    }

    /**
     * <p>{@code GOSUB 450}: wait for {@code SD} times 0.1 seconds, or until a key is pressed.</p>
     *
     * <p>Afterwards, in case a key was pressed to interrupt the wait, {@code SD} will contain the remaining number of
     * 0.1 seconds intervals, and {@code IN} and {@code IN$} will contain information about the key pressed to
     * interrupt. Otherwise, {@code SD} and {@code IN} will be {@code 0}, and {@code IN$} will be empty.</p>
     *
     * <p><b>Implementation note:</b> waiting can be entirely suppressed by passing the {@code -nowait} command line
     * flag, or by using the {@link de.haupz.basicode.interpreter.Configuration#nowait nowait} interpreter
     * configuration.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub450(InterpreterState state) {
        if (state.getConfiguration().nowait()) {
            return;
        }
        int sd = getStdVar(state, "SD").intValue();
        state.getInput().setReadyToInterrupt(true);
        try {
            while (sd > 0) {
                Thread.sleep(100L);
                sd--;
            }
        } catch (InterruptedException e) {
            // ignore
        }
        state.getInput().setReadyToInterrupt(false);
        state.setVar("SD", Double.valueOf(sd));
        char c;
        if ((c = (char) state.getInput().lastChar()) != 0) {
            state.getInput().clearInput();
            state.setVar("IN", Double.valueOf(basicodeToUpperCase(c)));
            state.setVar("IN$", "" + c);
        } else {
            state.setVar("IN", Double.valueOf(0));
            state.setVar("IN$", "");
        }
    }

    /**
     * <p>{@code GOSUB 500}: open a file for reading or writing.</p>
     *
     * <p>The file name is passed in {@code NF$}. Furthermore, the variable {@code NF} has the following meanings:<ul>
     *     <li>0, 2, 4, 6: read mode</li>
     *     <li>1, 3, 5, 7: write mode</li>
     * </ul>
     * In the original BASICODE conventions, the different numbers would refer to read and write mode on different kinds
     * of devices (e.g., cassette tape, floppy disks, microdrive). These are all irrelevant here; the fila named in
     * {@code NF$} will always be opened as a file on the host file system the BASICODE interpreter is running on.</p>
     *
     * <p>After this, the {@code IN} variable will contain an error code (0: everything OK).</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub500(InterpreterState state) {
        String fileName = (String) state.getVar("NF$").get();
        int mode = getStdVar(state, "NF").intValue();
        Path p = Paths.get(fileName);
        int errorCode = 0;
        String fileError = "error with file " + fileName + ": ";
        if (mode == 0 || mode == 2 || mode == 4 || mode == 6) {
            // read mode: file should exist
            if (null == state.getCurrentOutFile() && Files.exists(p)) {
                try {
                    BufferedReader in = Files.newBufferedReader(p);
                    state.setCurrentInFile(in);
                } catch (IOException ioe) {
                    fileError += ioe.getMessage();
                    errorCode = -1;
                }
            } else {
                fileError += null != state.getCurrentOutFile() ? "a file is already open" : "does not exist";
                errorCode = -1;
            }
        } else if (mode == 1 || mode == 3 || mode == 5 || mode == 7) {
            // write mode; simply create the file
            if (null == state.getCurrentInFile()) {
                try {
                    PrintStream out = new PrintStream(Files.newOutputStream(p));
                    state.setCurrentOutFile(out);
                } catch (IOException ioe) {
                    fileError += ioe.getMessage();
                    errorCode = -1;
                }
            } else {
                fileError += "a file is already open";
                errorCode = -1;
            }
        } else {
            fileError += "invalid mode: " + mode;
            errorCode = -1; // illegal mode
        }
        if (errorCode != 0) {
            System.err.println(fileError);
        }
        state.setVar("IN", Double.valueOf(errorCode));
    }

    /**
     * {@code GOSUB 540}: read a line from a file. Afterwards, {@code IN$} will contain the line, and {@code IN} will be
     * an error code (0: OK). After reading the last line, {@code IN} will be 1.
     *
     * @param state the interpreter state.
     */
    public static void gosub540(InterpreterState state) {
        int errorCode = 0;
        BufferedReader in = state.getCurrentInFile();
        if (null == in) {
            errorCode = -1;
        } else {
            try {
                String s = in.readLine();
                if (null == s) {
                    errorCode = 1;
                    state.setVar("IN$", "");
                } else {
                    if (!in.ready()) {
                        errorCode = 1;
                    }
                    state.setVar("IN$", s);
                }
            } catch (IOException e) {
                state.setVar("IN$", "");
                errorCode = e instanceof EOFException ? 1 : -1;
            }
        }
        state.setVar("IN", Double.valueOf(errorCode));
    }

    /**
     * {@code GOSUB 560}: write to a file. The string contained in {@code SR$} will be written to the currently open
     * output file, and afterwards, {@code IN} will contain an error code (0: OK).
     *
     * @param state the interpreter state.
     */
    public static void gosub560(InterpreterState state) {
        String sr = (String) state.getVar("SR$").get();
        int errorCode = 0;
        PrintStream out = state.getCurrentOutFile();
        if (null == out) {
            errorCode = -1;
        } else {
            out.println(sr);
        }
        state.setVar("IN", Double.valueOf(errorCode));
    }

    /**
     * {@code GOSUB 580}: close the currently open file.
     *
     * @param state the interpreter state.
     */
    public static void gosub580(InterpreterState state) {
        int errorCode = 0;
        // By convention, there can only be one open file at any given time. The subroutine for GOSUB 500 above ensures
        // this, so that we can safely ignore the mode.
        BufferedReader in = state.getCurrentInFile();
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                errorCode = -1;
            }
            state.setCurrentInFile(null);
        }
        PrintStream out = state.getCurrentOutFile();
        if (null != out) {
            out.flush();
            out.close();
            state.setCurrentOutFile(null);
        }
        state.setVar("IN", Double.valueOf(errorCode));
    }

    /**
     * {@code GOSUB 600}: switch to graphics mode, and clear the screen, filling it with the background colour as set in
     * {@code CC{1}}. The internal graphics cursor is initialised to (0,0), which is the upper left corner of the view.
     *
     * @param state the interpreter state.
     */
    public static void gosub600(InterpreterState state) {
        establishColours(state);
        state.getOutput().graphicsMode();
        state.setVar("HG", 320.0);
        state.setVar("VG", 200.0);
        state.getOutput().setGraphicsCursor(0.0, 0.0);
    }

    /**
     * Helper method: check whether a number {@code v} is in the expected range for graphics operations, i.e., 0 <=
     * {@code v} <= 1, and throw an exception if the number is outside that range.
     *
     * @param name the name of the number, for debugging purposes.
     * @param v the number to check.
     */
    private static void checkBoundaries(String name, double v) {
        if (v < 0.0 || v > 1.0) {
            throw new IllegalStateException("out of bounds: " + name + " = " + v);
        }
    }

    /**
     * Helper method: Convert a fractional graphics mode coordinate to a pixel coordinate on a given axis.
     *
     * @param coordinate the fractional coordinate, in the range 0 <= {@code coordinate} <= 1.
     * @param resolution the resolution (number of pixels) of the axis.
     * @return the pixel coordinate corresponding to the fractional coordinate.
     */
    private static int pixelCoordinate(double coordinate, int resolution) {
        return (int) Math.round(coordinate * (resolution - 1));
    }

    /**
     * {@code GOSUB 620}: in graphics mode, paint a dot at the horizontal and vertical position indicated by {@code HO}
     * and {@code VE}. Both must be in the {@linkplain Subroutines#checkBoundaries(String, double) correct range} for
     * graphics operations. The dot's colour will be the one set in {@code CC(0)}.
     *
     * @param state the interpreter state.
     */
    public static void gosub620(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = getStdVar(state, "HO").doubleValue();
        double ve = getStdVar(state, "VE").doubleValue();
        if (state.getConfiguration().enforceBoundaries()) {
            checkBoundaries("HO", ho);
            checkBoundaries("VE", ve);
        }
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        Color c = establishColours(state);
        g2.setPaint(c);
        g2.setStroke(STROKE);
        int h = pixelCoordinate(ho, im.getWidth());
        int v = pixelCoordinate(ve, im.getHeight());
        g2.drawLine(h, v, h, v);
        state.getOutput().setGraphicsCursor(ho, ve);
        state.getOutput().flush();
    }

    /**
     * {@code GOSUB 630}: in graphics mode, draw a line from the current graphics cursor position to the horizontal and
     * vertical position indicated by the {@code HO} and {@code VE} variables. Both must be in the
     * {@linkplain Subroutines#checkBoundaries(String, double) correct range} for graphics operations. The line's colour
     * will be the one set in {@code CC(0)}. The internal graphics cursor will be updated to the position indicated by
     * {@code HO} and {@code VE}.
     *
     * @param state the interpreter state.
     */
    public static void gosub630(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = getStdVar(state, "HO").doubleValue();
        double ve = getStdVar(state, "VE").doubleValue();
        GraphicsCursor gc = state.getOutput().getGraphicsCursor();
        if (state.getConfiguration().enforceBoundaries()) {
            checkBoundaries("HO", ho);
            checkBoundaries("VE", ve);
        }
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        Color c = establishColours(state);
        g2.setPaint(c);
        g2.setStroke(STROKE);
        int h = pixelCoordinate(gc.h(), im.getWidth());
        int v = pixelCoordinate(gc.v(), im.getHeight());
        int nh = pixelCoordinate(ho, im.getWidth());
        int nv = pixelCoordinate(ve, im.getHeight());
        g2.drawLine(h, v, nh, nv);
        state.getOutput().setGraphicsCursor(ho, ve);
        state.getOutput().flush();
    }

    /**
     * {@code GOSUB 650}: in graphics mode, draw a text string passed in {@code SR$} at the horizontal and vertical
     * position indicated by the {@code HO} and {@code VE} variables. This position is the top left corner of the text
     * area. Both variables must be in the {@linkplain Subroutines#checkBoundaries(String, double) correct range} for
     * graphics operations. The text's colour will be the one set in {@code CC(0)}. Afterwards, the internal graphics
     * cursor will be positioned at the coordinates indicated by {@code HO} and {@code VE}.
     *
     * @param state the interpreter state.
     */
    public static void gosub650(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = getStdVar(state, "HO").doubleValue();
        double ve = getStdVar(state, "VE").doubleValue();
        if (state.getConfiguration().enforceBoundaries()) {
            checkBoundaries("HO", ho);
            checkBoundaries("VE", ve);
        }
        String sr = (String) state.getVar("SR$").get();
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        Color c = establishColours(state);
        g2.setPaint(c);
        g2.setFont(state.getOutput().getFont());
        FontMetrics fm = g2.getFontMetrics();
        int h = pixelCoordinate(ho, im.getWidth());
        int v = pixelCoordinate(ve, im.getHeight());
        g2.drawString(sr, h, fm.getHeight() + v);
        state.getOutput().setGraphicsCursor(ho, ve);
        state.getOutput().flush();
    }

    /**
     * <p>{@code GOTO 950}: terminate execution.</p>
     *
     * <p><b>Implementation note:</b> if the {@code -hold} command line flag was used, or if the
     * {@link de.haupz.basicode.interpreter.Configuration#hold hold} interpreter configuration was applied, execution
     * will halt but wait for one final key press before terminating for good.</p>
     *
     * @param state the interpreter state.
     */
    public static void goto950(InterpreterState state) {
        if (state.getConfiguration().hold()) {
            gosub210(state);
        }
        state.terminate();
    }

    /**
     * <p>{@code GOSUB 960}: dump all variable values and array contents.</p>
     *
     * <p>This subroutine dumps all current BASICODE variables and arrays to the Java console.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub960(InterpreterState state) {
        String values = state.getValues();
        System.err.println("===== BASICODE values =====\n" + values + "\n===========================");
    }

    /**
     * <p>{@code GOSUB 961}: dump selected variable values and array contents.</p>
     *
     * <p>This subroutine dumps selected current BASICODE variables and arrays to the Java console. The variable values
     * and array contents displayed will be governed by what is contained in the {@code OD$()} array. Each element of
     * that array should be the name of a variable or array the values or contents of which should be displayed. The
     * {@code OD$()} array must be declared and properly dimensioned before the first use of this subroutine.</p>
     *
     * <p>If an element of {@code OD$()} is unusable (empty string, or a name that's not defined), it will be ignored.
     * If the {@code OD$()} array is not defined, this will be ignored, and no variable values or array contents will be
     * dumped.</p>
     *
     * <p>Variable names are simply the names including a {@code $} for string variables; array names must have the form
     * {@code ARRAY_NAME$()} or {@code ARRAY_NAME()}.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub961(InterpreterState state) {
        String values = state.getSelectedValues();
        System.err.println("===== BASICODE values =====\n" + values + "\n===========================");
    }

    /**
     * <p>{@code GOSUB 962}: print the call stack.</p>
     *
     * <p>This subroutine prints the current BASICODE call stack to the Java console. Execution will not be
     * interrupted.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub962(InterpreterState state) {
        String stackDump = state.getStackDump(true);
        System.err.println("===== BASICODE stack dump =====\n" + stackDump + "\n===============================");
    }

    /**
     * <p>{@code GOSUB 963}: trigger a breakpoint.</p>
     *
     * <p>When this subroutine is called, a dialogue box will open that displays the current call stack and
     * the values of all variables and contents of all arrays.</p>
     *
     * <p>If, at the time the subroutine is called, the string `OC$` contains a BASICODE condition that evaluates to
     * false, the breakpoint will not be triggered. If the string is undefined or empty at that time, that counts as the
     * condition being true.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub963(InterpreterState state) {
        if (state.shouldTriggerBreakpoint()) {
            String stackDump = state.getStackDump(true);
            String values = state.getValues();
            String content = stackDump + "\n" + values;
            state.getBreakpointHandler().breakRun(state, content);
        }
    }

    /**
     * <p>{@code GOSUB 964}: trigger a breakpoint with selective variable and array display.</p>
     *
     * <p>When this subroutine is called, a dialogue box will open that displays the current call stack and the values
     * of all variables and contents of all arrays. The variable values and array contents displayed will be governed by
     * what is contained in the {@code OD$()} array. Each element of that array should be the name of a variable or
     * array the values or contents of which should be displayed. The {@code OD$()} array must be declared and properly
     * dimensioned before the first use of this subroutine.</p>
     *
     * <p>If an element of {@code OD$()} is unusable (empty string, or a name that's not defined), it will be ignored.
     * If the {@code OD$()} array is not defined, this will be ignored, and the breakpoint will display no variable
     * values or array contents.</p>
     *
     * <p>Variable names are simply the names including a {@code $} for string variables; array names must have the form
     * {@code ARRAY_NAME$()} or {@code ARRAY_NAME()}.</p>
     *
     * <p>If, at the time the subroutine is called, the string `OC$` contains a BASICODE condition that evaluates to
     * false, the breakpoint will not be triggered. If the string is undefined or empty at that time, that counts as the
     * condition being true.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub964(InterpreterState state) {
        if (state.shouldTriggerBreakpoint()) {
            String content = state.getDebugInfo(true);
            state.getBreakpointHandler().breakRun(state, content);
        }
    }

    /**
     * <p>{@code GOSUB 965}: register a watchpoint.</p>
     *
     * <p>Calling this subroutine registers a watchpoint. It will be triggered when a condition flips from "unmet" to
     * "met". The condition is expressed in BASICODE syntax in the {@code OC$} variable. After execution of the
     * subroutine, the variable {@code OP} will contain a running number of the watchpoint. Numbering starts at 1. If
     * anything goes wrong during watchpoint registration, {@code OP} will be set to -1. Thus, 0 is an undefined value
     * for {@code OP}.</p>
     *
     * <p>The watchpoint will be triggered whenever the condition flips from "unmet" to "met" after the execution of a
     * statement. It will honour the contents of the {@code OD$()} array for selective display of variable values and
     * array contents, as described for {@link #gosub964 subroutine 964}.</p>
     *
     * @param state the interpreter state.
     */
    public static void gosub965(InterpreterState state) {
        double op = -1.0;
        Optional<Object> oods = state.getVar("OC$");
        if (oods.isPresent()) {
            String ods = (String) oods.get();
            Parser parser = new Parser(new StringReader(ods));
            ExpressionNode condition = parser.expression();
            op = state.getProgramInfo().registerWatchpoint(condition);
        }
        state.setVar("OP", Double.valueOf(op));
    }

}
