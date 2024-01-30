package com.billy.marinemotorsportsmanagement.JComponents.JButton;

import javax.swing.*;
import java.awt.*;

/**
 * The ButtonArray class extends JButton and allows for instantiation of customized array buttons
 */
public class ButtonArray extends JButton {
    public ButtonArray(String text) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        this.setFocusPainted(false);
    }
}
