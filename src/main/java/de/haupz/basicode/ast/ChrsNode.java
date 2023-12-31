package de.haupz.basicode.ast;

import java.util.Optional;

/**
 * {@code CHR$}.
 */
public class ChrsNode extends WrappingExpressionNode {

    public ChrsNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof Double d) {
            return Optional.of(chrs((char) d.intValue()));
        }
        return Optional.empty();
    }

    private String chrs(char c) {
        if (c < 0 || c > 127) {
            throw new IllegalStateException("out of range: " + (int) c);
        }
        return new String(new char[] {c});
    }

}
