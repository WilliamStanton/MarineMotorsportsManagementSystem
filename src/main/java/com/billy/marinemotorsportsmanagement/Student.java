package com.billy.marinemotorsportsmanagement;

import java.sql.*;
import java.util.ArrayList;

/**
 * The Student Class allows for various methods of student manipulation and
 * creation/removal
 *
 * @author Billy Stanton
 * @version 1.0
 * @date 11/1/23
 */
public class Student extends Management {

    /**
     * The isStudent method checks if a student exists by ID
     *
     * @param studentID the student ID to check
     * @return true if the student was found, otherwise false
     */
    public boolean isStudent(int studentID) {
        // declare variables
        boolean found = false;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.ID FROM Tool INNER JOIN (Student INNER JOIN Borrow ON Student.ID = Borrow.[Student ID]) ON Tool.ID = Borrow.[Tool ID] WHERE (((Student.ID)=" + studentID + "));"); // Get results for SQL Statement

            // check if any results found
            if (result.next()) {
                found = true;
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return existence
        return found;
    }

    /**
     * The overloaded isStudent method checks if a student exists by ID and is
     * active or inactive (specifically)
     *
     * @param studentID the student ID to check
     * @param status true for active students, false for inactive students
     * @return true if the student was found, otherwise false
     */
    public boolean isStudent(int studentID, boolean status) {
        // declare variables
        boolean found = false;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.ID, Student.Status FROM Student WHERE (((Student.ID)=" + studentID + ") AND ((Student.Status)=" + status + "));"); // Get results for SQL Statement
            // check if any results found
            if (result.next()) {
                found = true;
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return existence
        return found;
    }

    /**
     * The studentStatus method checks if a student is inactive or active
     *
     * @param studentID the student ID to check
     * @return true if active, false if inactive/not found
     */
    public boolean studentStatus(int studentID) {
        return isStudent(studentID, true);
    }

    /**
     * The addStudent method adds a student (admin only)
     *
     * @param name the student name
     * @param session specifies AM/PM session
     * @return true if successfully created, else false
     */
    public boolean addStudent(String name, String session) {
        if (session.toUpperCase().contains("AM") || session.toUpperCase().contains("PM") && sessionStatus()) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Student ( [Student Name], [Session], [Status] ) VALUES (\"" + name + "\", \"" + session + "\",\"True\");"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
            return true; // succesful attempt
        } else {
            return false; // unsuccesful attempt
        }
    }

    /**
     * The removeStudent method removes a student by marking it as inactive
     * (admin only)
     *
     * @param studentID the student id
     * @return true if successfully deleted, else false
     */
    public boolean removeStudent(int studentID) {
        if (isStudent(studentID, true) && sessionStatus()) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Student SET Student.Status = False WHERE (((Student.ID)=" + studentID + "));"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
            return true; // succesful attempt
        } else {
            return false; // unsuccesful attempt
        }
    }

    /**
     * The studentList method returns an arraylist of inactive or active
     * students
     *
     * @param status true if active, false if inactive
     * @return list of students with specified status
     */
    public ArrayList<String> studentList(boolean status) {
        // declare variables
        ArrayList<String> students = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[Student Name] FROM Student WHERE (((Student.Status)=" + status + "));"); // Get results for SQL Statement

            // check if any results found
            while (result.next()) {
                students.add(result.getString("Student Name"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return existence
        return students;
    }

    /**
     * The studentList method returns an arraylist of inactive or active
     * students
     *
     * @param status true if active, false if inactive
     * @param session specifies AM or PM session
     * @return list of students with specified status
     */
    public ArrayList<String> studentList(boolean status, String session) {
        // declare variables
        ArrayList<String> students = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[Student Name] FROM Student WHERE (((Student.Status)=" + status + ") AND ((Student.Session)=\"" + session + "\"));"); // Get results for SQL Statement

            // check if any results found
            while (result.next()) {
                students.add(result.getString("Student Name"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return existence
        return students;
    }
}
