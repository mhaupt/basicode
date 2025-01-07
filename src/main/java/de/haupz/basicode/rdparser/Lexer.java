package de.haupz.basicode.rdparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static de.haupz.basicode.rdparser.Symbol.*;

public class Lexer {

    /**
     * The input reader. It represents one complete BASIC program.
     */
    private final BufferedReader in;

    /**
     * The number of the line currently being read from the input, starting at 1 for the first line. Note that this is
     * not the BASIC line number.
     */
    private int fileLineNumber = 0;

    /**
     * The current line from the input being lexed.
     */
    private String currentLine = "";

    /**
     * The position of the next character to be read from the current line, starting at 0.
     */
    private int currentCharPos;

    /**
     * The current symbol.
     */
    private Symbol sym = None;

    /**
     * The text corresponding to the next symbol.
     */
    private StringBuilder text = new StringBuilder();

    /**
     * Construct a lexer.
     *
     * @param in the reader from which to lex input.
     */
    public Lexer(Reader in) {
        this.in = new BufferedReader(in);
    }

    /**
     * @return {@code true} if the lexer can produce the next symbol; {@code false} otherwise.
     */
    public boolean hasNextSymbol() {
        return hasMoreInput() && !endOfBuffer();
    }

    /**
     * @return the next symbol from the input.
     */
    public Symbol getSymbol() {
        do {
            if (!hasMoreInput()) {
                sym = None;
                return sym;
            }
            skipWhiteSpace();
        } while (endOfBuffer() || Character.isWhitespace(currentChar()));

        if (Character.isDigit(currentChar()) || ('.' == currentChar() && peekIsDigit())) {
            lexNumerical();
        } else if ('"' == currentChar()) {
            lexString();
        } else if (Character.isLetter(currentChar())) {
            sym = None;
            lexKeyword(); // keywords take precedence
            if (None == sym) {
                lexIdentifier();
            }
        } else {
            sym = None;
        }

        return sym;
    }

    /**
     * Handle a number or floating-point number from the input.
     */
    private void lexNumerical() {
        text = new StringBuilder();
        sym = Number;
        if (Character.isDigit(currentChar())) {
            consumeNumberPart();
        }
        boolean decimalPoint = '.' == currentChar();
        boolean exponent = Character.toUpperCase(currentChar()) == 'E';
        if (decimalPoint || exponent) {
            sym = Float;
            text.append(consumeChar());
            if (decimalPoint) {
                consumeNumberPart();
            }
            exponent = Character.toUpperCase(currentChar()) == 'E';
            if (exponent) {
                text.append(consumeChar());
                if ('-' == currentChar()) {
                    text.append(consumeChar());
                }
                consumeNumberPart();
            }
        }
    }

    /**
     * Lex a plain number consisting solely of digits. This is a helper for the {@link #lexNumerical()} method.
     */
    private void consumeNumberPart() {
        while (Character.isDigit(currentChar())) {
            text.append(consumeChar());
        }
    }

    /**
     * Lex a keyword.
     */
    private void lexKeyword() {

    }

    /**
     * Lex an identifier.
     */
    private void lexIdentifier() {

    }

    /**
     * Handle a string from the input.
     */
    private void lexString() {
        text = new StringBuilder();
        do {
            text.append(consumeChar());
        } while ('"' != currentChar() && '\n' != currentChar() && !endOfBuffer());
        if ('\n' == currentChar() || endOfBuffer()) {
            throw new LexerException("string does not end: " + text.toString());
        }
        text.append(currentChar());
        sym = String;
    }

    /**
     * @return the current text, i.e., the text representing the current symbol.
     */
    public String getText() {
        return text.toString();
    }

    /**
     * @return the current character from the input.
     */
    private char currentChar() {
        return !endOfBuffer() ? currentLine.charAt(currentCharPos) : '\0';
    }

    /**
     * Consume the current character from the input, and advance to the next.
     *
     * @return the current character from the input.
     */
    private char consumeChar() {
        char c = currentChar();
        ++currentCharPos;
        return c;
    }

    /**
     * @return the following {@code n} character from the input buffer; or an empty string if that is not possible. This
     * can be the case at the end of an input line, as tokens do not span multiple lines.
     */
    private String peek(int n) {
        return !endOfBuffer() && currentCharPos <= currentLine.length() - n ?
                currentLine.substring(currentCharPos + 1, currentCharPos + n + 1) :
                "";
    }

    /**
     * Check if the next character from the input is a digit. This is a helper to keep expressions tidy.
     *
     * @return {@code true} iff the next character from the input is a digit.
     */
    private boolean peekIsDigit() {
        String peek = peek(1);
        return peek.isEmpty() ? false : Character.isDigit(peek.charAt(0));
    }

    /**
     * Fill the read buffer, i.e., read the next line from the input, unless no more lines can be read.
     *
     * @return the length of the next read line, or -1 if no more input can be read.
     */
    private int fillBuffer() {
        try {
            if (!in.ready()) {
                return -1;
            }

            currentLine = in.readLine();
            if (currentLine == null) {
                return -1;
            }
            ++fileLineNumber;
            currentCharPos = 0;
            return currentLine.length();
        } catch (IOException ioe) {
            throw new IllegalStateException("Error reading from input: " + ioe.getMessage());
        }
    }

    /**
     * Check if there is more input available to process. The method fills the {@link #currentLine read buffer} if the
     * current line has been fully processed, and determines whether additional input can be read.
     *
     * @return {@code true} if more input is available; {@code false} otherwise.
     */
    private boolean hasMoreInput() {
        while (endOfBuffer()) {
            if (fillBuffer() == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Skip over any whitespace characters in the current input.
     */
    private void skipWhiteSpace() {
        while (Character.isWhitespace(currentChar())) {
            consumeChar();
            while (endOfBuffer()) {
                if (fillBuffer() == -1) {
                    return;
                }
            }
        }
    }

    /**
     * @return {@code true} if the current line has been consumed (or there is no current line); {@code false}
     * otherwise.
     */
    private boolean endOfBuffer() {
        return currentLine == null || currentCharPos >= currentLine.length();
    }

}
