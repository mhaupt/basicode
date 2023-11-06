package de.haupz.basicode.subroutines;

import de.haupz.basicode.interpreter.InterpreterState;

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
            throw new RuntimeException(t);
        }
    }

    public static void runGosub(int target, InterpreterState state) {
        try {
            ROUTINES.get(target).invoke(state);
            state.requestReturn();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

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
        state.getOutput().clear();
    }

    public static void gosub110(InterpreterState state) {
        int ho = ((Integer) state.getVar("HO").get()).intValue();
        int ve = ((Integer) state.getVar("VE").get()).intValue();
        state.getOutput().setTextCursor(ho, ve);
    }

    public static void goto950(InterpreterState state) {
        state.terminate();
    }

}
