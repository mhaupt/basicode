package de.haupz.basicode.io;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * <p>An implementation of the {@link BasicInput} interface using the {@link BufferedReader} standard class.</p>
 *
 * <p>This is used for testing purposes, and for simple console output (without advanced features such as stop keys and
 * interruption of waiting operations).</p>
 */
public class BufferedReaderInput implements BasicInput {

    private final BufferedReader br;

    public BufferedReaderInput(BufferedReader br) {
        this.br = br;
    }

    @Override
    public String readLine() throws IOException {
        return br.readLine();
    }

    @Override
    public int readChar() throws IOException {
        return br.read();
    }

}
