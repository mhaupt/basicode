package de.haupz.basicode.ui;

import de.haupz.basicode.io.BasicOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class BasicContainer extends JComponent implements BasicOutput {

    public static final int C_WIDTH = 24;

    public static final int C_HEIGHT = 24;

    public static final int COLUMNS = 40;

    public static final int LINES = 25;

    public static final int WIDTH = C_WIDTH * COLUMNS;

    public static final int HEIGHT = C_HEIGHT * LINES;

    public static final Font FONT;

    static {
        try {
            FONT = Font.createFont(Font.TRUETYPE_FONT,
                    BasicContainer.class.getResourceAsStream("/amstrad_cpc464.ttf")).deriveFont(24.0f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final char[][] textBuffer = new char[LINES][COLUMNS];

    private int curLine;

    private int curColumn;

    private final boolean[][] reverse = new boolean[LINES][];

    private boolean isGraphicsMode = false;

    private final BufferedImage image;

    public BasicContainer() {
        super();
        setSize(WIDTH, HEIGHT);
        clearTextBuffer();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    public void clearTextBuffer() {
        for (char[] line : textBuffer) {
            Arrays.fill(line, ' ');
        }
        Arrays.fill(reverse, null);
        curLine = 0;
        curColumn = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        if (!isGraphicsMode) {
            g2.setBackground(Color.BLUE);
            g2.clearRect(0, 0, WIDTH, HEIGHT);
        }
    }

    @Override
    public void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintChildren(g2);
        if (isGraphicsMode) {
            g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        } else {
            g2.setFont(FONT);
            for (int l = 0; l < LINES; ++l) {
                if (reverse[l] != null) {
                    boolean reverseMode = false;
                    int c = 0;
                    while (c < COLUMNS) {
                        int start = c;
                        int end = start;
                        while (end < COLUMNS && reverse[l][end] == reverseMode) {
                            ++end;
                        }
                        if (end > start) {
                            if (reverseMode) {
                                g2.setColor(Color.YELLOW);
                                g2.fillRect(start * C_WIDTH, l * C_HEIGHT, (end - start) * C_WIDTH, C_HEIGHT);
                                g2.setColor(Color.BLUE);
                                g2.drawChars(textBuffer[l], start, end - start, start * C_WIDTH, (l + 1) * C_HEIGHT);
                            } else {
                                g2.setColor(Color.YELLOW);
                                g2.drawChars(textBuffer[l], start, end - start, start * C_WIDTH, (l + 1) * C_HEIGHT);
                            }
                        }
                        c = end;
                        reverseMode = !reverseMode;
                    }
                } else {
                    g2.setColor(Color.YELLOW);
                    g2.drawChars(textBuffer[l], 0, COLUMNS, 0, (l + 1) * C_HEIGHT);
                }
            }
        }
    }

    @Override
    public void print(String s) {
        System.arraycopy(s.toCharArray(), 0, textBuffer[curLine], curColumn, s.length());
        curColumn += s.length();
        repaint();
    }

    @Override
    public void printReverse(String s) {
        if (reverse[curLine] == null) {
            reverse[curLine] = new boolean[COLUMNS];
        }
        Arrays.fill(reverse[curLine], curColumn, curColumn + s.length(), true);
        print(s);
    }

    @Override
    public void println(String s) {
        print(s);
        println();
    }

    @Override
    public void println() {
        curLine++;
        curColumn = 0;
        if (curLine >= LINES) {
            for (int l = 0; l < LINES - 1; ++l) {
                System.arraycopy(textBuffer[l+1], 0, textBuffer[l], 0, COLUMNS);
            }
            Arrays.fill(textBuffer[LINES-1], ' ');
            curLine--;
            repaint();
        }
    }

    @Override
    public void flush() {
        repaint();
    }

    @Override
    public void textMode() {
        isGraphicsMode = false;
        clearTextBuffer();
        repaint();
    }

    @Override
    public void setTextCursor(int ho, int ve) {
        curColumn = ho;
        curLine = ve;
    }

    @Override
    public int[] getTextCursor() {
        return new int[] { curColumn, curLine };
    }

    @Override
    public void graphicsMode() {
        isGraphicsMode = true;
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setBackground(Color.BLUE);
        g2.clearRect(0, 0, WIDTH, HEIGHT);
        repaint();
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public Font getFont() {
        return FONT;
    }

}
