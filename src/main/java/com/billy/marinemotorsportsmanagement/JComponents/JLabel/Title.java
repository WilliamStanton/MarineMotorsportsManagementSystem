package com.billy.marinemotorsportsmanagement.JComponents.JLabel;

import javax.swing.*;
import java.awt.*;

/**
 * The Title class extends JLabel and allows for instantiation of a title
 */
public class Title extends JLabel {
    public Title() {
        super();
        this.setFont(new Font("Segoe UI", Font.BOLD, 34));
        this.setForeground(Color.white);
    }

    public Title(String text) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.BOLD, 34));
        this.setForeground(Color.white);
    }
}
