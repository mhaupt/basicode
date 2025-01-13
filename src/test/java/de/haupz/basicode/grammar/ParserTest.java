package de.haupz.basicode.grammar;

import de.haupz.basicode.ast.DataNode;
import de.haupz.basicode.ast.LineNode;
import de.haupz.basicode.ast.StatementNode;
import de.haupz.basicode.rdparser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private Parser parse(String input) {
        return new Parser(new StringReader(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"Hello, world!\"", "\"\""})
    public void testDataLiteralString(String s) {
        Parser p = parse(s);
        Object d = p.dataLiteral();
        assertInstanceOf(String.class, d);
        assertEquals(s, d.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2.3", "23", "1E23", "1E-23", "1.2e3"})
    public void testDataLiteralFloat(String s) {
        Parser p = parse(s);
        Object d = p.dataLiteral();
        assertInstanceOf(Double.class, d);
        assertEquals(Double.parseDouble(s), ((Double) d).doubleValue(), 0);

        String t = "-" + s;
        Parser q = parse(t);
        Object e = q.dataLiteral();
        assertInstanceOf(Double.class, e);
        assertEquals(Double.parseDouble(t), ((Double) e).doubleValue(), 0);
    }

    @Test
    public void testDataLine() {
        Parser p = parse("\"Hello, world!\", 23, \"\", -2.3");
        DataNode d = (DataNode) p.dataLine();
        List<Object> l = d.getData();
        assertEquals(4, l.size());
        assertEquals("\"Hello, world!\"", l.get(0));
        assertEquals(23.0, l.get(1));
        assertEquals("\"\"", l.get(2));
        assertEquals(-2.3, l.get(3));
    }

    @Test
    public void testCompleteDataLine() {
        Parser p = parse("10 DATA \"Hello, world!\", 23, \"\", -2.3");

        LineNode l = p.line();
        assertEquals(10, l.getLineNumber());

        List<StatementNode> s = l.getStatements();
        assertEquals(1, s.size());

        List<Object> d = ((DataNode) s.get(0)).getData();
        assertEquals(4, d.size());
        assertEquals("\"Hello, world!\"", d.get(0));
        assertEquals(23.0, d.get(1));
        assertEquals("\"\"", d.get(2));
        assertEquals(-2.3, d.get(3));
    }

}
