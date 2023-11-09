package de.haupz.basicode.io;

import java.awt.image.BufferedImage;

public interface BasicOutput {

    void print(String s);

    void println(String s);

    void println();

    void flush();

    default void textMode() {}

    default void setTextCursor(int ho, int ve) {}

    default int[] getTextCursor() { return new int[]{0,0}; }

    default void graphicsMode() {}

    default BufferedImage getImage() { throw new IllegalStateException("graphics not supported"); }

}
