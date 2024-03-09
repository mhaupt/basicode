package de.haupz.basicode.ui;

import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static de.haupz.basicode.io.ConsoleConfiguration.*;

/**
 * <p>The {@code BasicContainer} is the actual GUI of a running BASICODE interpreter. By virtue of implementing the
 * {@link BasicOutput} and {@link BasicInput} interfaces, it is capable of displaying content in text and graphics mode,
 * and of handling keyboard input.</p>
 *
 * <p>The class is currently what is commonly referred to as a "big ball of mud", or "God class", in that it clumps
 * together all of the aforementioned features without regard for separation of concerns. It's up for a refactoring.</p>
 */
public class BasicContainer extends JComponent implements BasicInput, BasicOutput {

    /**
     * In text mode, the text buffer contains all content that is visible on screen.
     */
    private final TextBuffer textBuffer = new TextBuffer(LINES, COLUMNS);

    /**
     * If {@code true}, the GUI is in graphics mode. If {@code false}, it's in text mode.
     */
    private boolean isGraphicsMode = false;

    /**
     * The representation of the display in graphics mode.
     */
    private final BufferedImage image;

    /**
     * A thread to take care of key events.
     */
    private KeyThread keyThread;

    /**
     * Key events are stored in this queue.
     */
    private BlockingQueue<KeyPress> keyEvents = new LinkedBlockingQueue<>();

    /**
     * The background colour for both text and graphics mode.
     */
    private Color backgroundColour = COLOR_MAP[1]; // initially, blue

    /**
     * The foreground colour for both text and graphics mode.
     */
    private Color foregroundColour = COLOR_MAP[6]; // initially, yellow

    /**
     * This is {@code true} while the interpreter is running and ready to process key events. During shutdown, this is
     * set to {@code false}, to allow an orderly termination of the key events processing thread.
     */
    private volatile boolean collectingKeyEvents;

    /**
     * The character used to represent a newline on the host OS.
     */
    public static final char HOST_NEWLINE = 10;

    /**
     * The character used to represent a newline in BASICODE.
     */
    public static final char BASICODE_NEWLINE = 13;

    /**
     * If this is {@code true}, the interpreter can be terminated by pressing the stop key. See
     * {@link de.haupz.basicode.subroutines.Subroutines#gosub280(InterpreterState)}}.
     */
    private boolean acceptStopKey = true;

    /**
     * A handler for when the stop key is pressed.
     */
    private StopKeyHandler stopKeyHandler;

    /**
     * The cursor in graphics mode.
     */
    private GraphicsCursor graphicsCursor = new GraphicsCursor(0.0, 0.0);

    /**
     * Construct a {@code BasicContainer}, and enable key event processing.
     */
    public BasicContainer() {
        super();
        setSize(ConsoleConfiguration.WIDTH, ConsoleConfiguration.HEIGHT);
        textBuffer.clear();
        image = new BufferedImage(ConsoleConfiguration.WIDTH, ConsoleConfiguration.HEIGHT, BufferedImage.TYPE_INT_RGB);
        keyThread = new KeyThread();
        collectingKeyEvents = true;
        keyThread.start();
        addKeyListener(makeKeyListener());
    }

    /**
     * Shut down the GUI by ending the processing of key events.
     */
    public void shutdown() {
        collectingKeyEvents = false;
        keyThread.interrupt();
    }

