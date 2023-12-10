package de.haupz.basicode.ui;

import javax.swing.*;
import java.awt.*;

public class BasicFrame extends JFrame {

    private final BasicContainer bc;

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
