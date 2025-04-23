package de.haupz.basicode.parser;

public class ParserException extends RuntimeException {

    public ParserException(String message, String text) {
        super(message + "\n" + text);
    }

}
