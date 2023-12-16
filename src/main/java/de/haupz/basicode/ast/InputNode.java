package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.BasicInput;

import java.io.IOException;

/**
 * {@code INPUT}. The implementation here does not display a prompt. It uses {@link BasicInput#readLine()} to read a
 * string, which it then stores in a variable, attempting to convert it to a number if the variable name indicates that.
 */
public class InputNode extends StatementNode {

    /**
     * The name of the variable to store the input in.
     */
    private final String id;

    public InputNode(String id) {
        this.id = id;
    }

    @Override
    public void run(InterpreterState state) {
        String input;
        try {
            input = state.getInput().readLine();
        } catch (IOException ioe) {
            throw new IllegalStateException("could not read - " + ioe.getMessage());
        }
        if (id.endsWith("$")) {
            state.setVar(id, input);
        } else {
            try {
                state.setVar(id, Double.parseDouble(input));
            } catch (NumberFormatException nfed) {
                throw new IllegalStateException(String.format("expected number for variable %s, got %s", id, input));
            }
        }
    }

}
