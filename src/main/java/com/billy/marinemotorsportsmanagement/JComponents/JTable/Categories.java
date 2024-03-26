package com.billy.marinemotorsportsmanagement.JComponents.JTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * The Categories class extends JTable and allows for instantiation of a table for displaying categories with the following columns:
 * Category Name, ID
 * Usually held by Scroll
 */
public class Categories extends JTable {

    // Column Names
    private static final String[] columnNames = { "Category Name", "Tools Amount", "ID"};
    public Categories(String[][] data) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, 650, 500);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(32);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(400);
        tcm.getColumn(1).setPreferredWidth(200);
        tcm.getColumn(1).setPreferredWidth(50);
    }

    public Categories(String[][] data, int width, int height) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, width, height);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(32);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(400);
        tcm.getColumn(1).setPreferredWidth(200);
        tcm.getColumn(1).setPreferredWidth(50);
    }
}
