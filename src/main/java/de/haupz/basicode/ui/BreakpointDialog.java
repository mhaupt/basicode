package de.haupz.basicode.ui;

import de.haupz.basicode.interpreter.InterpreterState;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * A dialogue to be shown when BASICODE encounters a breakpoint.
 */
public class BreakpointDialog extends JDialog {

    /**
     * This will contain the exception stack trace.
     */
    private JTextArea text;

    public BreakpointDialog(InterpreterState state) {
        super(state.getFrame(), "Breakpoint", true);

        String stackDump = state.getStackDump(true);
        String values = state.getValues();
        String content = stackDump + "\n" + values;
        text = new JTextArea(content);
        text.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(text);
        sp.setPreferredSize(new Dimension(600, 300));

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> copyToClipboard());

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(copyButton);
        buttons.add(continueButton);

        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(state.getFrame());
    }

    private void copyToClipboard() {
        StringSelection sel = new StringSelection(text.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(sel, null);
    }

}
