package de.haupz.basicode.interpreter;

import de.haupz.basicode.ast.LineNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A holder for information about a program.
 */
public class ProgramInfo {

    /**
     * A "code address", comprising of a line and statement index on that line.
     *
     * @param line a BASIC line number.
     * @param statement a 0-based statement index into the respective BASIC line.
     */
    public record LineAndStatement(int line, int statement) {}

    /**
     * Map BASIC line numbers to indices in a {@link StatementIterator#statements flattened statements list}. The
     * statement index for any line number is the index of the first statement on the respective line.
     */
    private Map<Integer, Integer> lineNumberToStatementIndex = new HashMap<>();

    /**
     * Map statement indices from a {@link StatementIterator#statements flattened statements list} back to line numbers
     * and statements.
     */
    private Map<Integer, LineAndStatement> statementIndexToLineNumberAndStatement = new HashMap<>();

    /**
     * Populate the {@link #lineNumberToStatementIndex} and {@link #statementIndexToLineNumberAndStatement} maps by
     * processing all lines from the program.
     *
     * @param lines the lines of the program this info object will be representing.
     */
    public ProgramInfo(List<LineNode> lines) {
        for (int i = 0, totalStatements = 0; i < lines.size(); ++i) {
            LineNode line = lines.get(i);
            // A new line: the index of its first statement is the current size of the statements array.
            lineNumberToStatementIndex.put(line.getLineNumber(), totalStatements);
            totalStatements += line.getStatements().size();
            int nStatementsOnLine = line.getStatements().size();
            int lineNum = line.getLineNumber();
            for (int s = 0; s < nStatementsOnLine; ++s) {
                statementIndexToLineNumberAndStatement.put(
                        totalStatements - nStatementsOnLine + s, new LineAndStatement(lineNum, s));
            }
        }
    }

    /**
     * @param lineNumber a line number for which to retrieve the index in a
     *                   {@link StatementIterator#statements flattened statements list} where the line's first statement
     *                   is found.
     * @return the index of that statement.
     */
    public int getLineStartStamentIndex(int lineNumber) {
        return lineNumberToStatementIndex.get(lineNumber);
    }

    /**
     * @param flatIndex an index into a {@link StatementIterator#statements flattened statements list} for which to
     *                  retrieve the line number and line-local statement index.
     * @return the respective line number and line-local statement index.
     */
    public LineAndStatement locateStatement(int flatIndex) {
        return statementIndexToLineNumberAndStatement.get(flatIndex);
    }

}
