package com.billy.marinemotorsportsmanagement.JComponents.JTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * The Scroll class extends JScrollPane and allows for instantiation of a scrollbar that may hold a JTable or JTextArea
 */
public class Scroll extends JScrollPane {
    public Scroll(JTextArea textArea) {
        super(textArea);
        this.setPreferredSize(new Dimension(800, 500));
    }

    public Scroll(JTable textArea) {
        super(textArea);
        this.setPreferredSize(new Dimension(800, 500));
    }

    public Scroll(JPanel jPanel) {
        super(jPanel);
        this.setPreferredSize(new Dimension(1200, 500));
    }

    public Scroll(JTextArea textArea, int width, int height) {
        super(textArea);
        this.setPreferredSize(new Dimension(width, height));
    }

    public Scroll(JTable textArea, int width, int height) {
        super(textArea);
        this.setPreferredSize(new Dimension(width, height));
    }
}
