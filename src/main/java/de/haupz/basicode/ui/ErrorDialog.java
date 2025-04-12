package de.haupz.basicode.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A dialogue to be shown when some part of the BASICODE interpreter throws an exception.
 */
public class ErrorDialog extends JDialog {

    /**
     * This will contain the exception stack trace.
     */
    private JTextArea text;

    public ErrorDialog(Frame owner, Exception ex) {
        super(owner, "Oops!", true);

        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        text = new JTextArea(sw.toString());
        JScrollPane sp = new JScrollPane(text);
        sp.setPreferredSize(new Dimension(600, 300));

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> copyToClipboard());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(copyButton);
        buttons.add(closeButton);

        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void copyToClipboard() {
        StringSelection sel = new StringSelection(text.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(sel, null);
    }

}
