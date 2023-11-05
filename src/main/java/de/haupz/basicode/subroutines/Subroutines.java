package de.haupz.basicode.subroutines;

import de.haupz.basicode.interpreter.InterpreterState;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

public class Subroutines {

    private static final Map<Integer, MethodHandle> ROUTINES = new HashMap<>();

    private static final MethodType ROUTINE_TYPE = MethodType.methodType(void.class, InterpreterState.class);

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    static MethodHandle lookupTarget(String type, int target) {
        String methodName = type + target;
        try {
            return LOOKUP.findStatic(Subroutines.class, methodName, ROUTINE_TYPE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static {
        ROUTINES.put(20, lookupTarget("goto", 20));
        ROUTINES.put(950, lookupTarget("goto", 950));
    }

    public static void runGoto(int target, InterpreterState state) {
        try {
            ROUTINES.get(target).invoke(state);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void goto20(InterpreterState state) {
        throw new IllegalStateException("not yet implemented");
    }

    public static void goto950(InterpreterState state) {
        state.terminate();
    }

}
