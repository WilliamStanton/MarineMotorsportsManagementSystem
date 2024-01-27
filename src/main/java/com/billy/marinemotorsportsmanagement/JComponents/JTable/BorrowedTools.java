package com.billy.marinemotorsportsmanagement.JComponents.JTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class BorrowedTools extends JTable {

    private static final String[] columnNames = { "Name", "Avail.", "Unavail.", "ID"};
    public BorrowedTools(String[][] data) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, 500, 500);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(32);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(200);
        tcm.getColumn(1).setPreferredWidth(25);
        tcm.getColumn(2).setPreferredWidth(30);
        tcm.getColumn(3).setPreferredWidth(20);
    }

    public BorrowedTools(String[][] data, int width, int height) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, width, height);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(32);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(215);
        tcm.getColumn(1).setPreferredWidth(15);
        tcm.getColumn(2).setPreferredWidth(15);
        tcm.getColumn(3).setPreferredWidth(15);
    }
}
