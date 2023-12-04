package de.haupz.basicode.io;

import java.io.IOException;

public interface BasicInput {

    String readLine() throws IOException;

    int readChar() throws IOException;

    default int lastChar() { return 0; }

    default void setSleepingThread(Thread sleepingThread) {}

}
