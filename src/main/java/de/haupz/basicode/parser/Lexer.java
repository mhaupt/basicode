package de.haupz.basicode.parser;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.haupz.basicode.parser.Symbol.*;

/**
 * A lexer for BASICODE.
 */
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
     * The source code being lexed.
     */
    private String source;

    /**
     * The position of the next character to be read from the source code input, starting at 0.
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
     * The lexer's position on the current line.
     */
    private int currentLinePos = 0;

    /**
     * The start position, on the current line, of the current symbol.
     */
    private int currentSymbolStartPos = 0;

    /**
     * The text of the current line, as it is being lexed.
     */
    private StringBuilder currentLineText = new StringBuilder();

    /**
     * The complete text of the last completely parsed line.
     */
    private String lastLineText = "";

    /**
     * All Symbols representing BASIC keywords.
     */
    private static final List<Symbol> KEYWORD_SYMBOLS =
            Arrays.stream(Symbol.values()).filter(s -> !s.text.isEmpty()).toList();

    /**
     * Construct a lexer.
     *
     * @param in the reader from which to lex input.
     */
    public Lexer(Reader in) {
        this.in = new BufferedReader(in);
        source = readSource();
    }

    /**
     * Read the source code from the {@link #in input}.
     *
     * @return the source code.
     */
    private String readSource() {
        return in.lines().collect(Collectors.joining("\n"));
    }

    /**
     * @return the last completely parsed line.
     */
    public String getLastLineText() {
        return lastLineText;
    }

    /**
     * @return the text of the line currently being parsed, so far.
     */
    public String getLineSoFar() {
        return currentLineText.toString();
    }

    /**
     * @return the start position of the current symbol.
     */
    public int getCurrentSymbolStartPos() {
        return currentSymbolStartPos;
    }

    /**
     * @return the next symbol from the input.
     */
    public Symbol getSymbol() {
        if (!hasMoreInput()) {
            sym = None;
            text.setLength(0);
            lastLineText = currentLineText.toString();
            return sym;
        }
        skipWhiteSpace();
        currentSymbolStartPos = currentLinePos;

        if (0x02 == currentChar()) {
            note(Stx, consumeChar());
        } else if (0x03 == currentChar()) {
            note(Etx, consumeChar());
        } else if (0x1C == currentChar()) {
            note(Fs, consumeChar());
        } else if ('\n' == currentChar() || '\r' == currentChar()) {
            lexEol();
        } else if (Character.isDigit(currentChar()) || ('.' == currentChar() && peekIsDigit())) {
            lexNumerical();
        } else if ('"' == currentChar()) {
            lexString();
        } else if (':' == currentChar()) {
            note(Colon, consumeChar());
        } else if (',' == currentChar()) {
            note(Comma, consumeChar());
        } else if (';' == currentChar()) {
            note(Semicolon, consumeChar());
        } else if ('+' == currentChar()) {
            note(Plus, consumeChar());
        } else if ('-' == currentChar()) {
            note(Minus, consumeChar());
        } else if ('*' == currentChar()) {
            note(Multiply, consumeChar());
        } else if ('/' == currentChar()) {
            note(Divide, consumeChar());
        } else if ('^' == currentChar()) {
            note(Power, consumeChar());
        } else if ('(' == currentChar()) {
            note(LeftBracket, consumeChar());
        } else if (')' == currentChar()) {
            note(RightBracket, consumeChar());
        } else if ('=' == currentChar()) {
            note(Equal, consumeChar());
        } else if ('<' == currentChar()) {
            if (">".equals(peek(1))) {
                note(NotEqual, consume(2));
            } else if ("=".equals(peek(1))) {
                note(LessEqual, consume(2));
            } else {
                note(Less, consumeChar());
            }
        } else if ('>' == currentChar()) {
            if ("=".equals(peek(1))) {
                note(GreaterEqual, consume(2));
            } else {
                note(Greater, consumeChar());
            }
        } else if (Character.isLetter(currentChar())) {
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
     * Helper to note a single character as a lexed symbol.
     *
     * @param s the {@link Symbol} to note.
     * @param c the character representing the symbol.
     */
    private void note(Symbol s, char c) {
        sym = s;
        text = new StringBuilder();
        text.append(c);
    }

    /**
     * Helper to note a string as a lexed symbol.
     *
     * @param s the {@link Symbol} to note.
     * @param v the string representing the symbol.
     */
    private void note(Symbol s, String v) {
        sym = s;
        text = new StringBuilder();
        text.append(v);
    }

    /**
     * Handle an end-of-line sequence.
     */
    private void lexEol() {
        text = new StringBuilder();
        char c = consumeChar();
        text.append(c);
        if (hasMoreInput() && ((c == '\n' && currentChar() == '\r') || (c == '\r' && currentChar() == '\n'))) {
            text.append(consumeChar());
        }
        note(Eol, text.toString());
        lastLineText = currentLineText.toString();
        currentLineText = new StringBuilder();
        currentLinePos = 0;
        ++fileLineNumber;
    }

    /**
     * Handle a number or floating-point number from the input.
     */
    private void lexNumerical() {
        text = new StringBuilder();
        sym = NumberLiteral;
        if (Character.isDigit(currentChar())) {
            consumeNumberPart();
        }
        boolean decimalPoint = '.' == currentChar();
        boolean exponent = Character.toUpperCase(currentChar()) == 'E';
        if (decimalPoint || exponent) {
            sym = FloatLiteral;
            text.append(consumeChar());
            if (decimalPoint) {
                consumeNumberPart();
                exponent = Character.toUpperCase(currentChar()) == 'E';
            }
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
     * Lex a keyword. This includes handling REM lines and FN identifiers.
     */
    private void lexKeyword() {
        sym = KEYWORD_SYMBOLS.stream()
                .filter(ks -> matchKeyword(ks.text))
                .findFirst()
                .orElse(None);
        if (None != sym) {
            text = new StringBuilder();
            text.append(consume(sym.text.length()));
            if (Rem == sym) {
                // special handling: consume until the end of the current line
                int eolPos = source.indexOf('\n', currentCharPos);
                text.append(consume((eolPos > 0 ? eolPos : source.length()) - currentCharPos));
            }
            if (Fn == sym && Character.isLetter(currentChar())) {
                // special handling: we have a FnIndentifier, which consists of one or two more letters
                sym = FnIdentifier;
                text.append(consumeChar());
                if (Character.isLetter(currentChar())) {
                    text.append(consumeChar());
                }
            }
        }
    }

    /**
     * Helper to check whether a given keyword matches the input.
     *
     * @param kwtext the keyword text, in upper case.
     * @return {@code true} iff the input (transformed to upper case) matches the given keyword.
     */
    private boolean matchKeyword(String kwtext) {
        return kwtext.charAt(0) == Character.toUpperCase(currentChar())
                && kwtext.substring(1).equals(peek(kwtext.length() - 1).toUpperCase());
    }

    /**
     * Lex an identifier. This is a letter, possibly followed by a letter or digit, and possibly a '$' at the end.
     */
    private void lexIdentifier() {
        sym = Identifier;
        text = new StringBuilder();
        text.append(consumeChar());
        if (Character.isLetterOrDigit(currentChar())) {
            text.append(consumeChar());
        }
        if ('$' == currentChar()) {
            text.append(consumeChar());
        }
    }

    /**
     * Handle a string from the input.
     */
    private void lexString() {
        text = new StringBuilder();
        do {
            text.append(consumeChar());
        } while ('"' != currentChar() && '\n' != currentChar() && hasMoreInput());
        if ('\n' == currentChar() || !hasMoreInput()) {
            throw new LexerException("string does not end: " + text.toString());
        }
        text.append(consumeChar());
        sym = StringLiteral;
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
        return hasMoreInput() ? source.charAt(currentCharPos) : '\0';
    }

    /**
     * Consume the current character from the input, and advance to the next.
     *
     * @return the current character from the input.
     */
    private char consumeChar() {
        char c = currentChar();
        if ('\n' != c && '\r' != c) {
            currentLineText.append(c);
        }
        ++currentLinePos;
        ++currentCharPos;
        return c;
    }

    /**
     * Consume a number of characters from the input.
     *
     * @param n the number of characters to consume.
     * @return the consumed characters, as a string of length {@code n}.
     */
    private String consume(int n) {
        String r = source.substring(currentCharPos, currentCharPos + n);
        currentLineText.append(r);
        currentLinePos += n;
        currentCharPos += n;
        return r;
    }

    /**
     * @return the following {@code n} character from the input buffer; or an empty string if that is not possible. This
     * can be the case at the end of an input line, as tokens do not span multiple lines.
     */
    private String peek(int n) {
        return currentCharPos + n + 1 <= source.length() ?
                source.substring(currentCharPos + 1, currentCharPos + n + 1) :
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
     * Check if there is more input available to process.
     *
     * @return {@code true} if more input is available; {@code false} otherwise.
     */
    public boolean hasMoreInput() {
        return currentCharPos < source.length();
    }

    /**
     * Skip over any whitespace characters in the current input. This does not include {@code '\n'} and {@code '\r},
     * which are lexed as {@link Symbol#Eol}.
     */
    private void skipWhiteSpace() {
        while (Character.isSpaceChar(currentChar())) {
            consumeChar();
        }
    }

}
