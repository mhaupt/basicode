package de.haupz.basicode.io;

/**
 * A functional interface to represent a handler for when the STOP key is pressed. The {@link #stopKeyPressed()} method
 * is called when the STOP key is pressed.
 */
public interface StopKeyHandler {
    void stopKeyPressed();
}
