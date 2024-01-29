package com.billy.marinemotorsportsmanagement.JComponents.JTextArea;

import javax.swing.*;
import java.awt.*;

public class Scroll extends JScrollPane {
    public Scroll(JTextArea textArea) {
        super(textArea);
        this.setPreferredSize(new Dimension(800, 500));
    }

    public Scroll(JTable textArea) {
        super(textArea);
        this.setPreferredSize(new Dimension(800, 500));
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
