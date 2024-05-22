package com.billy.marinemotorsportsmanagement.Model;

/**
 * Tool POJO
 *
 * @author Billy Stanton
 * @version 2.0
 * @since 3/21/24
 */
public class Tool {
    private int id;
    private int categoryId;
    private String toolName;
    private int quantity;
    private boolean status;

    public Tool(int id, int categoryId, String toolName, int quantity, boolean status) {
        this.id = id;
        this.categoryId = categoryId;
        this.toolName = toolName;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", toolName='" + toolName + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}
