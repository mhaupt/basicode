package de.haupz.basicode.io;

import java.io.IOException;

/**
 * An interface providing abstractions for input operations performed by the BASICODE implementation.
 */
public interface BasicInput {

    /**
     * Read a line of input terminated by a newline. This is a blocking operation.
     *
     * @return the line that was entered.
     * @throws IOException in case anything goes wrong with the input.
     */
    String readLine() throws IOException;

    /**
     * Read a single character from the input. This is a blocking operation.
     *
     * @return the character that was entered.
     * @throws IOException in case anything goes wrong witn the input.
     */
    int readChar() throws IOException;

    /**
     * If a character is available from the input at the time this method is called, return that character. This is a
     * non-blocking operation.
     *
     * @return the character available from the input at the time this method is called, or 0 if no character is
     * available.
     */
    default int lastChar() { return 0; }

    default void setSleepingThread(Thread sleepingThread) {}

    default void toggleAcceptStopKey(boolean acceptStopKey) {}

    default void registerStopKeyHandler(StopKeyHandler stopKeyHandler) {}

}
