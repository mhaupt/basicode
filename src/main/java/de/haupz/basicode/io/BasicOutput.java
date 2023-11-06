package de.haupz.basicode.io;

public interface BasicOutput {

    void print(String s);

    void println(String s);

    void println();

    void flush();

    default void clear() {}

    default void setTextCursor(int ho, int ve) {}

}
