package de.haupz.basicode.io;

import de.haupz.basicode.ui.BasicContainer;

import java.awt.*;
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
     * <p>A dedicated array to store the colours characters are printed in in text mode.</p>
     *
     * <p>It's a two-dimensional array of {@link Color colours}, organised in the same fashion as the
     * {@link #textBuffer} array (lines first). This array is essentially an overlay of the {@link #textBuffer}; it
     * stores the character colour for each single position at which a character can be shown.</p>
     */
    private final Color[][] foregroundColours;

    /**
     * <p>A dedicated array to store the colours of the backgrounds characters are printed on in text mode.</p>
     *
     * <p>It's a two-dimensional array of {@link Color colours}, organised in the same fashion as the
     * {@link #textBuffer} array (lines first). This array is essentially an overlay of the {@link #textBuffer}; it
     * stores the background colour for each single position at which a character can be shown.</p>
     */
    private final Color[][] backgroundColours;

    /**
     * The current line of the cursor in text mode.
     */
    private int curLine;

    /**
     * The current column of the cursor in text mode.
     */
    private int curColumn;

    /**
     * The current background colour.
     */
    private Color curBackgroundColour;

    /**
     * The current foreground colour.
     */
    private Color curForegroundColour;

    public TextBuffer(int lines, int columns, Color backgroundColour, Color foregroundColour) {
        this.lines = lines;
        this.columns = columns;
        this.textBuffer = new char[lines][columns];
        this.foregroundColours = new Color[lines][columns];
        this.backgroundColours = new Color[lines][columns];
        this.curBackgroundColour = backgroundColour;
        this.curForegroundColour = foregroundColour;
        clear();
    }

    /**
     * Clear the buffer by filling it with space characters, and reset all information about reverse characters. Also,
     * set the text cursor to the top left corner.
     */
    public void clear() {
        for (int i = 0; i < lines; ++i) {
            Arrays.fill(textBuffer[i], ' ');
            Arrays.fill(backgroundColours[i], curBackgroundColour);
            Arrays.fill(foregroundColours[i], curForegroundColour);
        }
        curLine = 0;
        curColumn = 0;
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
                setColoursAt(curLine, curColumn, r);
            } else {
                textBuffer[curLine][curColumn] = c;
                setColoursAt(curLine, curColumn, r);
                curColumn++;
                if (curColumn == columns) {
                    // wrap around if need be
                    lineFeed();
                }
            }
        }
    }

    /**
     * Internal helper to set the background and foreground colours at a given position, depending on whether the
     * position should be reversed or not.
     *
     * @param line the line at which to set the colours.
     * @param column the column at which to set the colours.
     * @param r indicate whether the colours should be reversed.
     */
    private void setColoursAt(int line, int column, boolean r) {
        foregroundColours[line][column] = r ? curBackgroundColour : curForegroundColour;
        backgroundColours[line][column] = r ? curForegroundColour : curBackgroundColour;
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
            Arrays.fill(foregroundColours[curLine], curColumn, curColumn + chunkLength,
                    r ? curBackgroundColour : curForegroundColour);
            Arrays.fill(backgroundColours[curLine], curColumn, curColumn + chunkLength,
                    r ? curForegroundColour : curBackgroundColour);
            curColumn += chunkLength;
            if (curColumn == columns) {
                // wrap around if need be
                lineFeed();
            }
            chunkStart += chunkLength;
        }
    }

    /**
     * Retrieves the foreground colour at the specified line and column.
     *
     * @param line the line from which to retrieve the foreground colour.
     * @param column the column from which to retrieve the foreground colour.
     * @return the foreground colour at the specified position.
     */
    public Color getForegroundColourAt(int line, int column) {
        return foregroundColours[line][column];
    }

    /**
     * Retrieves the background colour at the specified line and column.
     *
     * @param line the line from which to retrieve the background colour.
     * @param column the column from which to retrieve the background colour.
     * @return the background colour at the specified position.
     */
    public Color getBackgroundColourAt(int line, int column) {
        return backgroundColours[line][column];
    }

    /**
     * Set the colours.
     *
     * @param foregroundColour the new foreground colour.
     * @param backgroundColour the new background colour.
     */
    public void setColours(Color foregroundColour, Color backgroundColour) {
        curForegroundColour = foregroundColour;
        curBackgroundColour = backgroundColour;
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
                System.arraycopy(backgroundColours[l+1], 0, backgroundColours[l], 0, columns);
                System.arraycopy(foregroundColours[l+1], 0, foregroundColours[l], 0, columns);
            }
            Arrays.fill(textBuffer[lines-1], ' ');
            Arrays.fill(foregroundColours[lines-1], curForegroundColour);
            Arrays.fill(backgroundColours[lines-1], curBackgroundColour);
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
