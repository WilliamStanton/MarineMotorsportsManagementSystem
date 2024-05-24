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
        this.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
        this.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 20));
    }

    public Scroll(JTable textArea) {
        super(textArea);
        this.setPreferredSize(new Dimension(800, 500));
        this.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
        this.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 20));
    }

    public Scroll(JPanel jPanel) {
        super(jPanel);
        this.setPreferredSize(new Dimension(1200, 500));
        this.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
        this.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 20));
    }

    public Scroll(JTextArea textArea, int width, int height) {
        super(textArea);
        this.setPreferredSize(new Dimension(width, height));
        this.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
        this.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 20));
    }

    public Scroll(JTable textArea, int width, int height) {
        super(textArea);
        this.setPreferredSize(new Dimension(width, height));
        this.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
        this.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 20));
    }
}
