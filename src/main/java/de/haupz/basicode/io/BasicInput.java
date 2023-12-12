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

    /**
     * Instruct the input to note that a waiting operation is under way, and that the input can interrupt this if a key
     * is pressed.
     *
     * @param ready if {@code true}, input will be ready to interrupt a waiting operation; if {@code false}, it won't.
     */
    default void setReadyToInterrupt(boolean ready) {}

    /**
     * Control whether pressing a stop key (e.g., ESC) will terminate program execution.
     *
     * @param acceptStopKey if {@code trye}, the stop key will terminate program execution; if {@code false}, it won't.
     */
    default void toggleAcceptStopKey(boolean acceptStopKey) {}

    /**
     * Register a handler to be invoked when the stop key is pressed.
     *
     * @param stopKeyHandler the handler.
     */
    default void registerStopKeyHandler(StopKeyHandler stopKeyHandler) {}

}
