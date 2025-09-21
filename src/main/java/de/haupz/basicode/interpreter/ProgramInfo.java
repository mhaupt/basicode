package de.haupz.basicode.interpreter;

/**
 * A holder for information about a program.
 */
public class ProgramInfo {

    /**
     * A "code address", comprising of a line and statement index on that line.
     *
     * @param line a BASIC line number.
     * @param statement a 0-based statement index into the respective BASIC line.
     */
    public record LineAndStatement(int line, int statement) {}

}
