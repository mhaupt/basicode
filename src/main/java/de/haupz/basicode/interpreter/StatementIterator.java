package de.haupz.basicode.interpreter;

import de.haupz.basicode.ast.LineNode;
import de.haupz.basicode.ast.StatementNode;

import java.util.ArrayList;
import java.util.List;

/**
 * An iterator for the sequential flattened list of all statements in a program.
 */
public class StatementIterator {

    /**
     * The flattened representation of all statements of a program.
     */
    private List<StatementNode> statements = new ArrayList<>();

    /**
     * The index of the next statement to retrieve.
     */
    private int index;

    /**
     * Create a new statement iterator, populating the {@link #statements} list from the source line nodes provided in
     * the argument.
     *
     * @param lines a program's source lines.
     */
    public StatementIterator(List<LineNode> lines) {
        lines.forEach(line -> statements.addAll(List.copyOf(line.getStatements())));
    }

    /**
     * @return the index of the next statement to retrieve.
     */
    public int getNextIndex() {
        return index;
    }

    /**
     * Set the index to the given argument after bounds-checking.
     *
     * @param idx the index of the next statement to retrieve via a call to {@link #getNext()}.
     */
    public void setIndex(int idx) {
        validate(idx);
        index = idx;
    }

    /**
     * @return {@code true} iff there is a next statement to retrieve.
     */
    public boolean hasNext() {
        return index < statements.size();
    }

    /**
     * @return the next statement from the current iterator position.
     */
    public StatementNode getNext() {
        validate(index);
        return statements.get(index++);
    }

    /**
     * @param idx an index to retrieve a statement from without iterating.
     * @return the statement at the given index in the flattened statements list.
     */
    public StatementNode peek(int idx) {
        validate(idx);
        return statements.get(idx);
    }

    /**
     * Bounds-check the given index, and throw an exception if it's out of bounds.
     *
     * @param idx the index to check.
     */
    private void validate(int idx) {
        if (idx < 0 || idx >= statements.size()) {
            throw new IllegalStateException(String.format("index %d out of bounds [0;%d]", idx, statements.size()));
        }
    }

}
