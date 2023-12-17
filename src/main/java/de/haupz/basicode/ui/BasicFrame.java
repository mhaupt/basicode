package de.haupz.basicode.ui;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code BasicFrame} class represents the top-level GUI element of the BASICODE environment. It references a single
 * {@link BasicContainer} and applies some scaffolding around it to display everything in a proper window.
 */
public class BasicFrame extends JFrame {

    /**
     * The actual GUI of a running BASICODE interpreter.
     */
    private final BasicContainer bc;

    /**
     * Construct a BASICODE window.
     *
     * @param bc the BASICODE GUI element to display.
     */
    public BasicFrame(BasicContainer bc) {
        super("BASICODE");
        this.bc = bc;
        Container cp = getContentPane();
        cp.setPreferredSize(bc.getSize());
        cp.add(bc);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(bc.makeKeyListener());
        pack();
    }

}
