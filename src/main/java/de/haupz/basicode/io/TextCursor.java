package de.haupz.basicode.io;

/**
 * A tuple representing the BASICODE environment's text cursor. The values will typically be in the ranges given.
 *
 * @param col the cursor column, with 0 <= {@code col} < 40.
 * @param row the cursor row, with 0 <= {@code row} < 25.
 */
public record TextCursor(int col, int row) {
}
