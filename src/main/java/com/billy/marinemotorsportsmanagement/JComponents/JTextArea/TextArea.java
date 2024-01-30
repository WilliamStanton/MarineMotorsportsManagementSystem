package com.billy.marinemotorsportsmanagement.JComponents.JTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * The TextArea class extends JTextArea and allows for instantiation of a TextArea that is usually held by Scroll
 */
public class TextArea extends JTextArea {

    public TextArea() {
        super();

        this.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
    }

    public TextArea(int fontSize) {
        super();

        this.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
    }

    public TextArea(String text) {
        super(text);

        this.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
    }

    public TextArea(String text, int fontSize) {
        super(text);

        this.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
    }
}
