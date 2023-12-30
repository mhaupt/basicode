package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.BasicInput;

import java.io.IOException;
import java.util.List;

/**
 * {@code INPUT}. The implementation here optionally displays a prompt. It uses {@link BasicInput#readLine()} to read a
 * string, which it then stores in a variable, attempting to convert it to a number if the variable name indicates that.
 */
public class InputNode extends StatementNode {

    /**
     * A {@link PrintNode} used to print the prompt (if any).
     */
    private final PrintNode prompt;

    /**
     * The name of the variable to store the input in.
     */
    private final String id;

    public InputNode(String prompt, String id) {
        this.prompt = (null == prompt || prompt.isEmpty()) ?
                null :
                new PrintNode(List.of(
                    new PrintNode.Element(PrintNode.ElementType.EXPRESSION, new StringNode(prompt)),
                    new PrintNode.Element(PrintNode.ElementType.SEPARATOR, ";")));
        this.id = id;
    }

    @Override
    public void run(InterpreterState state) {
        String input;
        if (null != prompt) {
            prompt.run(state);
        }
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
