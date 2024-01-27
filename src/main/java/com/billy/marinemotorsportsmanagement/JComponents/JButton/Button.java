package com.billy.marinemotorsportsmanagement.JComponents.JButton;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    public Button(String text) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.BOLD, 28));
        this.setFocusPainted(false);
    }
}
