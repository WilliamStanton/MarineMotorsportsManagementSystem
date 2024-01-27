package com.billy.marinemotorsportsmanagement.JComponents.JButton;

import javax.swing.*;
import java.awt.*;

public class ButtonArray extends JButton {
    public ButtonArray(String text) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        this.setFocusPainted(false);
    }
}
