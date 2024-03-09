package de.haupz.basicode.io;

import de.haupz.basicode.ui.BasicContainer;

import java.awt.*;

/**
 * A set of configuration options for the graphical console.
 */
public final class ConsoleConfiguration {

    private ConsoleConfiguration() {}

    /**
     * The width of a character in pixels.
     */
    public static final int C_WIDTH = 24;

    /**
     * The height of a character in pixels.
     */
    public static final int C_HEIGHT = 24;

    /**
     * The number of columns in text mode.
     */
    public static final int COLUMNS = 40;

    /**
     * The width of the display in pixels.
     */
    public static final int WIDTH = C_WIDTH * COLUMNS;

    /**
     * The number of lines in text mode.
     */
    public static final int LINES = 25;

    /**
     * The height of the display in pixels.
     */
    public static final int HEIGHT = C_HEIGHT * LINES;

    /**
     * The font used to render text in text mode and graphics mode.
     */
    public static final Font FONT;

    static {
        try {
            FONT = Font.createFont(Font.TRUETYPE_FONT,
                    BasicContainer.class.getResourceAsStream("/amstrad_cpc464.ttf")).deriveFont(24.0f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This array maps the BASICODE colour codes (0..7) directly to the corresponding colours.
     */
    public static final Color[] COLOR_MAP = new Color[] {
            Color.BLACK,
            Color.BLUE,
            Color.RED,
            Color.MAGENTA,
            Color.GREEN,
            Color.CYAN,
            Color.YELLOW,
            Color.WHITE
    };

    /**
     * The number of colours supported by BASICODE.
     */
    public static final int N_COLORS = COLOR_MAP.length;

}
