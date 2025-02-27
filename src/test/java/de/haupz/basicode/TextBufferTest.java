package de.haupz.basicode;

import de.haupz.basicode.io.ConsoleConfiguration;
import de.haupz.basicode.io.TextBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextBufferTest {

    private TextBuffer textBuffer;

    private static final Color BACKGROUND = ConsoleConfiguration.COLOR_MAP[1];

    private static final Color FOREGROUND = ConsoleConfiguration.COLOR_MAP[6];

    @BeforeEach
    public void setUp() {
        textBuffer = new TextBuffer(ConsoleConfiguration.LINES, ConsoleConfiguration.COLUMNS, BACKGROUND, FOREGROUND);
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
     * <p>Helper method to create reverse-printing buffers to be used as expected values. The multi-line content
     * argument will be padded with default values to the right and bottom to fill the buffer. Any content that exceeds
     * the height and width boundaries of the target buffer will be truncated and ignored.</p>
     *
     * <p>This method can be used for foreground and background colour buffers alike. This is indicated by the second
     * argument.</p>
     *
     * @param multiLineReverse reverse printing information to be pasted into the buffer. This is a string containing an
     *                         'R' character in every position the buffer should be reversed. Spaces will be interpreted
     *                         as non-reverse printing. Passing an empty string will lead to a buffer that is printed in
     *                         non-reverse mode entirely. Any content that is not 'R' or a space will be ignored.
     * @param isForeground if {@code true}, the buffer will represent foreground colours; it will represent background
     *                     colours otherwise.
     * @return the buffer constructed from the input strings.
     */
    private Color[][] makeReverse(String multiLineReverse, boolean isForeground) {
        Color[][] rev = new Color[textBuffer.getLines()][textBuffer.getColumns()];
        Color defaultColour = isForeground ? FOREGROUND : BACKGROUND;
        Color reverseColour = isForeground ? BACKGROUND : FOREGROUND;
        // pre-fill with standard background
        for (Color[] line : rev) {
            Arrays.fill(line, defaultColour);
        }
        // fill the buffer
        List<String> reverseLines = multiLineReverse.lines().toList();
        for (int l = 0; l < textBuffer.getLines(); ++l) {
            String reverseText = l >= reverseLines.size() ? "" : reverseLines.get(l);
            int max = Math.min(reverseText.length(), textBuffer.getColumns());
            for (int c = 0; c < max; c++) {
                if (reverseText.charAt(c) == 'R') {
                    rev[l][c] = reverseColour;
                }
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
     * Helper method to assert equality of colour arrays.
     *
     * @param isForeground if {@code true}, foreground colours are checked; otherwise, background colours.
     * @param reverseContent the content string indicating the reversed colour positions.
     */
    private void assertColoursEqual(boolean isForeground, String reverseContent) {
        Color[][] expected = makeReverse(reverseContent, isForeground);
        for (int l = 0; l < textBuffer.getLines(); ++l) {
            for (int c = 0; c < textBuffer.getColumns(); ++c) {
                Color actual =
                        isForeground ? textBuffer.getForegroundColourAt(l, c) : textBuffer.getBackgroundColourAt(l, c);
                assertEquals(expected[l][c], actual,
                        String.format("checking %s colours: mismatch at (line %d, column %d)\nexpected: %s, found %s",
                                isForeground ? "foreground" : "background", l, c, expected[l][c], actual));
            }
        }
    }

    @Test
    public void testMakeBufferAndReverse() {
        char[] chars = new char[ConsoleConfiguration.COLUMNS];
        Arrays.fill(chars, ' ');
        String spaces = new String(chars);
        StringBuilder sb = new StringBuilder();
        for (int l = 0; l < ConsoleConfiguration.LINES; ++l) {
            sb.append(spaces).append('\n');
        }
        String emptyBuffer = sb.toString();
        assertContentEquals(makeBuffer(emptyBuffer));
        assertColoursEqual(false, emptyBuffer);
        assertColoursEqual(true, emptyBuffer);
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
        assertColoursEqual(false, "");
        assertColoursEqual(true, "");
    }

    @Test
    public void testWrite() {
        textBuffer.writeString("Hello", false);
        assertContentEquals(makeBuffer("Hello"));
        assertColoursEqual(false, "");
        assertColoursEqual(true, "");
        assertEquals(0, textBuffer.getLine());
        assertEquals(5, textBuffer.getColumn());
        textBuffer.lineFeed();
        assertEquals(1, textBuffer.getLine());
        assertEquals(0, textBuffer.getColumn());
    }

    @Test
    public void testWriteReverse() {
        textBuffer.writeString("Hello", true);
        assertContentEquals(makeBuffer("Hello"));
        assertColoursEqual(false, "RRRRR");
        assertColoursEqual(true, "RRRRR");
        assertEquals(0, textBuffer.getLine());
        assertEquals(5, textBuffer.getColumn());
        textBuffer.lineFeed();
        assertEquals(1, textBuffer.getLine());
        assertEquals(0, textBuffer.getColumn());
    }

    @Test
    public void testWriteMixed() {
        textBuffer.writeString("Hello", false);
        textBuffer.lineFeed();
        textBuffer.lineFeed();
        textBuffer.writeString("world", true);
        assertContentEquals(makeBuffer("""
                Hello
                
                world"""));
        String rev = """
                
                
                RRRRR""";
        assertColoursEqual(false, rev);
        assertColoursEqual(true, rev);
        assertEquals(5, textBuffer.getColumn());
        assertEquals(2, textBuffer.getLine());
    }

    @Test
    public void testWriteMixedOnSameLine() {
        textBuffer.writeString("Hello, ", false);
        textBuffer.writeString("world", true);
        textBuffer.writeString("!", false);
        assertContentEquals(makeBuffer("Hello, world!"));
        String rev = "       RRRRR";
        assertColoursEqual(false, rev);
        assertColoursEqual(true, rev);
        assertEquals(0, textBuffer.getLine());
        assertEquals(13, textBuffer.getColumn());
    }

    @Test
    public void testScroll() {
        for (int i = 0; i < ConsoleConfiguration.LINES; ++i) {
            textBuffer.writeString(Integer.toString(i), false);
            textBuffer.lineFeed();
        }
        assertContentEquals(makeBuffer("""
                1
                2
                3
                4
                5
                6
                7
                8
                9
                10
                11
                12
                13
                14
                15
                16
                17
                18
                19
                20
                21
                22
                23
                24"""));
    }

    @Test
    public void testScrollReverse() {
        for (int i = 0; i < ConsoleConfiguration.LINES; ++i) {
            textBuffer.writeString(Integer.toString(i), i % 7 == 0);
            textBuffer.lineFeed();
        }
        assertContentEquals(makeBuffer("""
                1
                2
                3
                4
                5
                6
                7
                8
                9
                10
                11
                12
                13
                14
                15
                16
                17
                18
                19
                20
                21
                22
                23
                24"""));
        // For readability here, we're just replacing the bits that should be reversed with R, as all other content is
        // ignored.
        String rev = """
                1
                2
                3
                4
                5
                6
                R
                8
                9
                10
                11
                12
                13
                RR
                15
                16
                17
                18
                19
                20
                RR
                22
                23
                24""";
        assertColoursEqual(false, rev);
        assertColoursEqual(true, rev);
    }

    @Test
    public void testWrap() {
        textBuffer.writeString("0123456789ABCDE0123456789ABCDE0123456789ABCDE", false);
        assertContentEquals(makeBuffer("""
                0123456789ABCDE0123456789ABCDE0123456789
                ABCDE"""));
    }

    @Test
    public void testWrapReverse() {
        textBuffer.writeString("0123456789ABCDE", true);
        textBuffer.writeString("0123456789ABCDE", false);
        textBuffer.writeString("0123456789ABCDE", true);
        assertContentEquals(makeBuffer("""
                0123456789ABCDE0123456789ABCDE0123456789
                ABCDE"""));
        String rev = """
                RRRRRRRRRRRRRRR               RRRRRRRRRR
                RRRRR""";
        assertColoursEqual(false, rev);
        assertColoursEqual(true, rev);
    }

}
