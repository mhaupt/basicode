package de.haupz.basicode.io;

import java.io.PrintStream;

/**
 * <p>An implementation of the {@link BasicOutput} interface that uses the standard Java {@link PrintStream} class.</p>
 *
 * <p>This is used for testing purposes, and for simple console output. It does not support advanced features, such as
 * graphics mode.</p>
 */
public class PrintStreamOutput implements BasicOutput {

    private int col = 0;

    private int row = 0;

    private final PrintStream ps;

    public PrintStreamOutput(PrintStream ps) {
        this.ps = ps;
    }

    @Override
    public void print(String s) {
        ps.print(s);
        col += s.length();
    }

    @Override
    public void println(String s) {
        ps.println(s);
        col = 0;
        ++row;
    }

    @Override
    public void println() {
        ps.println();
        col = 0;
        ++row;
    }

    @Override
    public void flush() {
        ps.flush();
    }

    @Override
    public TextCursor getTextCursor() {
        return new TextCursor(col, row);
    }

}
