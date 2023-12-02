package de.haupz.basicode.interpreter;

public record Configuration(
        boolean nowait, // suppress waiting with GOSUB 450
        boolean nosound, // ignore any sound subroutine calls
        boolean hold // wait for a key to be pressed before terminating
) {}
