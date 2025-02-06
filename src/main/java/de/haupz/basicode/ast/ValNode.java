package de.haupz.basicode.ast;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code VAL}.
 */
public class ValNode extends WrappingExpressionNode {

    private static final Pattern DOUBLE = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

    public ValNode(ExpressionNode expression) {
        super(expression);
    }

    @Override
    Optional<Object> evalWithTypes(Object value) {
        if (value instanceof String s) {
            Matcher matcher = DOUBLE.matcher(s);
            return Optional.of(matcher.find() ? Double.parseDouble(matcher.group()) : 0.0);
        }
        return Optional.empty();
    }

}
