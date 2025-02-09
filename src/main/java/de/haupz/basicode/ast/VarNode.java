package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.Optional;

/**
 * <p>Retrieving a variable. This works for both ordinary variables and arrays.</p>
 *
 * <p>When evaluated on non-array variables, the node checks whether the variable has already been initialised, and
 * initialises it to the type default (zero or empty string) if that is not the case. Arrays have to be initialised
 * using {@code DIM}, so the node does not check whether they have been initialised.</p>
 */
public class VarNode extends ExpressionNode {

    private final String id;

    private final boolean isArray;

    public VarNode(String id, boolean isArray) {
        this.id = id.toUpperCase();
        this.isArray = isArray;
    }

    public String getId() {
        return id;
    }

    public boolean isArray() {
        return isArray;
    }

    @Override
    public Object eval(InterpreterState state) {
        if (isArray) {
            return state.getArray(id).orElseThrow(() -> new IllegalStateException("no such array: " + id));
        }
        Optional<Object> v = state.getVar(id);
        if (v.isEmpty()) {
            Object value = id.endsWith("$") ? "" : 0.0;
            v = Optional.of(value);
            state.setVar(id, value);
        }
        return v.get();
    }

}
