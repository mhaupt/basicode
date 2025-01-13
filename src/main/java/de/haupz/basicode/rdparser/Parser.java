package de.haupz.basicode.rdparser;

import de.haupz.basicode.ast.DataNode;
import de.haupz.basicode.ast.LineNode;
import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.ast.StatementNode;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.haupz.basicode.rdparser.Symbol.*;

/**
 * A parser for BASICODE.
 */
public class Parser {

    /**
     * The lexer supporting this parser.
     */
    private final Lexer lexer;

    /**
     * This list stores all items from {@code DATA} lines, in the order that they appear in in the BASIC source code, as
     * a flat list.
     */
    private List<Object> dataList = new ArrayList<>();

    /**
     * The current symbol.
     */
    private Symbol sym = None;

    /**
     * The current symbol's text.
     */
    private String text = "";

    /**
     * A stored symbol to return first, if any.
     */
    private Optional<Symbol> pendingSymbol = Optional.empty();

    /**
     * The text corresponding to {@link #pendingSymbol}.
     */
    private Optional<String> pendingText = Optional.empty();

    /**
     * Construct a parser for a given input.
     *
     * @param input the input from which to read a BASIC program.
     */
    public Parser(Reader input) {
        lexer = new Lexer(input);
    }

    //
    // symbol handling
    //

    /**
     * Retrieve the next symbol from the lexer. This populates the {@link #sym} and {@link #text} fields with the
     * corresponding values.
     */
    private void getNextSymbol() {
        if (pendingSymbol.isPresent()) {
            sym = pendingSymbol.get();
            text = pendingText.get();
            pendingSymbol = Optional.empty();
            pendingText = Optional.empty();
        } else {
            sym = lexer.getSymbol();
            text = lexer.getText();
        }
    }

    /**
     * Expect the input to next contain the given symbol. Throw an exception if it's not the right one. This advances
     * the input.
     *
     * @param s the expected symbol.
     * @throws ParserException if the expected symbol is not found.
     */
    private void expect(Symbol s) throws ParserException {
        getNextSymbol();
        if (sym != s) {
            throw new ParserException("Expected " + s + " but got " + sym);
        }
    }

    /**
     * Expect the input to next contain one of the given symbols. Throw an exception if that's not the case. This
     * advances the input.
     *
     * @param ss the expected symbols.
     * @throws ParserException if none of the expected symbols are found.
     */
    private void expectOneOf(Symbol... ss) throws ParserException {
        getNextSymbol();
        List<Symbol> lss = List.of(ss);
        if (!lss.contains(sym)) {
            throw new ParserException("Expected one of " + lss + " but got " + sym);
        }
    }

    /**
     * Check if the next symbol is the given one. This advances the input.
     *
     * @param s the symbol to accept.
     * @return {@code true} iff the next symbol is the same as the argument.
     */
    private boolean accept(Symbol s) {
        getNextSymbol();
        if (sym == s) {
            return true;
        }
        // accept, when failing, keeps the symbol around
        pendingSymbol = Optional.of(sym);
        pendingText = Optional.of(text);
        return false;
    }

    //
    // basic structures
    //

    public ProgramNode program() {
        List<LineNode> lines = new ArrayList<>();
        do {
            lines.add(line());
        } while (lexer.hasNextSymbol());
        return new ProgramNode(lines, dataList);
    }

    public LineNode line() {
        List<StatementNode> statements = new ArrayList<>();
        expect(NumberLiteral);
        int num = Integer.parseInt(text);
        if (accept(Data)) {
            statements.add(dataLine());
        } else {
            do {
                statements.add(statement());
            } while (accept(Colon));
        }
        return new LineNode(num, statements);
    }

    //
    // statements
    //

    public StatementNode dataLine() {
        List<Object> data = new ArrayList<>();
        do {
            data.add(dataLiteral());
        } while (accept(Comma));
        return new DataNode(data);
    }

    public Object dataLiteral() {
        if (accept(StringLiteral)) {
            return text;
        }
        int sgn = accept(Minus) ? -1 : 1;
        expectOneOf(NumberLiteral, FloatLiteral);
        return sgn * Double.parseDouble(text);
    }

    public StatementNode statement() { return null; }

}
