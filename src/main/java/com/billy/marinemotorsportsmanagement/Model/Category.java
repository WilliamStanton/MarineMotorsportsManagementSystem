package com.billy.marinemotorsportsmanagement.Model;

import java.util.ArrayList;

/**
 * Category POJO
 *
 * @author Billy Stanton
 * @version 2.0
*/
public class Category {
    // Category Fields
    private int id;
    private String categoryName;
    private boolean status;
    private ArrayList<Tool> tools;

    public Category(int id, String categoryName, boolean status, ArrayList<Tool> tools) {
        this.id = id;
        this.categoryName = categoryName;
        this.status = status;
        this.tools = tools;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Tool> getTools() {
        return tools;
    }

    public void setTools(ArrayList<Tool> tools) {
        this.tools = tools;
    }
}
