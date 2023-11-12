package de.haupz.basicode.subroutines;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.ui.Sound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        try {
            ROUTINES.get(target).invoke(state);
        } catch (Throwable t) {
            throw new IllegalStateException("subroutine not implemented: goto " + target);
        }
    }

    public static void runGosub(int target, InterpreterState state) {
        try {
            ROUTINES.get(target).invoke(state);
            state.requestReturn();
        } catch (Throwable t) {
            throw new IllegalStateException("subroutine not implemented: gosub " + target);
        }
    }

    private static final Stroke STROKE = new BasicStroke(3);

    public static void goto20(InterpreterState state) {
        state.setVar("HO", 0);
        state.setVar("VE", 0);
        state.setVar("HG", 0);
        state.setVar("VG", 0);
        state.setVar("SV", 15);
        state.setLineJumpTarget(1010);
        state.requestLineJump();
    }

    public static void gosub100(InterpreterState state) {
        state.getOutput().textMode();
    }

    public static void gosub110(InterpreterState state) {
        int ho = state.getStdVar("HO").intValue();
        int ve = state.getStdVar("VE").intValue();
        state.getOutput().setTextCursor(ho, ve);
    }

    public static void gosub150(InterpreterState state) {
        String sr = "   " + (String) state.getVar("SR$").get() + "   ";
        state.getOutput().printReverse(sr);
    }

    public static void gosub120(InterpreterState state) {
        int[] coordinates = state.getOutput().getTextCursor();
        state.setVar("HO", coordinates[0]);
        state.setVar("VE", coordinates[1]);
    }

    public static void gosub250(InterpreterState state) {
        Sound.play(440, 250, 100);
    }

    public static void gosub400(InterpreterState state) {
        int sp = state.getStdVar("SP").intValue();
        int sd = state.getStdVar("SD").intValue();
        int sv = state.getStdVar("SV").intValue();
        int frequency = (int) (440.0 * Math.pow(2.0, 100.0 * (sp - 69) / 1200.0));
        int duration = 100 * sd; // sd is in 0.1 s (100 ms)
        int volume = (int) (100/15.0 * sv); // volume is 0..100, mapping from 0..15
        Sound.play(frequency, duration, volume);
    }

    public static void gosub600(InterpreterState state) {
        state.getOutput().graphicsMode();
    }

    public static void gosub620(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = state.getStdVar("HO").doubleValue();
        double ve = state.getStdVar("VE").doubleValue();
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        g2.setPaint(Color.YELLOW);
        g2.setStroke(STROKE);
        int x = (int) (im.getWidth() * ho);
        int y = (int) (im.getHeight() * ve);
        g2.drawLine(x, y, x, y);
        state.setVar("HG", ho);
        state.setVar("VG", ve);
        state.getOutput().flush();
    }

    public static void gosub630(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = state.getStdVar("HO").doubleValue();
        double ve = state.getStdVar("VE").doubleValue();
        double hg = state.getStdVar("HG").doubleValue();
        double vg = state.getStdVar("VG").doubleValue();
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        g2.setPaint(Color.YELLOW);
        g2.setStroke(STROKE);
        g2.drawLine((int) (im.getWidth() * hg), (int) (im.getHeight() * vg),
                (int) (im.getWidth() * ho), (int) (im.getHeight() * ve));
        state.setVar("HG", ho);
        state.setVar("VG", ve);
        state.getOutput().flush();
    }

    public static void gosub650(InterpreterState state) {
        BufferedImage im = state.getOutput().getImage();
        double ho = state.getStdVar("HO").doubleValue();
        double ve = state.getStdVar("VE").doubleValue();
        String sr = (String) state.getVar("SR$").get();
        Graphics2D g2 = (Graphics2D) im.getGraphics();
        g2.setPaint(Color.YELLOW);
        g2.setFont(state.getOutput().getFont());
        g2.drawString(sr, (int) (im.getWidth() * ho), g2.getFontMetrics().getHeight() + (int) (im.getHeight() * ve));
        state.setVar("HG", ho);
        state.setVar("VG", ve);
        state.getOutput().flush();
    }

    public static void goto950(InterpreterState state) {
        state.terminate();
    }

}
