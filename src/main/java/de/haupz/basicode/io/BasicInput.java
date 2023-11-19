package de.haupz.basicode.io;

import java.io.IOException;

public interface BasicInput {

    String readLine() throws IOException;

    int readChar() throws IOException;

}
