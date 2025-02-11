package de.haupz.basicode.grammar;

import de.haupz.basicode.ast.*;
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

    @Test
    public void testTwoCompleteDataLines() {
        Parser p = parse("""
            10 DATA \"Hello, world!\", 23
            20 DATA \"\", -2.3
        """);

        LineNode l1 = p.line();
        assertEquals(10, l1.getLineNumber());

        List<StatementNode> s1 = l1.getStatements();
        assertEquals(1, s1.size());

        List<Object> d1 = ((DataNode) s1.get(0)).getData();
        assertEquals(2, d1.size());
        assertEquals("\"Hello, world!\"", d1.get(0));
        assertEquals(23.0, d1.get(1));

        LineNode l2 = p.line();
        assertEquals(20, l2.getLineNumber());

        List<StatementNode> s2 = l2.getStatements();
        assertEquals(1, s2.size());

        List<Object> d2 = ((DataNode) s2.get(0)).getData();
        assertEquals(2, d2.size());
        assertEquals("\"\"", d2.get(0));
        assertEquals(-2.3, d2.get(1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"wheee", "  wheee", "  wheee  ", ""})
    public void testRem(String rem) {
        Parser p = parse("10 REM " + rem);

        LineNode l = p.line();
        assertEquals(10, l.getLineNumber());

        List<StatementNode> s = l.getStatements();
        assertEquals(1, s.size());

        RemNode r = (RemNode) s.get(0);
        assertEquals(rem.trim(), r.getRem());
    }

    @ParameterizedTest
    @ValueSource(strings = {"A=23", "LET A=23"})
    public void testAssignment(String assignment) {
        Parser p = parse(assignment);
        LetNode l = (LetNode) p.statement();
        LetNode.Variable lhs = (LetNode.Variable) l.getLhs();
        assertEquals("A", lhs.getId());
        assertFalse(lhs.isString());
        DoubleNode d = (DoubleNode) l.getExpression();
        assertEquals(23.0, d.eval(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A$=\"boop\"", "LET A$=\"boop\""})
    public void testStringAssignment(String assignment) {
        Parser p = parse(assignment);
        LetNode l = (LetNode) p.statement();
        LetNode.Variable lhs = (LetNode.Variable) l.getLhs();
        assertEquals("A$", lhs.getId());
        assertTrue(lhs.isString());
        StringNode s = (StringNode) l.getExpression();
        assertEquals("boop", s.eval(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A(1)=23", "A(2,3)=23"})
    public void testArrayAssignment(String assignment) {
        Parser p = parse(assignment);
        LetNode l = (LetNode) p.statement();
        LetNode.Array lhs = (LetNode.Array) l.getLhs();
        assertFalse(lhs.isString());
        VarNode v = lhs.getGetArray();
        assertEquals("A", v.getId());
        assertTrue(v.isArray());
        DoubleNode d1 = (DoubleNode) lhs.getDim1();
        if (null == lhs.getDim2()) {
            assertEquals(1.0, d1.eval(null));
        } else {
            DoubleNode d2 = (DoubleNode) lhs.getDim2();
            assertEquals(2.0, d1.eval(null));
            assertEquals(3.0, d2.eval(null));
        }
        DoubleNode r = (DoubleNode) l.getExpression();
        assertEquals(23.0, r.eval(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"PRINT 1;A,TAB(7);SGN(2);", "PRINT 1;A,TAB(7);SGN(2)"})
    public void testPrint(String ps) {
        Parser p = parse(ps);
        PrintNode pn = (PrintNode) p.statement();

        boolean trailingSemicolon = ps.endsWith(";");

        List<PrintNode.Element> es = pn.getElements();
        assertEquals(trailingSemicolon ? 8 : 7, es.size());

        PrintNode.Element e0 = es.get(0);
        assertEquals(PrintNode.ElementType.EXPRESSION, e0.type());
        DoubleNode d0 = (DoubleNode) e0.payload();
        assertEquals(1.0, d0.eval(null));

        PrintNode.Element e1 = es.get(1);
        assertEquals(PrintNode.ElementType.SEPARATOR, e1.type());
        String s1 = (String) e1.payload();
        assertEquals(";", s1);

        PrintNode.Element e2 = es.get(2);
        assertEquals(PrintNode.ElementType.EXPRESSION, e2.type());
        VarNode v2 = (VarNode) e2.payload();
        assertEquals("A", v2.getId());

        PrintNode.Element e3 = es.get(3);
        assertEquals(PrintNode.ElementType.SEPARATOR, e3.type());
        String s3 = (String) e3.payload();
        assertEquals(",", s3);

        PrintNode.Element e4 = es.get(4);
        assertEquals(PrintNode.ElementType.TAB, e4.type());
        DoubleNode d4 = (DoubleNode) e4.payload();
        assertEquals(7.0, d4.eval(null));

        PrintNode.Element e5 = es.get(5);
        assertEquals(PrintNode.ElementType.SEPARATOR, e5.type());
        String s5 = (String) e5.payload();
        assertEquals(";", s5);

        PrintNode.Element e6 = es.get(6);
        assertEquals(PrintNode.ElementType.EXPRESSION, e6.type());
        SgnNode sgn = (SgnNode) e6.payload();
        DoubleNode d6 = (DoubleNode) sgn.getExpression();
        assertEquals(2.0, d6.eval(null));

        if (trailingSemicolon) {
            PrintNode.Element e7 = es.get(7);
            assertEquals(PrintNode.ElementType.SEPARATOR, e7.type());
            String s7 = (String) e7.payload();
            assertEquals(";", s7);
        }
    }

    @Test
    public void testGoto() {
        Parser p = parse("10 GOTO 20");
        LineNode l = p.line();
        GotoNode g = (GotoNode) l.getStatements().get(0);
        assertEquals(20, g.getTarget());
    }

    @Test
    public void testGosub() {
        Parser p = parse("10 GOSUB 20");
        LineNode l = p.line();
        GosubNode g = (GosubNode) l.getStatements().get(0);
        assertEquals(20, g.getTarget());
    }

    @Test
    public void testReturn() {
        Parser p = parse("10 RETURN");
        LineNode l = p.line();
        assertInstanceOf(ReturnNode.class, l.getStatements().get(0));
    }

    @Test
    public void testMultipleStatements() {
        Parser p = parse("10 PRINT 1:GOTO 20:RETURN");
        LineNode l = p.line();
        List<StatementNode> s = l.getStatements();
        assertEquals(3, s.size());
        assertInstanceOf(PrintNode.class, s.get(0));
        assertInstanceOf(GotoNode.class, s.get(1));
        assertInstanceOf(ReturnNode.class, s.get(2));
    }

    @Test
    public void testOnGosub() {
        Parser p = parse("10 ON X GOSUB 3,4,5");
        LineNode l = p.line();
        OnGosubNode g = (OnGosubNode) l.getStatements().get(0);

        VarNode v = (VarNode) g.getExpression();
        assertEquals("X", v.getId());

        List<Integer> targets = g.getTargets();
        assertEquals(3, targets.size());
        assertEquals(3, (int) targets.get(0));
        assertEquals(4, (int) targets.get(1));
        assertEquals(5, (int) targets.get(2));
    }

    @Test
    public void testOnGoto() {
        Parser p = parse("10 ON X GOTO 3,4,5");
        LineNode l = p.line();
        OnGotoNode g = (OnGotoNode) l.getStatements().get(0);

        VarNode v = (VarNode) g.getExpression();
        assertEquals("X", v.getId());

        List<Integer> targets = g.getTargets();
        assertEquals(3, targets.size());
        assertEquals(3, (int) targets.get(0));
        assertEquals(4, (int) targets.get(1));
        assertEquals(5, (int) targets.get(2));
    }

}
