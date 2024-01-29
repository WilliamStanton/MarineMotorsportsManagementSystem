package com.billy.marinemotorsportsmanagement.JComponents.JLabel;

import javax.swing.*;
import java.awt.*;

public class Field extends JLabel {
    public Field() {
        super();
        this.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        this.setForeground(Color.white);
    }

    public Field(int fontSize) {
        super();
        this.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        this.setForeground(Color.white);
    }

    public Field(String text) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        this.setForeground(Color.white);
    }

    public Field(String text, int fontSize) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        this.setForeground(Color.white);
    }
}
