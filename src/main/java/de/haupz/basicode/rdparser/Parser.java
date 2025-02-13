package de.haupz.basicode.rdparser;

import de.haupz.basicode.ast.*;

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
            throw new ParserException("Expected " + s + " but got " + sym + " << " + text + " >>");
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
            throw new ParserException("Expected one of " + lss + " but got " + sym + " << " + text + " >>");
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

    public ProgramNode program() {
        List<LineNode> lines = new ArrayList<>();
        do {
            lines.add(line());
        } while (lexer.hasNextSymbol());
        return new ProgramNode(lines, dataList);
    }

    public LineNode line() {
        List<StatementNode> statements = new ArrayList<>();
        int num = lineNumber();
        if (accept(Data)) {
            StatementNode dataLine = dataLine();
            statements.add(dataLine);
        } else {
            do {
                StatementNode statement = statement();
                statements.add(statement);
            } while (accept(Colon));
        }
        return new LineNode(num, statements);
    }

    private int lineNumber() {
        expect(NumberLiteral);
        return Integer.parseInt(text);
    }

    public StatementNode dataLine() {
        List<Object> data = new ArrayList<>();
        do {
            Object d = dataLiteral();
            dataList.add(d);
            data.add(d);
        } while (accept(Comma));
        return new DataNode(data);
    }

    public Object dataLiteral() {
        if (accept(StringLiteral)) {
            return text.substring(1, text.length() - 1);
        }
        int sgn = accept(Minus) ? -1 : 1;
        expectOneOf(NumberLiteral, FloatLiteral);
        return sgn * Double.parseDouble(text);
    }

    public StatementNode statement() {
        // Here, it's OK to bypass a call to accept(): we know we're going to parse a statement, and we know which
        // starting symbols there can be.
        getNextSymbol();
        Symbol statement = sym;

        StatementNode s = switch (statement) {
            case Def -> defFnStatement();
            case Dim -> dimStatement();
            case End, Stop -> new EndNode();
            case For -> forStatement();
            case Gosub -> {
                int num = lineNumber();
                yield new GosubNode(num);
            }
            case Goto -> {
                int num = lineNumber();
                yield new GotoNode(num);
            }
            case If -> ifStatement();
            case Input -> inputStatement();
            case Let, Identifier -> assignment();
            case Next -> nextStatement();
            case On -> dependentJump();
            case Print -> printStatement();
            case Read -> readStatement();
            case Rem -> new RemNode(text.substring(3).trim()); // text starts with "REM"
            case Restore -> new RestoreNode();
            case Return -> new ReturnNode();
            case Run -> new RunNode();
            default -> throw new ParserException("Expecting statement symbol, but got: " + statement);
        };
        
        return s;
    }

    public StatementNode assignment() {
        LetNode.LHS l = lhs();
        expect(Equal);
        ExpressionNode e = expression();
        return new LetNode(l, e);
    }

    public LetNode.LHS lhs() {
        if (Let == sym) {
            expect(Identifier);
        }
        String id = text;
        if (accept(LeftBracket)) {
            ExpressionNode e = expression();
            ExpressionNode f = null;
            if (accept(Comma)) {
                f = expression();
            }
            expect(RightBracket);
            return new LetNode.Array(id, e, f);
        }
        return new LetNode.Variable(id);
    }

    public ExpressionNode expression() {
        ExpressionNode e = andExpression();
        while (accept(Or)) {
            ExpressionNode f = andExpression();
            e = new OrNode(e, f);
        }
        return e;
    }

    public ExpressionNode andExpression() {
        ExpressionNode e = equalityExpression();
        while (accept(And)) {
            ExpressionNode f = equalityExpression();
            e = new AndNode(e, f);
        }
        return e;
    }

    public ExpressionNode equalityExpression() {
        ExpressionNode e = relationalExpression();
        if (accept(Equal)) {
            ExpressionNode f = relationalExpression();
            return new EqNode(e, f);
        } else if (accept(NotEqual)) {
            ExpressionNode f = relationalExpression();
            return new NeqNode(e, f);
        }
        return e;
    }

    public ExpressionNode relationalExpression() {
        ExpressionNode e = additiveExpression();
        if (accept(Less)) {
            ExpressionNode f = additiveExpression();
            return new LtNode(e, f);
        } else if (accept(LessEqual)) {
            ExpressionNode f = additiveExpression();
            return new LeqNode(e, f);
        } else if (accept(Greater)) {
            ExpressionNode f = additiveExpression();
            return new GtNode(e, f);
        } else if (accept(GreaterEqual)) {
            ExpressionNode f = additiveExpression();
            return new GeqNode(e, f);
        }
        return e;
    }

    public ExpressionNode additiveExpression() {
        ExpressionNode e = multiplicativeExpression();
        while (accept(Plus) || accept(Minus)) {
            Symbol s = sym;
            ExpressionNode f = multiplicativeExpression();
            e = Plus == s ? new AddNode(e, f) : new SubtractNode(e, f);
        }
        return e;
    }

    public ExpressionNode multiplicativeExpression() {
        ExpressionNode e = powerExpression();
        while (accept(Multiply) || accept(Divide)) {
            Symbol s = sym;
            ExpressionNode f = powerExpression();
            e = Multiply == s ? new MultiplyNode(e, f) : new DivideNode(e, f);
        }
        return e;
    }

    public ExpressionNode powerExpression() {
        ExpressionNode e = unaryExpression();
        while (accept(Power)) {
            ExpressionNode f = unaryExpression();
            e = new PowerNode(e, f);
        }
        return e;
    }

    public ExpressionNode unaryExpression() {
        if (accept(Minus)) {
            ExpressionNode e = unaryExpression();
            return new NegateNode(e);
        } else if (accept(Plus)) {
            ExpressionNode e = unaryExpression();
            return e;
        }
        return unaryExpressionNotPlusMinus();
    }

    public ExpressionNode unaryExpressionNotPlusMinus() {
        if (accept(Not)) {
            ExpressionNode e = unaryExpression();
            return new NotNode(e);
        }
        return primaryExpression();
    }

    public ExpressionNode primaryExpression() {
        if (accept(NumberLiteral) || accept(FloatLiteral) || accept(StringLiteral)) {
            return literal();
        } else if (accept(LeftBracket)) {
            ExpressionNode e = expression();
            expect(RightBracket);
            return e;
        } else if (accept(Identifier)) {
            return varOrDimAccess();
        } else if (accept(FnIdentifier) || accept(Fn)) {
            return fnCall();
        }
        return builtinCall();
    }

    public ExpressionNode literal() {
        if (NumberLiteral == sym || FloatLiteral == sym) {
            return new DoubleNode(Double.parseDouble(text));
        } else {
            return new StringNode(text.substring(1, text.length() - 1));
        }
    }

    public ExpressionNode varOrDimAccess() {
        String id = text;
        if (accept(LeftBracket)) {
            ExpressionNode e = expression();
            ExpressionNode f = null;
            if (accept(Comma)) {
                f = expression();
            }
            expect(RightBracket);
            return new DimAccessNode(id, e, f);
        }
        return new VarNode(id, false);
    }

    public ExpressionNode fnCall() {
        String id = fnId();
        expect(LeftBracket);
        ExpressionNode e = expression();
        expect(RightBracket);
        return new FnCallNode(id, e);
    }

    private String fnId() {
        String id;
        if (Fn == sym) {
            expect(Identifier);
            id = text;
        } else { // FnIdentifier
            id = text.substring(2);
        }
        return id;
    }

    public ExpressionNode builtinCall() {
        // Here, it's OK to bypass a call to accept(): at this point, we're certain we're parsing a builtin call, so
        // we'll just get the symbol and proceed with parsing.
        getNextSymbol();
        Symbol builtin = sym;
        String builtinName = text;
        
        expect(LeftBracket);
        ExpressionNode e = expression();
        ExpressionNode n = switch (builtin) {
            case Abs -> new AbsNode(e);
            case Asc -> new AscNode(e);
            case Atn -> new AtnNode(e);
            case ChrS -> new ChrsNode(e);
            case Cos -> new CosNode(e);
            case Exp -> new ExpNode(e);
            case Int -> new IntNode(e);
            case LeftS -> {
                expect(Comma);
                ExpressionNode f = expression();
                yield new LeftsNode(e, f);
            }
            case Len -> new LenNode(e);
            case Log -> new LogNode(e);
            case MidS -> {
                expect(Comma);
                ExpressionNode f = expression();
                ExpressionNode g = null;
                if (accept(Comma)) {
                    g = expression();
                }
                yield new MidsNode(e, f, g);
            }
            case RightS -> {
                expect(Comma);
                ExpressionNode f = expression();
                yield new RightsNode(e, f);
            }
            case Sgn -> new SgnNode(e);
            case Sin -> new SinNode(e);
            case Sqr -> new SqrNode(e);
            case Tan -> new TanNode(e);
            case Val -> new ValNode(e);
            default -> throw new ParserException("Unsupported builtin call: " + builtinName);
        };
        
        expect(RightBracket);
        return n;
    }

    public StatementNode printStatement() {
        List<PrintNode.Element> elements = new ArrayList<>();
        if (printElementIsNext()) {
            PrintNode.Element e = printElement();
            elements.add(e);
            while (accept(Comma) || accept(Semicolon)) {
                elements.add(new PrintNode.Element(PrintNode.ElementType.SEPARATOR, text));
                if (printElementIsNext()) {
                    e = printElement();
                    elements.add(e);
                }
            }
        }
        return new PrintNode(elements);
    }

    private static final List<Symbol> EXPRESSION_START_SYMBOLS =
            List.of(
                // grammatical start symbols
                    NumberLiteral, FloatLiteral, StringLiteral, LeftBracket, Identifier, FnIdentifier, Fn, Minus, Plus,
                    Not,
                // builtin calls are also start symbols
                    Abs, Asc, Atn, ChrS, Cos, Exp, Int, LeftS, Len, Log, MidS, RightS, Sgn, Sin, Sqr, Tan, Val);

    private boolean printElementIsNext() {
        // We're implementing a lookahead here, and will check if the next symbol is a start symbol of a PRINT statement
        // element. We can't use accept(), as we must not consume the symbol when peeking.
        getNextSymbol();
        pendingSymbol = Optional.of(sym);
        pendingText = Optional.of(text);
        return EXPRESSION_START_SYMBOLS.contains(sym) || Tab == sym;
    }

    public PrintNode.Element printElement() {
        if (accept(Tab)) {
            expect(LeftBracket);
            ExpressionNode e = expression();
            expect(RightBracket);
            return new PrintNode.Element(PrintNode.ElementType.TAB, e);
        }
        ExpressionNode e = expression();
        return new PrintNode.Element(PrintNode.ElementType.EXPRESSION, e);
    }

    public StatementNode dependentJump() {
        ExpressionNode e = expression();
        boolean isGosub;
        if (accept(Goto)) {
            isGosub = false;
        } else if (accept(Gosub)) {
            isGosub = true;
        } else {
            throw new ParserException("Expecting GOTO or GOSUB, but got: " + sym + " << " + text + " >>");
        }

        List<Integer> targets = new ArrayList<>();
        int n = lineNumber();
        targets.add(n);
        while (accept(Comma)) {
            n = lineNumber();
            targets.add(n);
        }

        return isGosub ? new OnGosubNode(e, targets) : new OnGotoNode(e, targets);
    }

    public StatementNode dimStatement() {
        List<DimCreateNode> dims = new ArrayList<>();
        DimCreateNode d = oneDim();
        dims.add(d);
        while (accept(Comma)) {
            d = oneDim();
            dims.add(d);
        }
        return new DimNode(dims);
    }

    private DimCreateNode oneDim() {
        expect(Identifier);
        String id = text;
        expect(LeftBracket);
        ExpressionNode d1 = expression();
        ExpressionNode d2 = null;
        if (accept(Comma)) {
            d2 = expression();
        }
        expect(RightBracket);
        return new DimCreateNode(id, d1, d2);
    }

    public StatementNode forStatement() {
        expect(Identifier);
        String id = text;
        expect(Equal);
        ExpressionNode e = expression();
        expect(To);
        ExpressionNode f = expression();
        ExpressionNode g = null;
        if (accept(Step)) {
            g = expression();
        }
        return new ForNode(id, e, f, g);
    }

    public StatementNode nextStatement() {
        expect(Identifier);
        String id = text;
        return new NextNode(id);
    }

    public StatementNode ifStatement() {
        ExpressionNode e = expression();
        StatementNode s;
        if (accept(Then)) {
            if (accept(NumberLiteral)) {
                int l = Integer.parseInt(text);
                s = new GotoNode(l);
            } else {
                s = statement();
            }
        } else if (accept(Goto)) {
            int l = lineNumber();
            s = new GotoNode(l);
        } else {
            throw new ParserException("Expecting THEN or GOTO, but got " + sym + " << " + text + " >>");
        }
        return new IfThenNode(e, s);
    }

    public StatementNode readStatement() {
        List<LetNode.LHS> lhss = new ArrayList<>();
        expect(Identifier);
        LetNode.LHS lhs = lhs();
        lhss.add(lhs);
        while (accept(Comma)) {
            expect(Identifier);
            lhs = lhs();
            lhss.add(lhs);
        }
        return new ReadNode(lhss);
    }

    public StatementNode inputStatement() {
        String prompt = null;
        if (accept(StringLiteral)) {
            prompt = text.substring(1, text.length() - 1);
            expect(Semicolon);
        }
        expect(Identifier);
        LetNode.LHS lhs = lhs();
        return new InputNode(prompt, lhs);
    }

    public StatementNode defFnStatement() {
        getNextSymbol();
        String id = fnId();
        expect(LeftBracket);
        expect(Identifier);
        String u = text;
        expect(RightBracket);
        expect(Equal);
        ExpressionNode e = expression();
        return new DefFnNode(id, u, e);
    }

}
