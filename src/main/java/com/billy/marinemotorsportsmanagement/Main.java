package com.billy.marinemotorsportsmanagement;

import java.util.ArrayList;
import javax.swing.*;

/**
 * This Main Class operates the Marine Motorsports Management Program
 *
 * @author Billy Stanton
 * @version 1.0
 * @date 11/1/23
 */
public class Main {

    public static void main(String[] args) {
        // initiate api
        Tool api = new Tool();
        // load main menu
        mainMenu(api);
    }
    
    /**
     * The mainMenu method displays the options Student, Admin, or exit.
     * @param api the api
     */
    public static void mainMenu(Tool api) {
        String[] options = { "Student", "Admin", "Exit" };
        int selection = JOptionPane.showOptionDialog(null, "Welcome to the Marine Motorsports Management System", "Main Menu", 0, 3, null, options, options[0]);
        switch(selection) {
            case 0:
                JOptionPane.showMessageDialog(null, "You chose Student");
                break;
                
            case 1:
                JOptionPane.showMessageDialog(null, "You chose Admin");
                admin(api);
                break;
                
            case 2:
                JOptionPane.showMessageDialog(null, "You chose Exit");
                api.exit();
        }
    }
    
    public static void student() {
        
    }
    
    public static void admin(Tool api) {
        // Login Field
        JTextField username = new JTextField();
        JTextField password = new JTextField();
        Object[] loginField = {
            "Username:", username,
            "Password:", password
        };
        
        // Attempt login till session status is active
        while(!api.sessionStatus()) {
            // Login Dialog
            int option = JOptionPane.showConfirmDialog(null, loginField, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
            
            // If Ok, check login
            if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {
                JOptionPane.showMessageDialog(null, "Login Successful", "Admin Login", JOptionPane.INFORMATION_MESSAGE, null);
            } 
            
            // If no, exit to menu
            else if (option == JOptionPane.CANCEL_OPTION) {
                mainMenu(api);
                
            // Else, login was incorrect
            } else {
                JOptionPane.showMessageDialog(null, "Login Unsuccessful, please try again", "Admin Login", JOptionPane.ERROR_MESSAGE, null);
            }
        }
        
        // Display options
        String[] options = {"Students", "Tools", "Back to Main Menu"};
        int selection = JOptionPane.showOptionDialog(null, "What would you like to manage?", "Admin Panel", 0, 3, null, options, options[0]);
        switch(selection) {
            case 0:
                // Manage Students
                String[] studentOptions = {"Add Student", "Remove Student", "Back"};
                int studentSelection = JOptionPane.showOptionDialog(null, "Please select an action", "Admin Panel - Student Management", 0, JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);
                switch(studentSelection) {
                    case 0:
                        // Add student
                        // Student Field
                        JTextField fullName = new JTextField();
                        JTextField studentSession = new JTextField();
                        Object[] addStudentField = {
                            "Full Name", fullName,
                            "Session (AM or PM):", studentSession
                        };
                        
                        // 
                        int addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Add Student", JOptionPane.OK_CANCEL_OPTION);
                        // If Ok, attempt student creation
                        if (addStudentInfo == JOptionPane.OK_OPTION) {
                            boolean added = false;
                            // If student added
                            if (api.addStudent(fullName.getText(), studentSession.getText())) {
                                String successMessage = "Student successfully added:\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                                JOptionPane.showMessageDialog(null, successMessage, "Add Student", JOptionPane.INFORMATION_MESSAGE, null);
                            } 
                            // Else, if student not added, try again till added or cancelled
                            else {
                                while(!added) {
                                    JOptionPane.showMessageDialog(null, "Student has not been added, please ensure you enter a valid session.", "Add Student", JOptionPane.ERROR_MESSAGE, null);
                                    addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Add Student", JOptionPane.OK_CANCEL_OPTION);
                                    // if yes, continue trying to add student
                                    if (addStudentInfo == JOptionPane.YES_OPTION) {
                                        if (api.addStudent(fullName.getText(), studentSession.getText())) {
                                            added = true;
                                            String successMessage = "Student successfully added:\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                                            JOptionPane.showMessageDialog(null, successMessage, "Add Student", JOptionPane.INFORMATION_MESSAGE, null);
                                        }
                                    }
                                    // if no, exit loop and return to menu
                                    else {
                                        break;
                                    }
                                }
                            }
                            
                            // Return to admin panel
                            admin(api);
                        }
                        // If No, return to admin panel
                        else {
                            admin(api);
                        }
                        
                        break;
                        
                    case 1:
                        // Remove student
                        // Get all students names and their id
//                        ArrayList<String> studentNames = api.studentNameList(true);
//                        String[] studentNamesArray = studentNames.toArray(new String[studentNames.size()]);
//                        
//                        String studentToRemove = (String) JOptionPane.showInputDialog(null, "", "Remove Student", JOptionPane.QUESTION_MESSAGE, null, studentNamesArray, studentNamesArray[0]);
//                        api.removeStudent(api.getStudentID(studentToRemove));
                        
                        // Remove the student
                        break;
                        
                    case 2:
                        // Back to admin panel
                        admin(api);
                        break;
                    
                }
                
                break;
                
            case 1:
                // Manage Tools
                break;
                
            case 2: 
                // Return to menu
                mainMenu(api);
                break;
        }
    }
    
    

}
