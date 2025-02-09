package de.haupz.basicode.rdparser;

/**
 * A lexical symbol for the BASIC grammar.
 */
public enum Symbol {
    None,
    NumberLiteral,
    FloatLiteral,
    StringLiteral,
    Identifier,
    FnIdentifier,
    Colon,
    Comma,
    Plus,
    Minus,
    Multiply,
    Divide,
    Power,
    LeftBracket,
    RightBracket,
    Equal,
    NotEqual,
    LessEqual,
    GreaterEqual,
    Less,
    Greater,
    // The following are BASIC keywords.
    Abs("ABS"), And("AND"), Asc("ASC"), Atn("ATN"),
    ChrS("CHR$"), Cos("COS"),
    Data("DATA"), Def("DEF"), Dim("DIM"),
    End("END"), Exp("EXP"),
    Fn("FN"), For("FOR"),
    Gosub("GOSUB"), Goto("GOTO"),
    Int("INT"), If("IF"), Input("INPUT"),
    LeftS("LEFT$"), Len("LEN"), Let("LET"), Log("LOG"),
    MidS("MID$"),
    Next("NEXT"), Not("NOT"),
    On("ON"), Or("OR"),
    Print("PRINT"),
    Read("READ"), Rem("REM"), Restore("RESTORE"), Return("RETURN"), RightS("RIGHT$"), Run("RUN"),
    Sgn("SGN"), Sin("SIN"), Sqr("SQR"), Step("STEP"), Stop("STOP"),
    Tab("TAB"), Tan("TAN"), Then("THEN"), To("TO"),
    Val("VAL");

    /**
     * The textual representation of a symbol, if it has one. If it doesn't, the string is empty.
     */
    public final String text;

    Symbol() {
        text = "";
    }

    Symbol(String text) {
        this.text = text;
    }
}
