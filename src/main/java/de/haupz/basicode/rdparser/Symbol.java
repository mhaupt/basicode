package de.haupz.basicode.rdparser;

/**
 * A lexical symbol for the BASIC grammar.
 */
public enum Symbol {
    None,
    Number,
    Float,
    String;

    /**
     * The textual representation of a symbol, if it has one. If it doesn't, the string is empty.
     */
    final String text;

    Symbol() {
        text = "";
    }

    Symbol(String text) {
        this.text = text;
    }
}
