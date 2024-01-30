package com.billy.marinemotorsportsmanagement.JComponents.JTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * The StudentRoster class extends JTable and allows for instantiation of a table for the student roster with the following columns:
 * Name, ID, Session
 * Usually held by scroll
 */
public class StudentRoster extends JTable {

    // Column Names
    private static final String[] columnNames = { "Name", "ID", "Session" };
    public StudentRoster(String[][] data) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, 500, 500);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(32);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(215);
        tcm.getColumn(1).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(15);
    }

    public StudentRoster(String[][] data, int width, int height) {
        super(data, columnNames);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        this.setBounds(30, 40, width, height);
        this.setDefaultEditor(Object.class, null);
        this.setRowHeight(32);
        this.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 22));

        TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(215);
        tcm.getColumn(1).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(15);
    }
}
