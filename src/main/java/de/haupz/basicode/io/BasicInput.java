package de.haupz.basicode.io;

import de.haupz.basicode.interpreter.InterpreterState;

import java.io.IOException;

public interface BasicInput {

    String readLine() throws IOException;

    int readChar() throws IOException;

    default int lastChar() { return 0; }

    default void setSleepingThread(Thread sleepingThread) {}

    default void toggleAcceptStopKey(boolean acceptStopKey) {}

    default void registerStopKeyHandler(StopKeyHandler stopKeyHandler) {}

}
