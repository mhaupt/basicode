package de.haupz.basicode.ui;

import de.haupz.basicode.io.BasicOutput;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class BasicContainer extends JComponent implements BasicOutput {

    public static final int C_WIDTH = 14;

    public static final int C_HEIGHT = 29;

    public static final int COLUMNS = 40;

    public static final int LINES = 25;

    public static final int WIDTH = C_WIDTH * COLUMNS;

    public static final int HEIGHT = C_HEIGHT * LINES;

    public static final Font FONT = new Font("Monospaced", Font.BOLD, 24);

    private final char[][] textBuffer = new char[LINES][COLUMNS];

    private int curLine;

    private int curColumn;

    public BasicContainer() {
        super();
        setSize(WIDTH, HEIGHT);
        clearTextBuffer();
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
        g2.setBackground(Color.BLUE);
        g2.clearRect(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintChildren(g2);
        g2.setFont(FONT);
        g2.setColor(Color.YELLOW);
        for (int l = 0; l < LINES; ++l) {
            g2.drawChars(textBuffer[l], 0, COLUMNS, 0, (l+1) * C_HEIGHT);
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

}