    /**
     * Paint the BASICODE GUI component. In text mode, this also clears the screen.
     *
     * @param g the {@code Graphics} object to use for painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        if (!isGraphicsMode) {
            g2.setBackground(backgroundColour);
            g2.clearRect(0, 0, ConsoleConfiguration.WIDTH, ConsoleConfiguration.HEIGHT);
        }
    }

    /**
     * Depending on whether BASICODE is in graphics mode or text mode, draw the current GUI contents. In text mode, the
     * entire text output will be repainted.
     *
     * @param g the {@code Graphics} context in which to paint
     */
    @Override
    public void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintChildren(g2);
        if (isGraphicsMode) {
            g2.drawImage(image, 0, 0, ConsoleConfiguration.WIDTH, ConsoleConfiguration.HEIGHT, null);
        } else {
            g2.setFont(FONT);
            for (int l = 0; l < textBuffer.getLines(); ++l) {
                boolean reverseMode = false;
                int c = 0;
                while (c < textBuffer.getColumns()) {
                    int start = c;
                    int end = start;
                    while (end < textBuffer.getColumns() && textBuffer.isReverseAt(l, end) == reverseMode) {
                        ++end;
                    }
                    if (end > start) {
                        if (reverseMode) {
                            g2.setColor(foregroundColour);
                            g2.fillRect(start * C_WIDTH, l * C_HEIGHT, (end - start) * C_WIDTH, C_HEIGHT);
                            g2.setColor(backgroundColour);
                            g2.drawChars(textBuffer.getLine(l), start, end - start, start * C_WIDTH, (l + 1) * C_HEIGHT);
                        } else {
                            g2.setColor(foregroundColour);
                            g2.drawChars(textBuffer.getLine(l), start, end - start, start * C_WIDTH, (l + 1) * C_HEIGHT);
                        }
                    }
                    c = end;
                    reverseMode = !reverseMode;
                }
            }
        }
    }

    /**
     * Print a string in text mode.
     *
     * @param s the string to print.
     */
    @Override
    public void print(String s) {
        textBuffer.writeString(s, false);
        repaint();
    }

    /**
     * Print a string in text mode, in reverse mode.
     *
     * @param s the string to print in reverse mode.
     */
    @Override
    public void printReverse(String s) {
        textBuffer.writeString(s, true);
        repaint();
    }

    /**
     * In text mode, print a string, followed by a newline.
     *
     * @param s the string to print.
     */
    @Override
    public void println(String s) {
        print(s);
        println();
    }

    /**
     * In text mode, print a newline. This may trigger scrolling if the text cursor advances past the maximum number of
     * lines that can be displayed.
     */
    @Override
    public void println() {
        textBuffer.lineFeed();
        repaint();
    }

    /**
     * Repaint the GUI.
     */
    @Override
    public void flush() {
        repaint();
    }

    /**
     * Switch the BASICODE GUI to text mode, and clear the display.
     */
    @Override
    public void textMode() {
        isGraphicsMode = false;
        textBuffer.clear();
        repaint();
    }

    /**
     * Set the text cursor to the given coordinates.
     *
     * @param ho the horizontal coordinate, starting at 0.
     * @param ve the vertical coordinate, starting at 0.
     */
    @Override
    public void setTextCursor(int ho, int ve) {
        textBuffer.setCursor(ho, ve);
    }

    /**
     * @return the current text cursor coordinates.
     */
    @Override
    public TextCursor getTextCursor() {
        return new TextCursor(textBuffer.getColumn(), textBuffer.getLine());
    }

    /**
     * Switch the BASICODE GUI to graphics mode, and clear the display.
     */
    @Override
    public void graphicsMode() {
        isGraphicsMode = true;
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setBackground(backgroundColour);
        g2.clearRect(0, 0, ConsoleConfiguration.WIDTH, ConsoleConfiguration.HEIGHT);
        repaint();
    }

    /**
     * @return the graphics content in graphics mode.
     */
    @Override
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return the font used to display text in both text and graphics mode.
     */
    @Override
    public Font getFont() {
        return FONT;
    }

    /**
     * Read a single line of text. This is a blocking operation.
     *
     * @return the line of text that was entered.
     * @throws IOException in case anything goes wrong.
     */
    @Override
    public String readLine() throws IOException {
        String s = "";
        char c;
        do {
            c = (char) readChar();
            if (c != BASICODE_NEWLINE) {
                print("" + c);
                s += c;
            } else {
                println();
            }
        } while (c != BASICODE_NEWLINE);
        return s;
    }

    /**
     * Lock object to process key events between threads.
     */
    private final Object keyLock = new Object();

    /**
     * <p>The {@code KeyThread} is a simple thread that processes key events. In AWT and Swing, this needs to happen in
     * a thread that's separate from the "main" or event processing thread, as it would otherwise block execution.</p>
     *
     * <p>The key events processing thread stores any key event in the {@link #keyEvents} queue, from which they can be
     * consumed.</p>
     */
    class KeyThread extends Thread {
        @Override
        public void run() {
            while (collectingKeyEvents) {
                try {
                    synchronized (keyLock) {
                        while (keyEvents.isEmpty()) {
                            keyLock.wait();
                        }
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    /**
     * <p>Transform an AWT {@link KeyEvent} to a BASICODE {@link KeyPress}, mapping Java key characters to BASICODE
     * codes in the process.</p>
     *
     * <p>The BASICODE conventions require some codes that are normally different on machines this interpreter will run
     * on. This method takes care of that.</p>
     *
     * @param e a key event to map.
     * @return the mapped key event.
     */
    private KeyPress mapKey(KeyEvent e) {
        char c = switch (e.getKeyChar()) {
            case HOST_NEWLINE -> BASICODE_NEWLINE;
            default -> e.getKeyChar();
        };
        System.err.printf("code %d char %d, [%c]\n", e.getKeyCode(), (int) e.getKeyChar(), e.getKeyChar());
        return new KeyPress(e.getKeyCode(), c);
    }

    /**
     * Map a non-character key to a BASICODE character.
     *
     * @param code the key code corresponding to the {@link KeyPress} event being mapped.
     * @return the BASICODE character corresponding to the code.
     */
    private char mapFnKey(int code) {
        return (char) switch (code) {
            case KeyEvent.VK_LEFT -> 28;
            case KeyEvent.VK_RIGHT -> 29;
            case KeyEvent.VK_DOWN -> 30;
            case KeyEvent.VK_UP -> 31;
            default -> 0;
        };
    }

    /**
     * @return a key listener to be registered in the GUI component. The listener takes care of filling the
     * {@link #keyEvents} queue, and also notifies the {@link #stopKeyHandler} in case the stop key (here: escape) was
     * pressed.
     */
    public KeyListener makeKeyListener() {
        return new KeyAdapter() {
            private void finishHandling() {
                if (sleepingThread != null) {
                    sleepingThread.interrupt();
                }
                keyLock.notify();
            }
            @Override
            public void keyPressed(KeyEvent e) {
                synchronized (keyLock) {
                    // Process this only if it's a "function" (non-character) key.
                    if (e.getKeyChar() == 65535) {
                        char c = mapFnKey(e.getKeyCode());
                        if (c != 0) {
                            System.err.printf("code %d char %d, [%c]\n", e.getKeyCode(), (int) e.getKeyChar(), e.getKeyChar());
                            keyEvents.add(new KeyPress(e.getKeyCode(), c));
                            finishHandling();
                        }
                    }
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
                synchronized (keyLock) {
                    keyEvents.add(mapKey(e));
                    if (acceptStopKey && e.getKeyChar() == 27) {
                        stopKeyHandler.stopKeyPressed();
                    }
                    finishHandling();
                }
            }
        };
    }

    /**
     * Read a single character. This is a blocking operation.
     *
     * @return the character that was entered.
     * @throws IOException in case anything goes wrong.
     */
    @Override
    public int readChar() throws IOException {
        keyEvents.clear();
        try {
            return keyEvents.take().character();
        } catch (InterruptedException ie) {
            throw new IllegalStateException("could not consume key event", ie);
        }
    }

    /**
     * @return the character from the last key that was pressed, if any; 0 otherwise.
     */
    @Override
    public int lastChar() {
        synchronized (keyLock) {
            if (!keyEvents.isEmpty()) {
                return keyEvents.poll().character();
            }
            return 0;
        }
    }

    /**
     * Helper: ensure a colour number is in the allowed range.
     *
     * @param name the name of the colour (foreground, background), for debugging purposes.
     * @param c the colour code to test.
     */
    private void ensureColourRange(String name, int c) {
        if (c < 0 || c >= N_COLORS) {
            throw new IllegalStateException(name + " colour out of range: " + c);
        }
    }

    /**
     * Set the foregreound and background colours for text or graphics mode.
     *
     * @param fg the foreground colour.
     * @param bg the background colour.
     */
    @Override
    public void setColours(int fg, int bg) {
        ensureColourRange("foreground", fg);
        ensureColourRange("background", bg);
        foregroundColour = COLOR_MAP[fg];
        backgroundColour = COLOR_MAP[bg];
    }

    /**
     * @return the background colour.
     */
    @Override
    public Color getBackgroundColour() {
        return backgroundColour;
    }

    /**
     * @return the foreground colour.
     */
    @Override
    public Color getForegroundColour() {
        return foregroundColour;
    }

    /**
     * @param ho the horizontal position.
     * @param ve the vertical position.
     * @return the character at the indicated position in text mode.
     */
    @Override
    public char getCharAt(int ho, int ve) {
        return textBuffer.getCharAt(ho, ve);
    }

    /**
     * Used to remember a thread that needs to be interrupted when a key is pressed.
     */
    private Thread sleepingThread;

    /**
     * Instruct the input to note that a waiting operation is under way, and that the input can interrupt this if a key
     * is pressed.
     *
     * @param ready if {@code true}, input will be ready to interrupt a waiting operation; if {@code false}, it won't.
     */
    @Override
    public void setReadyToInterrupt(boolean ready) {
        sleepingThread = ready ? Thread.currentThread() : null;
    }

    /**
     * Control whether pressing a stop key (e.g., ESC) will terminate program execution.
     *
     * @param acceptStopKey if {@code trye}, the stop key will terminate program execution; if {@code false}, it won't.
     */
    @Override
    public void toggleAcceptStopKey(boolean acceptStopKey) {
        this.acceptStopKey = acceptStopKey;
    }

    /**
     * Register a stop key handler for the GUI.
     *
     * @param stopKeyHandler the handler.
     */
    @Override
    public void registerStopKeyHandler(StopKeyHandler stopKeyHandler) {
        this.stopKeyHandler = stopKeyHandler;
    }

    /**
     * Set the coordinates of the internal graphics cursor.
     *
     * @param h the horizontal coordinate.
     * @param v the vertical coordinate.
     */
    @Override
    public void setGraphicsCursor(double h, double v) {
        graphicsCursor = new GraphicsCursor(h, v);
    }

    /**
     * @return the current position of the internal graphics cursor.
     */
    @Override
    public GraphicsCursor getGraphicsCursor() {
        return graphicsCursor;
    }

}
