package com.billy.marinemotorsportsmanagement.JComponents.JLabel;

import javax.swing.*;
import java.awt.*;

/**
 * The Description class extends JLabel and allows for instantiation of a description label
 */
public class Description extends JLabel {
    public Description() {
        super();
        this.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        this.setForeground(Color.white);
    }

    public Description(String text) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        this.setForeground(Color.white);
    }

    public Description(String text, boolean center) {
        super(text, SwingConstants.CENTER);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        this.setForeground(Color.white);
    }
}
