package de.haupz.basicode.grammar;

import de.haupz.basicode.rdparser.Lexer;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static de.haupz.basicode.rdparser.Symbol.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

}
