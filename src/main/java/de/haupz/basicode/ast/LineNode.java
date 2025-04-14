package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.util.List;

/**
 * The representation of a single line of code from a BASICODE program.
 */
public class LineNode extends BasicNode {

    /**
     * The source line number.
     */
    private final int lineNumber;

    /**
     * All statements found on this line in the source code.
     */
    private final List<StatementNode> statements;

    /**
     * The complete text of the line, from the source.
     */
    private final String lineText;

    /**
     * Construct a {@code LineNode} from a line number and list of statements.
     *
     * @param lineNumber the line number.
     * @param statements the statements.
     */
    public LineNode(int lineNumber, List<StatementNode> statements, String lineText) {
        this.lineNumber = lineNumber;
        this.statements = List.copyOf(statements);
        this.lineText = lineText;
    }

    /**
     * While a {@code LineNode} isn't an expression, it is also not seen as a statement. Execution of lines is done
     * statement by statement in the {@link ProgramNode#run(InterpreterState)} interpreter loop. Consequently, this
     * method just throws an exception.
     *
     * @param state the interpreter state.
     */
    @Override
    public void run(InterpreterState state) {
        throw new IllegalStateException("a LineNode should not be run");
    }

    /**
     * Throw an exception, as a {@code LineNode} isn't an expression.
     *
     * @param state the interpreter state.
     * @return nothing; throws an exception.
     */
    @Override
    public Object eval(InterpreterState state) {
        throw new IllegalStateException("a LineNode should not be evaluated");
    }

    /**
     * @return this line's line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return this line's list of statements.
     */
    public List<StatementNode> getStatements() {
        return statements;
    }

    /**
     * @return this line's source.
     */
    public String getLineText() {
        return lineText;
    }

}
