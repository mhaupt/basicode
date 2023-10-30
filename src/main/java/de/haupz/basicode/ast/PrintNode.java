package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class PrintNode extends StatementNode {

    private static final DecimalFormat DECIMAL_FORMAT =
            new DecimalFormat("#.#########", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public enum ElementType { EXPRESSION, TAB, SEPARATOR; }

    public record Element(ElementType type, Object payload) {}

    private static final Element SEP_NO_NEWLINE = new Element(ElementType.SEPARATOR, ";");

    private final List<Element> elements;

    public PrintNode(List<Element> elements) {
        this.elements = List.copyOf(elements);
    }

    @Override
    public void run(InterpreterState state) {
        for (Element e : elements) {
            switch (e.type) {
                case EXPRESSION -> {
                    Object v = ((ExpressionNode) e.payload).eval(state);
                    if (v instanceof Double d) {
                        state.getOutput().print(DECIMAL_FORMAT.format(d));
                    } else {
                        state.getOutput().print(v);
                    }
                }
                case TAB -> {
                    Integer t = ((Number) ((ExpressionNode) e.payload).eval(state)).intValue();
                    state.getOutput().printf("<TAB%d>", t);
                }
                case SEPARATOR -> {
                    String sep = (String) e.payload;
                    if (",".equals(sep)) {
                        state.getOutput().println();
                    }
                }
            }
        }
        // if the final element wasn't a ";" separator, we still need to print a new line
        Element last = elements.get(elements.size() - 1);
        if (!SEP_NO_NEWLINE.equals(last)) {
            state.getOutput().println();
        }
    }

}
