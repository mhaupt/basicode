package de.haupz.basicode.io;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedReaderInput implements BasicInput {

    private final BufferedReader br;

    public BufferedReaderInput(BufferedReader br) {
        this.br = br;
    }

    @Override
    public String readLine() throws IOException {
        return br.readLine();
    }

}
