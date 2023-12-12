package de.haupz.basicode.io;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface BasicOutput {

    /**
     * Print a string on the output, with no newline added at the end.
     *
     * @param s the string to print.
     */
    void print(String s);

    /**
     * Print a string with foreground and background colours reversed, with no newline added at the end. Not supported
     * in all implementations.
     *
     * @param s the string to print in reverse mode.
     */
    default void printReverse(String s) { print(s); }

    /**
     * Print a string on the output, with a newline added at the end.
     *
     * @param s the string to print.
     */
    void println(String s);

    /**
     * Print a single newline.
     */
    void println();

    /**
     * Flush the output, refresh the representation.
     */
    void flush();

    /**
     * Switch the output to text mode and clear it. Not supported in all implementations.
     */
    default void textMode() {}

    /**
     * Position the text mode cursor at the given horizontal and vertical coordinates. Not supported in all
     * implementations.
     *
     * @param ho the horizontal coordinate, starting at 0.
     * @param ve the vertical coordinate, starting at 0.
     */
    default void setTextCursor(int ho, int ve) {}

    /**
     * Return the current position of the text cursor. Not supported in all implementations.
     *
     * @return the current position of the text cursor.
     */
    TextCursor getTextCursor();

    /**
     * Switch the output to graphics mode, and clear the screen. Not supported in all implementations.
     */
    default void graphicsMode() {}

    /**
     * Retrieve the image representing the screen in graphics mode. Not supported in all implementations.
     *
     * @return the image representing the screen in graphics mode.
     */
    default BufferedImage getImage() { throw new IllegalStateException("graphics not supported"); }

    /**
     * Get the font used to print and draw text (in both text and graphics mode). Not supported in all implementations.
     *
     * @return the font used to print and draw text.
     */
    default Font getFont() { throw new IllegalStateException("font not supported"); }

    /**
     * <p>Set the foregraound and background colours. Not supported in all implementations.</p>
     *
     * <p>The colours are defined as follows:<ul>
     *     <li>0: black</li>
     *     <li>1: blue</li>
     *     <li>2: red</li>
     *     <li>3: magenta</li>
     *     <li>4: green</li>
     *     <li>5: cyan</li>
     *     <li>6: yellow</li>
     *     <li>7: white</li>
     * </ul></p>
     *
     * @param fg the foreground colour.
     * @param bg the background colour.
     */
    default void setColours(int fg, int bg) { throw new IllegalStateException("colours not supported"); }

    /**
     * @return the current background colout (not supported in all implementations).
     */
    default Color getBackgroundColour() { throw new IllegalStateException("colours not supported"); }

    /**
     * @return the current foreground colour (not supported in all implementations).
     */
    default Color getForegroundColour() { throw new IllegalStateException("colours not supported"); }

    /**
     * Get the character displayed at the given position. Not supported in all implementations.
     *
     * @param ho the horizontal position.
     * @param ve the vertical position.
     * @return the character displayed at the given position.
     */
    default char getCharAt(int ho, int ve) { return ' '; }

    /**
     * <p>Position the internal graphics cursor at the given relative coordinates. Not supported in all
     * implementations.</p>
     *
     * <p>The coordinates are fractions of the total screen resolution; both have to be in the range 0 <= x <= 1.</p>
     *
     * @param h the horizontal coordinate.
     * @param v the vertical coordinate.
     */
    default void setGraphicsCursor(double h, double v) {}

    /**
     * @return the current coordinates of the graphics cursor (not supported in all implementations).
     */
    default GraphicsCursor getGraphicsCursor() { throw new IllegalStateException("graphics mode not supported"); }

}
