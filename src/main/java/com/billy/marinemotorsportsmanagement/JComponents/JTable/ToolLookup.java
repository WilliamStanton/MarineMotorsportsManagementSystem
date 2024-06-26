package com.billy.marinemotorsportsmanagement.JComponents.JTable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * The ToolLookup class extends JTable and allows for instantiation of a table for tool lookup with the following columns:
 * Tool Name, ID, Borrower
 * Usually held by Scroll
 */
public class ToolLookup extends JTable {

    // Column Names
    private static final String[] columnNames = { "Tool Name", "ID", "Borrower" };
    public ToolLookup(String[][] data) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, 715, 715);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(50);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(600);
        tcm.getColumn(1).setPreferredWidth(15);
        tcm.getColumn(2).setPreferredWidth(100);
    }

    public ToolLookup(String[][] data, String colName) {
        super(data, new String[]{ "Tool Name", "ID", "Borrower", colName});
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, 715, 715);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(50);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(600);
        tcm.getColumn(1).setPreferredWidth(15);
        tcm.getColumn(2).setPreferredWidth(100);
        tcm.removeColumn(tcm.getColumn(3));
        tcm.removeColumn(tcm.getColumn(1));
    }

    public ToolLookup(String[][] data, int width, int height) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, width, height);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(50);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(600);
        tcm.getColumn(1).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(50);
    }
}
