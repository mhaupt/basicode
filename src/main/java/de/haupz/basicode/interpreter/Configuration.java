package de.haupz.basicode.interpreter;

/**
 * The {@code Configuration} record represents a configuration of the BASICODE environment. It is usually controlled by
 * command line flags.
 *
 * @param nowait suppress waiting with {@linkplain de.haupz.basicode.subroutines.Subroutines#gosub450(InterpreterState)
 * GOSUB 450}.
 * @param nosound ignore any sound-related subroutine calls.
 * @param hold wait for a key to be pressed before terminating.
 * @param enforceBoundaries enforce that {@code HO}/{@code VE} coordinates in graphics mode are not allowed to be
 *                          outside the 0 <= x <= 1 range.
 * @param showMapKeys display debugging information when keys are pressed and mapped.
 */
public record Configuration(
        boolean nowait,
        boolean nosound,
        boolean hold,
        boolean enforceBoundaries,
        boolean showMapKeys
) {
    public Configuration() {
        this(false, false, false, false, false);
    }
}
