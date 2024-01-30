package com.billy.marinemotorsportsmanagement.JComponents.JButton;

import javax.swing.*;
import java.awt.*;

/**
 * The Button class extends JButton and allows for instantiation of customized buttons
 */
public class Button extends JButton {
    public Button(String text) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.BOLD, 28));
        this.setFocusPainted(false);
    }
}
