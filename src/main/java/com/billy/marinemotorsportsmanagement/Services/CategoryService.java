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
            ResultSet getCategory = statement.executeQuery("SELECT * FROM Category WHERE Category.ID=" + id); // Get results for SQL Statement
            ResultSet getCategoryTools = statement.executeQuery("SELECT * FROM Tool WHERE Tool.[Category ID]=" + id); // Get results for SQL Statement


            // Build category without tool list
            if (getCategory.next()) {
                // Build category
                category = new Category(getCategory.getInt("ID"), getCategory.getString("Category Name"), getCategory.getBoolean("Status"), null);

                // Get tool list and add to category
                ArrayList<Tool> toolList = new ArrayList<>();
                while (getCategoryTools.next()) {
                    toolList.add(new Tool(getCategoryTools.getInt("ID"), getCategoryTools.getInt("Category ID"), getCategoryTools.getString("Tool Name"), getCategoryTools.getInt("Quantity"), getCategoryTools.getBoolean("Status")));
                }
                category.setTools(toolList); // Initialize the tools array
            }

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
            while (result.next()) categories.add(getCategory(result.getInt("ID")));

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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Category SET Category.[Category Name] = \"" + category.getCategoryName() + "\", Category.Status = " + category.isStatus() + " WHERE (((Category.ID)=" + category.getId() + "))"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement

            // Add each tool to category that are now in category
            for (int i = 0; i < category.getTools().size(); i++) {
                addToolToCategory(category, category.getTools().get(i).getId());
            }

            // Remove old tools from the category
            var oldCategory = getCategory(category.getId());
            // Check each tool from old category
            for (int i = 0; i < oldCategory.getTools().size(); i++) {
                boolean exists = false;
                // Inside new category list to see if it still exists
                for (int j = 0; j < category.getTools().size(); j++) {
                    // If exists... break
                    if (oldCategory.getTools().get(i).getId() == category.getTools().get(j).getId()) {
                        exists = true;
                        break;
                    }
                }

                // If tool doesn't exist... remove it from the category
                if (!exists) {
                    removeToolFromCategory(oldCategory.getTools().get(i).getId());
                }
            }

            // Update tool rows
            connection.close(); // Close DB connection

            // Successfully set category
            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Unsuccessfully set category
        return false;
    }

    /**
     * Adds a category to the database
     * @return true/false
     */
    public boolean addCategory(String categoryName) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Category ([Category Name]) VALUES \"" + categoryName + "\""); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement
            connection.close(); // Close DB connection

            // Successfully added
            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Unsuccessfully added
        return false;
    }

    /**
     * Adds a tool to a category by id
     * @param category provides category id
     * @param toolID tool id to add to category
     * @return
     */
    private boolean addToolToCategory(Category category, int toolID) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            // Update Tool Table
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Tool SET Tool.[Category ID] = " + category.getId() + " WHERE (((Tool.ID)=" + toolID + "))"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement

            // Update Tool Rows
            connection.close(); // Close DB connection

            // Successfully set category
            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Unsuccessfully set category
        return false;
    }

    /**
     * Removes a tool from a category by id
     * @param toolID tool id to remove
     * @return true/false
     */
    private boolean removeToolFromCategory(int toolID) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            // Update Tool Table
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Tool SET Tool.[Category ID] = null WHERE (((Tool.ID)=" + toolID + "))"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement

            // Update tool rows
            connection.close(); // Close DB connection

            // Successfully set category
            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Unsuccessfully set category
        return false;
    }
}
