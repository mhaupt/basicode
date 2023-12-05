package de.haupz.basicode.subroutines;

import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.ast.PrintNode;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Subroutines {

    private static final Map<Integer, MethodHandle> ROUTINES = new HashMap<>();

    private static final MethodType ROUTINE_TYPE = MethodType.methodType(void.class, InterpreterState.class);

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private static MethodHandle lookupTarget(String methodName) {
        try {
            return LOOKUP.findStatic(Subroutines.class, methodName, ROUTINE_TYPE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerRoutine(Method method) {
        String name = method.getName();
        int target = Integer.parseInt(name.substring(name.startsWith("goto")?4:5));
        ROUTINES.put(target, lookupTarget(name));
    }

    static void registerRoutines() {
        Arrays.stream(Subroutines.class.getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> m.getName().matches("(goto|gosub)\\d+"))
                .forEach(Subroutines::registerRoutine);
    }

    static {
        registerRoutines();
    }

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

    private static final Random RND = new Random();

    private static final Stroke STROKE = new BasicStroke(3);

    private static void setColours(InterpreterState state) {
        BasicArray1D cc = (BasicArray1D) state.getVar("CC").get();
        int fg = ((Double) cc.at(0, -1)).intValue();
        int bg = ((Double) cc.at(1, -1)).intValue();
        state.getOutput().setColours(fg, bg);
    }

    public static void goto20(InterpreterState state) {
        state.setVar("HO", 0.0);
        state.setVar("VE", 0.0);
        state.setVar("HG", 0);
        state.setVar("VG", 0);
        state.setVar("SV", 15);
        state.setLineJumpTarget(1010);
        state.requestLineJump();
    }

    public static void gosub100(InterpreterState state) {
        setColours(state);
        state.getOutput().textMode();
    }

    public static void gosub110(InterpreterState state) {
        int ho = state.getStdVar("HO").intValue();
        int ve = state.getStdVar("VE").intValue();
        state.getOutput().setTextCursor(ho, ve);
    }

    public static void gosub120(InterpreterState state) {
        int[] coordinates = state.getOutput().getTextCursor();
        state.setVar("HO", Double.valueOf(coordinates[0]));
        state.setVar("VE", Double.valueOf(coordinates[1]));
    }

    public static void gosub150(InterpreterState state) {
        String sr = "   " + (String) state.getVar("SR$").get() + "   ";
        state.getOutput().printReverse(sr);
    }

    public static void gosub200(InterpreterState state) {
        char input = (char) state.getInput().lastChar();
        state.setVar("IN", Double.valueOf(input));
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
        state.setVar("IN", Double.valueOf(input));
    }

    public static void gosub220(InterpreterState state) {
        int ho = state.getStdVar("HO").intValue();
        int ve = state.getStdVar("VE").intValue();
        char c = state.getOutput().getCharAt(ho, ve);
        state.setVar("IN", Double.valueOf(c));
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

    public static void gosub300(InterpreterState state) {
        double sr = state.getStdVar("SR").doubleValue();
        String str = PrintNode.DECIMAL_FORMAT.format(sr);
        state.setVar("SR$", str);
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
        state.setVar("HG", 0);
        state.setVar("VG", 0);
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
        int hg = (int) (im.getWidth() * ho);
        int vg = (int) (im.getHeight() * ve);
        g2.drawLine(hg, vg, hg, vg);
        state.setVar("HG", hg);
        state.setVar("VG", vg);
        state.getOutput().flush();
    }

    public static void gosub630(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = state.getStdVar("HO").doubleValue();
        double ve = state.getStdVar("VE").doubleValue();
        int hg = state.getStdVar("HG").intValue();
        int vg = state.getStdVar("VG").intValue();
        checkBoundaries("HO", ho);
        checkBoundaries("VE", ve);
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        setColours(state);
        g2.setPaint(state.getOutput().getForegroundColour());
        g2.setStroke(STROKE);
        int nhg = (int) (im.getWidth() * ho);
        int nvg = (int) (im.getHeight() * ve);
        g2.drawLine(hg, vg, nhg, nvg);
        state.setVar("HG", nhg);
        state.setVar("VG", nvg);
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
        int hg = (int) (im.getWidth() * ho);
        int vg = (int) (im.getHeight() * ve);
        g2.drawString(sr, hg, fm.getHeight() + vg);
        state.setVar("HG", hg);
        state.setVar("VG", vg);
        state.getOutput().flush();
    }

    public static void goto950(InterpreterState state) {
        if (state.getConfiguration().hold()) {
            gosub210(state);
        }
        state.terminate();
    }

}
