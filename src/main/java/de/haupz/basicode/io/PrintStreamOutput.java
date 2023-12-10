package de.haupz.basicode.io;

import java.io.PrintStream;

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
    public int[] getTextCursor() {
        return new int[]{col,row};
    }

}
