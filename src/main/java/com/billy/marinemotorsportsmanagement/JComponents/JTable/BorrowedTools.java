package com.billy.marinemotorsportsmanagement.JComponents.JTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * The BorrowedTools class extends JTable and allows for instantiation of a table for borrowed tools with the following columns:
 * Name, In, Out, ID
 * Usually held by scroll
 */
public class BorrowedTools extends JTable {

    // Column Names
    private static final String[] columnNames = { "Name", "IN", "OUT", "ID"};
    public BorrowedTools(String[][] data) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, 800, 500);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(32);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(600);
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
        tcm.getColumn(0).setPreferredWidth(600);
        tcm.getColumn(1).setPreferredWidth(15);
        tcm.getColumn(2).setPreferredWidth(15);
        tcm.getColumn(3).setPreferredWidth(15);
    }
}
