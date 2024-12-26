package de.haupz.basicode.io;

import de.haupz.basicode.ui.BasicContainer;

import java.util.Arrays;

/**
 * A buffer for the contents of the BASICODE console when it is in text mode. The buffer stores text as well as
 * information about whether each of the characters is printed in normal or reverse mode.
 */
public class TextBuffer {

    /**
     * The number of lines in the text buffer.
     */
    private final int lines;

    /**
     * The number of columns in the text buffer.
     */
    private final int columns;

    /**
     * The actual character buffer. It is a precise character-by-character mapping. Where there is no character being
     * displayed, the text buffer contains a space, rather than a {@code '\0'}. The first index is lines; the second,
     * columns.
     */
    private final char[][] textBuffer;

    /**
     * <p>A special array to store information about characters that should be displayed in reverse mode in text
     * mode.</p>
     *
     * <p>It's a two-dimensional {@code boolean} array, organised in the same fashion as the {@link #textBuffer} array
     * (lines first). Column arrays are created on demand. This array is essentially an overlay of the
     * {@link #textBuffer}; where it contains a {@code true} value, the corresponding character is to be displayed in
     * reverse mode.</p>
     */
    private final boolean[][] reverse;

    /**
     * The current line of the cursor in text mode.
     */
    private int curLine;

    /**
     * The current column of the cursor in text mode.
     */
    private int curColumn;

    public TextBuffer(int lines, int columns) {
        this.lines = lines;
        this.columns = columns;
        this.textBuffer = new char[lines][columns];
        this.reverse = new boolean[lines][columns];
        clear();
    }

    /**
     * Clear the buffer by filling it with space characters, and reset all information about reverse characters. Also,
     * set the text cursor to the top left corner.
     */
    public void clear() {
        for (int i = 0; i < lines; ++i) {
            Arrays.fill(textBuffer[i], ' ');
            Arrays.fill(reverse[i], false);
        }
        curLine = 0;
        curColumn = 0;
    }

    /**
     * Return whether the character at the given position is printed in reverse mode.
     *
     * @param line the line at which to check.
     * @param column the column at which to check.
     * @return the reverse-print status of the character at the given position.
     */
    public boolean isReverseAt(int line, int column) {
        return reverse[line][column];
    }

    /**
     * Return an entire line of text.
     *
     * @param line the position of the line.
     * @return the character array representing the requested line.
     */
    public char[] getLine(int line) {
        return textBuffer[line];
    }

    /**
     * Copy the characters from the string argument to the {@linkplain #textBuffer text buffer}.
     *
     * @param s the string to transfer to the text buffer.
     * @param r if {@code true}, print in reverse mode.
     */
    public void writeString(String s, boolean r) {
        char[] chars = s.toCharArray();
        if (s.contains("" + BasicContainer.BASICODE_DELETE)) {
            writeStringDelInternal(chars, r);
        } else {
            writeStringInternal(chars, r);
        }
    }

    /**
     * Interhal helper to write a string that contains backspace characters. The effect of a backspace character, when
     * printed, is to move the cursor back one character horizontally, and to delete the character in that position by
     * replacing it with a space. For reasons of simplicity, this method processes the characters one by one.
     */
    private void writeStringDelInternal(char[] chars, boolean r) {
        for (char c : chars) {
            if (c == BasicContainer.BASICODE_DELETE) {
                if (curColumn > 0) {
                    curColumn--;
                }
                textBuffer[curLine][curColumn] = ' ';
                reverse[curLine][curColumn] = r;
            } else {
                textBuffer[curLine][curColumn] = c;
                reverse[curLine][curColumn] = r;
                curColumn++;
                if (curColumn == columns) {
                    // wrap around if need be
                    lineFeed();
                }
            }
        }
    }

    /**
     * Internal helper to write a string that contains no backspace characters. This is an optimised implementation that
     * breaks down the string into chunks per line to handle wrapping around.
     *
     * @param chars the characters to print.
     * @param r if {@code true}, print in reverse mode.
     */
    private void writeStringInternal(char[] chars, boolean r) {
        for(int chunkStart = 0, // start position of the current chunk in the chars array
            remainingChars = chars.length - chunkStart; // this many characters remain to be printed
            remainingChars > 0; // loop while there are still characters to print
            remainingChars = chars.length - chunkStart // update remaining characters depending on new chunk start
        ) {
            int space = columns - curColumn; // this many characters still fit in the current line
            int chunkLength = Math.min(remainingChars, space); // this many characters can be printed on this line
            System.arraycopy(chars, chunkStart, textBuffer[curLine], curColumn, chunkLength);
            Arrays.fill(reverse[curLine], curColumn, curColumn + chunkLength, r);
            curColumn += chunkLength;
            if (curColumn == columns) {
                // wrap around if need be
                lineFeed();
            }
            chunkStart += chunkLength;
        }
    }

    /**
     * End a line, and scroll the contents up if need be.
     */
    public void lineFeed() {
        curLine++;
        curColumn = 0;
        if (curLine >= lines) {
            for (int l = 0; l < lines - 1; ++l) {
                System.arraycopy(textBuffer[l+1], 0, textBuffer[l], 0, columns);
                System.arraycopy(reverse[l+1], 0, reverse[l], 0, columns);
            }
            Arrays.fill(textBuffer[lines-1], ' ');
            Arrays.fill(reverse[lines-1], false);
            curLine--;
        }
    }

    /**
     * Set the cursor to the given coordinates.
     *
     * @param column the column at which to place the cursor.
     * @param line the line at which to place the cursor.
     */
    public void setCursor(int column, int line) {
        curColumn = column;
        curLine = line;
    }

    /**
     * @return the current cursor column.
     */
    public int getColumn() {
        return curColumn;
    }

    /**
     * @return the current cursor line.
     */
    public int getLine() {
        return curLine;
    }

    /**
     * Get the character at the indicated position.
     *
     * @param column the column from which to retrieve the character.
     * @param line the line from which to retrieve the character.
     * @return the character at the indicated position.
     */
    public char getCharAt(int column, int line) {
        return textBuffer[line][column];
    }

    /**
     * @return the number of lines in this text buffer.
     */
    public int getLines() {
        return lines;
    }

    /**
     * @return the number of columns in this text buffer.
     */
    public int getColumns() {
        return columns;
    }

}
