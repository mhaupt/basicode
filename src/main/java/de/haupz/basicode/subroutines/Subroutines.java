package de.haupz.basicode.subroutines;

import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.ui.Sound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        int ho = state.getStdVar("HO").intValue();
        int ve = state.getStdVar("VE").intValue();
        state.getOutput().setTextCursor(ho, ve);
    }

    /**
     * {@code GOSUB 120}: in text mode, store the current coordinates of the cursor in the {@code HO} and {@code VE}
     * variables.
     *
     * @param state the interpreter state.
     */
    public static void gosub120(InterpreterState state) {
        int[] coordinates = state.getOutput().getTextCursor();
        state.setVar("HO", Double.valueOf(coordinates[0]));
        state.setVar("VE", Double.valueOf(coordinates[1]));
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

    public static void gosub200(InterpreterState state) {
        char input = (char) state.getInput().lastChar();
        state.setVar("IN", Double.valueOf(Character.toUpperCase(input)));
        state.setVar("IN$", input == 0 ? "" : "" + input);
    }

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

    public static void gosub220(InterpreterState state) {
        int ho = state.getStdVar("HO").intValue();
        int ve = state.getStdVar("VE").intValue();
        char c = state.getOutput().getCharAt(ho, ve);
        state.setVar("IN", Double.valueOf(Character.toUpperCase(c)));
    }

    public static void gosub250(InterpreterState state) {
        if (state.getConfiguration().nosound()) {
            return;
        }
        Sound.play(440, 250, 100);
    }

    public static void gosub260(InterpreterState state) {
        state.setVar("RV", RND.nextDouble());
    }

    public static void gosub270(InterpreterState state) {
        state.setVar("FR", Double.valueOf(Runtime.getRuntime().freeMemory()));
    }

    public static void gosub280(InterpreterState state) {
        int fr = state.getStdVar("FR").intValue();
        state.getInput().toggleAcceptStopKey(fr == 0);
    }

    private static String fill(int length, char c) {
        char[] a = new char[length];
        Arrays.fill(a, c);
        return new String(a);
    }

    private static String decimalFormat(int decimalPlaces) {
        return "#" + (decimalPlaces > 0 ? "." + fill(decimalPlaces, '#') : "");
    }

    private static final DecimalFormat STANDARD_DECIMAL_FORMAT =
            new DecimalFormat(decimalFormat(9), DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public static void gosub300(InterpreterState state) {
        double sr = state.getStdVar("SR").doubleValue();
        String str = STANDARD_DECIMAL_FORMAT.format(sr);
        state.setVar("SR$", str);
    }

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

    public static void gosub310(InterpreterState state) {
        double sr = state.getStdVar("SR").doubleValue();
        int cn = state.getStdVar("CN").intValue();
        int ct = state.getStdVar("CT").intValue();
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

    public static void gosub330(InterpreterState state) {
        String srs = (String) state.getVar("SR$").get();
        state.setVar("SR$", srs.toUpperCase());
    }

    public static void gosub400(InterpreterState state) {
        if (state.getConfiguration().nosound()) {
            return;
        }
        int sp = state.getStdVar("SP").intValue();
        int sd = state.getStdVar("SD").intValue();
        int sv = state.getStdVar("SV").intValue();
        int frequency = (int) (440.0 * Math.pow(2.0, 100.0 * (sp - 69) / 1200.0));
        int duration = 100 * sd; // sd is in 0.1 s (100 ms)
        int volume = (int) (100/15.0 * sv); // volume is 0..100, mapping from 0..15
        Sound.play(frequency, duration, volume);
    }

    public static void gosub450(InterpreterState state) {
        if (state.getConfiguration().nowait()) {
            return;
        }
        int sd = state.getStdVar("SD").intValue();
        state.getInput().setSleepingThread(Thread.currentThread());
        try {
            Thread.sleep(sd * 100);
        } catch (InterruptedException e) {
            // ignore
        }
        state.getInput().setSleepingThread(null);
    }

    public static void gosub600(InterpreterState state) {
        setColours(state);
        state.getOutput().graphicsMode();
        state.setVar("HG", 320.0);
        state.setVar("VG", 200.0);
        state.setGraphicsCursor(0.0, 0.0);
    }

    private static void checkBoundaries(String name, double v) {
        if (v < 0.0 || v > 1.0) {
            throw new IllegalStateException("out of bounds: " + name + " = " + v);
        }
    }

    public static void gosub620(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = state.getStdVar("HO").doubleValue();
        double ve = state.getStdVar("VE").doubleValue();
        checkBoundaries("HO", ho);
        checkBoundaries("VE", ve);
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        setColours(state);
        g2.setPaint(state.getOutput().getForegroundColour());
        g2.setStroke(STROKE);
        double h = im.getWidth() * ho;
        double v = im.getHeight() * ve;
        g2.drawLine((int) h, (int) v, (int) h, (int) v);
        state.setGraphicsCursor(h, v);
        state.getOutput().flush();
    }

    public static void gosub630(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = state.getStdVar("HO").doubleValue();
        double ve = state.getStdVar("VE").doubleValue();
        InterpreterState.GraphicsCursor gc = state.getGraphicsCursor();
        checkBoundaries("HO", ho);
        checkBoundaries("VE", ve);
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        setColours(state);
        g2.setPaint(state.getOutput().getForegroundColour());
        g2.setStroke(STROKE);
        double nh = im.getWidth() * ho;
        double nv = im.getHeight() * ve;
        g2.drawLine((int) gc.h(), (int) gc.v(), (int) nh, (int) nv);
        state.setGraphicsCursor(nh, nv);
        state.getOutput().flush();
    }

    public static void gosub650(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = state.getStdVar("HO").doubleValue();
        double ve = state.getStdVar("VE").doubleValue();
        checkBoundaries("HO", ho);
        checkBoundaries("VE", ve);
        String sr = (String) state.getVar("SR$").get();
        int cn = state.getStdVar("CN").intValue();
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        setColours(state);
        Color c = cn == 0 ? state.getOutput().getForegroundColour() : state.getOutput().getBackgroundColour();
        g2.setPaint(c);
        g2.setFont(state.getOutput().getFont());
        FontMetrics fm = g2.getFontMetrics();
        double h = im.getWidth() * ho;
        double v = im.getHeight() * ve;
        g2.drawString(sr, (int) h, fm.getHeight() + (int) v);
        state.setGraphicsCursor(h, v);
        state.getOutput().flush();
    }

    public static void goto950(InterpreterState state) {
        if (state.getConfiguration().hold()) {
            gosub210(state);
        }
        state.terminate();
    }

}
