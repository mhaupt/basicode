package de.haupz.basicode.io;

import java.io.PrintStream;

public class PrintStreamOutput implements BasicOutput {

    private final PrintStream ps;

    public PrintStreamOutput(PrintStream ps) {
        this.ps = ps;
    }

    @Override
    public void print(String s) {
        ps.print(s);
    }

    @Override
    public void println(String s) {
        ps.println(s);
    }

    @Override
    public void println() {
        ps.println();
    }

    @Override
    public void flush() {
        ps.flush();
    }

    @Override
    public void clear() {
        // naught
    }

}
