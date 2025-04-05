package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.BasicInput;

import java.io.IOException;
import java.util.List;

/**
 * {@code INPUT}. The implementation here optionally displays a prompt. It uses a node wrapping a call to
 * {@link BasicInput#readLine()} to read a string. It then uses a {@link LetNode} to handle storing the input.
 */
public class InputNode extends StatementNode {

    /**
     * A {@link PrintNode} used to print the prompt (if any).
     */
    private final PrintNode prompt;

    /**
     * To read the input.
     */
    private final ReadLineNode readLine;

    /**
     * To handle storing the input.
     */
    private final LetNode let;

    /**
     * A helper to read a line from the input and possibly convert it to a number.
     */
    private final class ReadLineNode extends ExpressionNode {
        private final boolean isString;
        protected ReadLineNode(boolean isString) {
            this.isString = isString;
        }
        @Override
        public Object eval(InterpreterState state) {
            String input = "";
            try {
                input = state.getInput().readLine();
                if (isString) {
                    return input;
                }
                return Double.parseDouble(input);
            } catch (IOException ioe) {
                throw new IllegalStateException("could not read - " + ioe.getMessage());
            } catch (NumberFormatException nfed) {
                throw new IllegalStateException(String.format("expected number, got %s", input));
            }
        }
    }

    public InputNode(String prompt, LetNode.LHS lhs) {
        this.prompt = new PrintNode(List.of(
                    new PrintNode.Element(PrintNode.ElementType.EXPRESSION, new StringNode(prompt)),
                    new PrintNode.Element(PrintNode.ElementType.SEPARATOR, ";")));
        this.readLine = new ReadLineNode(lhs.isString());
        this.let = new LetNode(lhs, readLine);
    }

    public PrintNode getPrompt() {
        return prompt;
    }

    public LetNode getLet() {
        return let;
    }

    @Override
    public void run(InterpreterState state) {
        if (null != prompt) {
            prompt.run(state);
        }
        let.run(state);
    }

}
