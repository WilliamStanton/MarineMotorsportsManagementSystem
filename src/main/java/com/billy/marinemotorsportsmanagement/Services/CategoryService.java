package com.billy.marinemotorsportsmanagement.Services;

import com.billy.marinemotorsportsmanagement.Model.Category;
import com.billy.marinemotorsportsmanagement.Model.Tool;

import java.sql.*;
import java.util.ArrayList;

/**
 * The Category Class allows for various methods of category manipulation and creation/removal
 *
 * @author Billy Stanton
 * @version 2.0
 * @since 3/21/24
 */
public class CategoryService extends ToolService {
    /**
     * Returns a category including the tool list
     * @param id category id
     * @return complete category
     */
    public Category getCategory(int id) {
        // Init var
        Category category = null;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet getCategory = statement.executeQuery("SELECT * FROM Category WHERE Category.ID=" + "id"); // Get results for SQL Statement
            ResultSet getCategoryTools = statement.executeQuery("SELECT * FROM Tool WHERE Tool.[Category ID]=" + "id"); // Get results for SQL Statement


            // Build category without tool list
            if (getCategory.next())
                category = new Category(getCategory.getInt("ID"), getCategory.getString("Category Name"), getCategory.getBoolean("Status"), null);

            // Get tool list and add to category
            ArrayList<Tool> toolList = new ArrayList<>();
            while (getCategoryTools.next()) {
                toolList.add(new Tool(getCategoryTools.getInt("ID"), getCategoryTools.getInt("Category ID"), getCategoryTools.getString("Tool Name"), getCategoryTools.getInt("Quantity"), getCategoryTools.getBoolean("Status")));
            }
            category.setTools(toolList);


            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return tool category
        return category;
    }

    /**
     * Returns list of all categories
     * @return categories list of all categories
     */
    public ArrayList<Category> getCategories() {
        // Init var
        ArrayList<Category> categories = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Category.ID FROM Category"); // Get results for SQL Statement

            // Build category list
            if (result.next())
                categories.add(getCategory(result.getInt("ID")));

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return list of tool categories
        return categories;
    }

    /**
     * Sets an existing category to the updated information
     * @param category new category
     * @return true/false
     */
    public boolean setCategory(Category category) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            // Update Category Table
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Category SET Category.ID=" + category.getId() + ", Category.[Category Name]=" + category.getCategoryName() + ", Category.Status=" + category.isStatus()); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement

            // Update Tool Table (Put category id on each tool id part of category object)
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT Tool.ID, Tool.[Category ID] FROM Tool", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT); // Create SQL Statement
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                var resultID = result.getInt("ID");
                for (int i = 0; i < category.getTools().size(); i++) {
                    // Update tool category id if tool id is contained in category object
                    if (resultID == category.getTools().get(i).getId())
                        result.updateInt("Category ID", category.getId());
                }
            }

            // Update tool rows
            result.updateRow();
            connection.close(); // Close DB connection

            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        return false;
    }
}
