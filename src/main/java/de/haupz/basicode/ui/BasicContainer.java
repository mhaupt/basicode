package de.haupz.basicode.ui;

import de.haupz.basicode.io.BasicOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

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
            g2.setColor(Color.YELLOW);
            for (int l = 0; l < LINES; ++l) {
                g2.drawChars(textBuffer[l], 0, COLUMNS, 0, (l + 1) * C_HEIGHT);
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

}
