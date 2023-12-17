package de.haupz.basicode.subroutines;

import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.GraphicsCursor;
import de.haupz.basicode.io.TextCursor;
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
import java.nio.file.attribute.FileAttribute;
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
     * Set the foreground and background colours from the {@code CC} standard array stored in the
     * {@linkplain InterpreterState interpreter state}.
     *
     * @param state the current interpreter state, at the time of execution.
     */
    private static void setColours(InterpreterState state) {
        BasicArray1D cc = (BasicArray1D) state.getArray("CC").get();
        int fg = ((Double) cc.at(0, -1)).intValue();
        int bg = ((Double) cc.at(1, -1)).intValue();
        state.getOutput().setColours(fg, bg);
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
        setColours(state);
        state.getOutput().textMode();
    }

    /**
     * {@code GOSUB 110}: in text mode, position the cursor at the coordinates passed in {@code HO} and {@code VE}.
     *
     * @param state the interpreter state.
     */
    public static void gosub110(InterpreterState state) {
        int ho = getStdVar(state, "HO").intValue();
        int ve = getStdVar(state, "VE").intValue();
        state.getOutput().setTextCursor(ho, ve);
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
        state.setVar("IN", Double.valueOf(Character.toUpperCase(input)));
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
        state.setVar("IN", Double.valueOf(Character.toUpperCase(input)));
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
        char c = state.getOutput().getCharAt(ho, ve);
        state.setVar("IN", Double.valueOf(Character.toUpperCase(c)));
    }

    /**
     * <p>{@code GOSUB 250}: beep. Play a 440 Hz tone for 250 ms at full volume.</p>
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
        Sound.play(440, 250, 100);
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
        state.setVar("SR$", srs.toUpperCase());
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
        int frequency = (int) (440.0 * Math.pow(2.0, 100.0 * (sp - 69) / 1200.0));
        int duration = (int) (100 * sd); // sd is in 0.1 s (100 ms)
        int volume = (int) (100/15.0 * sv); // volume is 0..100, mapping from 0..15
        Sound.play(frequency, duration, volume);
    }

    /**
     * <p>{@code GOSUB 450}: wait for {@code SD} times 0.1 seconds, or until a key is pressed.</p>
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
            Thread.sleep(sd * 100);
        } catch (InterruptedException e) {
            // ignore
        }
        state.getInput().setReadyToInterrupt(false);
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
        if (mode == 0 || mode == 2 || mode == 4 || mode == 6) {
            // read mode: file should exist
            if (null == state.getCurrentOutFile() && Files.exists(p)) {
                try {
                    BufferedReader in = Files.newBufferedReader(p);
                    state.setCurrentInFile(in);
                } catch (IOException ioe) {
                    errorCode = -1;
                }
            } else {
                errorCode = -1;
            }
        } else if (mode == 1 || mode == 3 || mode == 5 || mode == 7) {
            // write mode; simply create the file
            if (null == state.getCurrentInFile()) {
                try {
                    PrintStream out = new PrintStream(Files.newOutputStream(p));
                    state.setCurrentOutFile(out);
                } catch (IOException e) {
                    errorCode = -1;
                }
            } else {
                errorCode = -1;
            }
        } else {
            errorCode = -1; // illegal mode
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
        }
        PrintStream out = state.getCurrentOutFile();
        if (null != out) {
            out.flush();
            out.close();
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
        setColours(state);
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
        checkBoundaries("HO", ho);
        checkBoundaries("VE", ve);
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        setColours(state);
        g2.setPaint(state.getOutput().getForegroundColour());
        g2.setStroke(STROKE);
        double h = im.getWidth() * ho;
        double v = im.getHeight() * ve;
        g2.drawLine((int) h, (int) v, (int) h, (int) v);
        state.getOutput().setGraphicsCursor(h, v);
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
        checkBoundaries("HO", ho);
        checkBoundaries("VE", ve);
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        setColours(state);
        g2.setPaint(state.getOutput().getForegroundColour());
        g2.setStroke(STROKE);
        double nh = im.getWidth() * ho;
        double nv = im.getHeight() * ve;
        g2.drawLine((int) gc.h(), (int) gc.v(), (int) nh, (int) nv);
        state.getOutput().setGraphicsCursor(nh, nv);
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
        checkBoundaries("HO", ho);
        checkBoundaries("VE", ve);
        String sr = (String) state.getVar("SR$").get();
        int cn = getStdVar(state, "CN").intValue();
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        setColours(state);
        Color c = cn == 0 ? state.getOutput().getForegroundColour() : state.getOutput().getBackgroundColour();
        g2.setPaint(c);
        g2.setFont(state.getOutput().getFont());
        FontMetrics fm = g2.getFontMetrics();
        double h = im.getWidth() * ho;
        double v = im.getHeight() * ve;
        g2.drawString(sr, (int) h, fm.getHeight() + (int) v);
        state.getOutput().setGraphicsCursor(h, v);
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

}
