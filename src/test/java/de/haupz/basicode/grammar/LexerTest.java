package de.haupz.basicode.grammar;

import de.haupz.basicode.rdparser.Lexer;
import de.haupz.basicode.rdparser.LexerException;
import de.haupz.basicode.rdparser.Symbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static de.haupz.basicode.rdparser.Symbol.*;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    private Lexer lex(String input) {
        return new Lexer(new StringReader(input));
    }

    @Test
    public void testEmpty() {
        Lexer lexer = lex("");
        assertFalse(lexer.hasNextSymbol());
        assertEquals(None, lexer.getSymbol());
        assertEquals("", lexer.getText());
    }

    @Test
    public void testNumber() {
        Lexer lexer = lex("2342");
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
    }

    @Test
    public void testNumberWhitespace() {
        Lexer lexer = lex("  2342 ");
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
    }

    @Test
    public void testNumbersSeparatedBySpace() {
        Lexer lexer = lex("2342  4223");
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("4223", lexer.getText());
    }

    @Test
    public void testNumbersSeparatedByNewline() {
        Lexer lexer = lex("""
                2342
                4223
                """);
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("4223", lexer.getText());
    }

    @Test
    public void testNoneAfterSymbol() {
        Lexer lexer = lex("2342");
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(None, lexer.getSymbol());

        lexer = lex("2342  ");
        assertEquals(NumberLiteral, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @Test
    public void testString() {
        Lexer lexer = lex("\"Hello, world!\"");
        assertEquals(StringLiteral, lexer.getSymbol());
        assertEquals("\"Hello, world!\"", lexer.getText());

        lexer = lex("  \"  Hello, world!  \"  ");
        assertEquals(StringLiteral, lexer.getSymbol());
        assertEquals("\"  Hello, world!  \"", lexer.getText());
    }

    @Test
    public void testStringDoesNotEnd() {
        Lexer lexer1 = lex("\"Hello, world!");
        assertThrows(LexerException.class, () -> lexer1.getSymbol(), "string does not end: \"Hello, world!");

        Lexer lexer2 = lex("""
                "Hello,\s
                world!"
                """);
        assertThrows(LexerException.class, () -> lexer2.getSymbol(), "string does not end: \"Hello, ");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.2", "1.2e3", "1.2E3", "1.2e-3", "1.2E-3", ".1", ".1e2", ".1E2", ".1e-2", ".1E-2",
            "1E2", "1e2"})
    public void testFloat(String s) {
        Lexer lexer = lex(s);
        assertEquals(FloatLiteral, lexer.getSymbol());
        assertEquals(s, lexer.getText());
    }

    /**
     * @return a list of all the keywords.
     */
    private static List<Symbol> keywords() {
        return Arrays.stream(Symbol.values()).filter(s -> !s.text.isEmpty()).toList();
    }

    @ParameterizedTest
    @MethodSource("keywords")
    public void testKeyword(Symbol keyword) {
        Lexer lexer = lex(keyword.text);
        assertEquals(keyword, lexer.getSymbol());
        assertEquals(keyword.text, lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @ParameterizedTest
    @MethodSource("keywords")
    public void testKeywordLowerCase(Symbol keyword) {
        Lexer lexer = lex(keyword.text.toLowerCase());
        assertEquals(keyword, lexer.getSymbol());
        assertEquals(keyword.text, lexer.getText().toUpperCase());
        assertEquals(None, lexer.getSymbol());
    }

    @ParameterizedTest
    @MethodSource("keywords")
    public void testKeywordWhitespace(Symbol keyword) {
        Lexer lexer = lex("  " + keyword.text + "  ");
        assertEquals(keyword, lexer.getSymbol());
        if (Rem == keyword) {
            // special case: REM consumes the remainder of the line
            assertEquals(keyword.text + "  ", lexer.getText());
        } else {
            assertEquals(keyword.text, lexer.getText());
        }
        assertEquals(None, lexer.getSymbol());
    }

    @Test
    public void testFnIdentifier() {
        Lexer lexer = lex("FNUP");
        assertEquals(FnIdentifier, lexer.getSymbol());
        assertEquals("FNUP", lexer.getText());
    }

    /**
     * @return a list of some identifiers.
     */
    private static List<String> identifiers() {
        return List.of("A", "AB", "a", "ab", "A7", "a7");
    }

    @ParameterizedTest
    @MethodSource("identifiers")
    public void testIdentifier(String text) {
        Lexer lexer = lex(text);
        assertEquals(Identifier, lexer.getSymbol());
        assertEquals(text, lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @ParameterizedTest
    @MethodSource("identifiers")
    public void testIdentifierString(String text) {
        String id = text + "$";
        Lexer lexer = lex(id);
        assertEquals(Identifier, lexer.getSymbol());
        assertEquals(id, lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @ParameterizedTest
    @MethodSource("identifiers")
    public void testIdentifierWhitespace(String text) {
        Lexer lexer = lex("  " + text + "  ");
        assertEquals(Identifier, lexer.getSymbol());
        assertEquals(text, lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @Test
    public void testColon() {
        Lexer lexer = lex(":");
        assertEquals(Colon, lexer.getSymbol());
        assertEquals(":", lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @Test
    public void testComma() {
        Lexer lexer = lex(",");
        assertEquals(Comma, lexer.getSymbol());
        assertEquals(",", lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @Test
    public void testMinus() {
        Lexer lexer = lex("-");
        assertEquals(Minus, lexer.getSymbol());
        assertEquals("-", lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

}
