package de.haupz.basicode.io;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface BasicOutput {

    void print(String s);

    default void printReverse(String s) { print(s); }

    void println(String s);

    void println();

    void flush();

    default void textMode() {}

    default void setTextCursor(int ho, int ve) {}

    default int[] getTextCursor() { return new int[]{0,0}; }

    default void graphicsMode() {}

    default BufferedImage getImage() { throw new IllegalStateException("graphics not supported"); }

    default Font getFont() { throw new IllegalStateException("font not supported"); }

    default void setColours(int fg, int bg) { throw new IllegalStateException("colours not supported"); }

    default Color getBackgroundColour() { throw new IllegalStateException("colours not supported"); }

    default Color getForegroundColour() { throw new IllegalStateException("colours not supported"); }

}
