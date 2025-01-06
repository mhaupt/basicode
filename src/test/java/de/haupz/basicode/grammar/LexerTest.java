package de.haupz.basicode.grammar;

import de.haupz.basicode.rdparser.Lexer;
import de.haupz.basicode.rdparser.LexerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.StringReader;

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
        assertEquals(Number, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
    }

    @Test
    public void testNumberWhitespace() {
        Lexer lexer = lex("  2342 ");
        assertEquals(Number, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
    }

    @Test
    public void testNumbersSeparatedBySpace() {
        Lexer lexer = lex("2342  4223");
        assertEquals(Number, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(Number, lexer.getSymbol());
        assertEquals("4223", lexer.getText());
    }

    @Test
    public void testNumbersSeparatedByNewline() {
        Lexer lexer = lex("""
                2342
                4223
                """);
        assertEquals(Number, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(Number, lexer.getSymbol());
        assertEquals("4223", lexer.getText());
    }

    @Test
    public void testNoneAfterSymbol() {
        Lexer lexer = lex("2342");
        assertEquals(Number, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(None, lexer.getSymbol());

        lexer = lex("2342  ");
        assertEquals(Number, lexer.getSymbol());
        assertEquals("2342", lexer.getText());
        assertEquals(None, lexer.getSymbol());
    }

    @Test
    public void testString() {
        Lexer lexer = lex("\"Hello, world!\"");
        assertEquals(String, lexer.getSymbol());
        assertEquals("\"Hello, world!\"", lexer.getText());

        lexer = lex("  \"  Hello, world!  \"  ");
        assertEquals(String, lexer.getSymbol());
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
    @ValueSource(strings = {"1.2", "1.2e3", "1.2E3", "1.2e-3", "1.2E-3", ".1", ".1e2", ".1E2", ".1e-2", ".1E-2"})
    public void testFloat(String s) {
        Lexer lexer = lex(s);
        assertEquals(Float, lexer.getSymbol());
        assertEquals(s, lexer.getText());
    }

}
