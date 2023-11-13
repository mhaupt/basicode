package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.io.IOException;

public class InputNode extends StatementNode {

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
