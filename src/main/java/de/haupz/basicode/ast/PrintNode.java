package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class PrintNode extends StatementNode {

    private static final DecimalFormat DECIMAL_FORMAT =
            new DecimalFormat("#.#########", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    private final List<ExpressionNode> expressions;

    private final List<String> separators;

    public PrintNode(List<ExpressionNode> expressions, List<String> separators) {
        this.expressions = List.copyOf(expressions);
        this.separators = List.copyOf(separators);
    }

    @Override
    public void run(InterpreterState state) {
        for (int i = 0; i < expressions.size(); ++i) {
            Object v = expressions.get(i).eval(state);
            if (v instanceof Double d) {
                state.getOutput().print(DECIMAL_FORMAT.format(d));
            } else {
                state.getOutput().print(v);
            }
            if (",".equals(separators.get(i))) {
                state.getOutput().println();
            }
        }
    }

}
