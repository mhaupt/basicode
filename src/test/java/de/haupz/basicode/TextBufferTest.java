package de.haupz.basicode;

import de.haupz.basicode.io.ConsoleConfiguration;
import de.haupz.basicode.io.TextBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextBufferTest {

    private TextBuffer textBuffer;

    @BeforeEach
    public void setUp() {
        textBuffer = new TextBuffer(ConsoleConfiguration.LINES, ConsoleConfiguration.COLUMNS);
    }

    /**
     * Helper method to create text buffers to be used as expected values. The multi-line content argument will be
     * padded with spaces to the right and bottom to fill the buffer. Any content that exceeds the height and width
     * boundaries of the target buffer will be truncated and ignored.
     *
     * @param multiLineContent the character content to be pasted into the buffer. Passing an empty string will lead to
     *                         an empty buffer.
     * @return the buffer constructed from the input strings.
     */
    private char[][] makeBuffer(String multiLineContent) {
        char[][] buf = new char[textBuffer.getLines()][textBuffer.getColumns()];
        // pre-fill with spaces
        for (char[] line : buf) {
            Arrays.fill(line, ' ');
        }
        // fill the buffer line by line
        List<String> contentLines = multiLineContent.lines().toList();
        for (int l = 0; l < textBuffer.getLines(); ++l) {
            String content = l >= contentLines.size() ? "" : contentLines.get(l);
            System.arraycopy(content.toCharArray(), 0, buf[l], 0, Math.min(content.length(), textBuffer.getColumns()));
        }
        return buf;
    }

    /**
     * Helper method to create reverse-printing buffers to be used as expected values. The multi-line content argument
     * will be padded with {@code false} values to the right and bottom to fill the buffer. Any content that exceeds the
     * height and width boundaries of the target buffer will be truncated and ignored.
     *
     * @param multiLineReverse reverse printing information to be pasted into the buffer. This is a string containing an
     *                         'R' character in every position the buffer should be reversed. Spaces will be interpreted
     *                         as non-reverse printing. Passing an empty string will lead to a buffer that is printed in
     *                         non-reverse mode entirely. Any content that is not 'R' or a space will be ignored.
     * @return the buffer constructed from the input strings.
     */
    private boolean[][] makeReverse(String multiLineReverse) {
        boolean[][] rev = new boolean[textBuffer.getLines()][textBuffer.getColumns()];
        // pre-fill with falsehood
        for (boolean[] line : rev) {
            Arrays.fill(line, false);
        }
        // fill the buffer
        List<String> reverseLines = multiLineReverse.lines().toList();
        for (int l = 0; l < textBuffer.getLines(); ++l) {
            String reverseText = l >= reverseLines.size() ? "" : reverseLines.get(l);
            int max = Math.min(reverseText.length(), textBuffer.getColumns());
            for (int c = 0; c < max; c++) {
                rev[l][c] = reverseText.charAt(c) == 'R';
            }
        }
        return rev;
    }

    /**
     * Helper method to assert equality of the textual contents between the {@link #textBuffer} and an expected buffer.
     *
     * @param expected the expected contents.
     */
    private void assertContentEquals(char[][] expected) {
        for (int l = 0; l < textBuffer.getLines(); ++l) {
            char[] expectedLine = expected[l];
            char[] actualLine = textBuffer.getLine(l);
            assertEquals(0, Arrays.compare(expectedLine, actualLine),
                    String.format("mismatch in line %d\nexpected: %s\n  actual: %s", l, new String(expectedLine),
                            new String(actualLine)));
        }
    }

    /**
     * Helper method to assert equality of the textual contents between the {@link #textBuffer} and an expected buffer.
     *
     * @param expected the expected contents.
     */
    private void assertReverseEquals(boolean[][] expected) {
        for (int l = 0; l < textBuffer.getLines(); ++l) {
            for (int c = 0; c < textBuffer.getColumns(); c++) {
                assertEquals(expected[l][c], textBuffer.isReverseAt(l, c),
                        String.format("mismatch on line %d at column %d", l, c));
            }
        }
    }

    @Test
    public void testSize() {
        assertEquals(ConsoleConfiguration.LINES, textBuffer.getLines());
        assertEquals(ConsoleConfiguration.COLUMNS, textBuffer.getColumns());
    }

    @Test
    public void testInitialCursorPosition() {
        assertEquals(0, textBuffer.getLine());
        assertEquals(0, textBuffer.getColumn());
    }

    @Test
    public void testInitialContents() {
        assertContentEquals(makeBuffer(""));
        assertReverseEquals(makeReverse(""));
    }

}
