package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * <p>{@code PRINT}.</p>
 *
 * <p>Internally, the arguments to a {@code PRINT} statements are represented as a list of {@link Element} tuples. Each
 * of these has a {@link ElementType} type and possibly carries a payload.</p>
 *
 * <p>The element types are as follows:<ul>
 *     <li>{@code EXPRESSION}: a plain old variable, string, number, expression, whatever.</li>
 *     <li>{@code TAB}: a {@code TAB} expression, moving the text cursor to a specific place on the output line.</li>
 *     <li>{@code SEPARATOR}: a semicolon or comma, indicating whether the cursor should stay on the same line after
 *     printing the previous element, or whether it should advance to the next line.</li>
 * </ul></p>
 */
public class PrintNode extends StatementNode {

    public static final DecimalFormat DECIMAL_FORMAT =
            new DecimalFormat(" #.######### ;-#.######### ", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public enum ElementType { EXPRESSION, TAB, SEPARATOR; }

    public record Element(ElementType type, Object payload) {}

    private static final Element SEP_NO_NEWLINE = new Element(ElementType.SEPARATOR, ";");

    private final List<Element> elements;

    public PrintNode(List<Element> elements) {
        this.elements = List.copyOf(elements);
    }

    @Override
    public void run(InterpreterState state) {
        if (elements.isEmpty()) {
            state.getOutput().println();
            return;
        }
        for (Element e : elements) {
            switch (e.type) {
                case EXPRESSION -> {
                    Object v = ((ExpressionNode) e.payload).eval(state);
                    String s = v instanceof Double d ? DECIMAL_FORMAT.format(d) : v.toString();
                    state.getOutput().print(s);
                }
                case TAB -> {
                    int tab = ((Number) ((ExpressionNode) e.payload).eval(state)).intValue();
                    int spacesToPrint = tab - state.getOutput().getTextCursor().col();
                    if (spacesToPrint < 0) {
                        state.getOutput().println();
                        state.getOutput().print(String.format("%" + tab + "s", ""));
                    } else if (spacesToPrint > 0) {
                        state.getOutput().print(String.format("%" + spacesToPrint + "s", ""));
                    }
                    // no need to print anything for spacesToPrint == 0
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
