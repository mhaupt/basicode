package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PrintNode extends StatementNode {

    private static final DecimalFormat DECIMAL_FORMAT =
            new DecimalFormat("#.#########", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    private final ExpressionNode value;

    public PrintNode(ExpressionNode value) {
        this.value = value;
    }

    @Override
    public void run(InterpreterState state) {
        Object v = value.eval(state);
        if (v instanceof Double d) {
            state.getOutput().println(DECIMAL_FORMAT.format(d));
        } else {
            state.getOutput().println(v);
        }
    }

}
