package de.haupz.basicode.ast;

/**
 * A common superclass for the {@code LEFT$}, {@code MID$}, and {@code RIGHT$} operations that contains some shared
 * functionality used by the three.
 */
public abstract class SubstringNode extends ExpressionNode {

    /**
     * An expression yielding the string from which to extract a substring.
     */
    protected final ExpressionNode expression1;

    /**
     * An expression to yield the first numeric argument to the substring expression. This will be the only argument in
     * {@code LEFT$} and {@code RIGHT$}.
     */
    protected final ExpressionNode expression2;

    public SubstringNode(ExpressionNode expression1, ExpressionNode expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    /**
     * @return the name of the BASIC function represented by this node.
     */
    private String opName() {
        return switch (this.getClass().getName()) {
            case "LeftsNode" -> "LEFT$";
            case "MidsNode" -> "MID$";
            case "RightsNode" -> "RIGHT$";
            default -> "undefined";
        };
    }

    /**
     * Helper method to convert an argument to an integral number that can be used as a string position. In case the
     * conversion fails, throw an expression with a message helpful for debugging.
     *
     * @param arg the argument to convert to {@code int}.
     * @param pos the position of the argument.
     * @return the argument, converted to {@code int}.
     */
    protected int argToInt(Object arg, String pos) {
        if (arg instanceof Double d) {
            return d.intValue();
        } else {
            throw new IllegalStateException(opName() + " expects number as " + pos + " argument");
        }
    }

    /**
     * Helper method to convert an argument to a string. In case the conversion fails, throw an exception with a message
     * helpful for debugging.
     *
     * @param arg the argument to convert to a string.
     * @return the argument, converted to a string.
     */
    protected String argToString(Object arg) {
        if (arg instanceof String s) {
            return s;
        } else {
            throw new IllegalStateException(opName() + " expects string as first argument");
        }
    }

}
