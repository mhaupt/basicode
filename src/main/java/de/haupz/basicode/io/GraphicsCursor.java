package de.haupz.basicode.io;

/**
 * A tuple representing the BASICODE graphics cursor. Coordinates are floating-point numbers in the range 0 <= x <= 1,
 * indicating the relative position in graphics mode.
 *
 * @param h the horizontal position of the graphics cursor.
 * @param v the vertical position of the graphics cursor.
 */
public record GraphicsCursor(double h, double v) {
}
