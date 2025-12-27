package de.haupz.basicode.interpreter;

import de.haupz.basicode.ast.ExpressionNode;
import de.haupz.basicode.ast.LineNode;

import java.util.*;
import java.util.stream.Stream;

/**
 * A holder for information about a program.
 */
public class ProgramInfo {

    /**
     * A map of line numbers to the corresponding source code lines.
     */
    private Map<Integer, LineNode> lines = new HashMap<>();

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
     * The list of watchpoints for this BASICODE program.
     */
    private List<Watchpoint> watchpoints = new ArrayList<>();

    /**
     * The list of breakpoints for this BASICODE program.
     */
    private List<Breakpoint> breakpoints = new ArrayList<>();

    /**
     * Populate the {@link #lineNumberToStatementIndex} and {@link #statementIndexToLineNumberAndStatement} maps by
     * processing all lines from the program.
     *
     * @param lines the lines of the program this info object will be representing.
     */
    public ProgramInfo(List<LineNode> lines) {
        for (int i = 0, totalStatements = 0; i < lines.size(); ++i) {
            LineNode line = lines.get(i);
            this.lines.put(line.getLineNumber(), line);
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

    /**
     * Register a new watchpoint.
     *
     * @param condition the condition that should trigger the new watchpoint.
     * @return the newly registered watchpoint's ID.
     */
    public int registerWatchpoint(ExpressionNode condition) {
        int id = watchpoints.size() + 1;
        watchpoints.add(new Watchpoint(id, condition));
        return id;
    }

    /**
     * @return the stream of conditional watchpoints for this BASICODE program.
     */
    public Stream<Watchpoint> watchpoints() {
        return watchpoints.stream();
    }

    /**
     * Register a new breakpoint.
     *
     * @param line the line number on which to set the breakpoint.
     * @param statementIndex the statement index on the line at which to set the breakpoint.
     * @param displayInfo the information to display when the breakpoint is triggered.
     * @param condition the condition that should trigger the breakpoint.
     * @return the newly registered breakpoint's ID.
     */
    public int registerBreakpoint(int line, int statementIndex, List<String> displayInfo, Optional<ExpressionNode> condition) {
        int id = breakpoints.size() + 1;
        breakpoints.add(new Breakpoint(id, line, statementIndex, displayInfo, condition));
        return id;
    }

    /**
     * @return the breakpoints (if any) for a given line number and statement index.
     *
     * @param here the current line and statement index.
     */
    public List<Breakpoint> breakpointsForHere(LineAndStatement here) {
        return breakpoints.stream()
                .filter(b -> b.getLine() == here.line && b.getStatementIndex() == here.statement)
                .toList();
    }

    /**
     * @param line a line number for which to check if it exists in this program.
     * @return {@code true} iff the given line number exists in this program.
     */
    public boolean hasLineNumber(int line) {
        return lineNumberToStatementIndex.containsKey(line);
    }

    /**
     * @param line a line number for which to retrieve the corresponding line node.
     * @return the line node for the given line number.
     */
    public LineNode getLine(int line) {
        return lines.get(line);
    }

}
