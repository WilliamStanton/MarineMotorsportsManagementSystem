package com.billy.marinemotorsportsmanagement;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This Main Class operates the Marine Motorsports Management Program
 *
 * @author Billy Stanton
 * @version 1.0
 * @since 11/1/23
 */
public class Main {

    public static void main(String[] args) {
        // Initiate api
        Tool api = new Tool();

        // Configure UI
        uiConfig();

        // Load main menu
        mainMenu(api);

        // Exit
        api.exit();
    }

    /**
     * The uiConfig method configures the way that the UI is displayed
     */
    public static void uiConfig() {
        // JOptionPane
        // Fonts
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 20));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 16));
        UIManager.put("OptionPane.textFieldFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.listFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.comboBoxFont", new Font("Segoe UI", Font.PLAIN, 16));

        // Border Size (margin)
        UIManager.put("OptionPane.border", new EmptyBorder(50, 50, 50, 50));

        // Btns
        UIManager.put("OptionPane.cancelButtonText", "Back");
        UIManager.put("OptionPane.okButtonText", "Continue");

        // Text Field
        // Fonts
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 16));
    }

    /**
     * The mainMenu method displays the options Student, Admin, or exit.
     *
     * @param api the api
     */
    public static void mainMenu(Tool api) {
        // Initialize menu options
        String[] options = {"Tool Master", "Admin", "Exit"};
        int selection = JOptionPane.showOptionDialog(null, "Welcome to the Marine Motorsports Management System\nPlease select who you are:", "Marine Motorsports Management System", 0, 3, null, options, options[0]);
        switch (selection) {
            // load tool master panel
            case 0:
                toolMaster(api, null);
                break;

            // load admin panel
            case 1:
                admin(api);
                break;

            // exit application
            default:
                api.exit();
        }
    }

    /**
     * The toolMaster method controls various different functions that are
     * utilized by the toolMaster
     *
     * @param api contains methods for management
     * @param session null if no session chosen, else pass session for method
     */
    public static void toolMaster(Tool api, String session) {
        // Select Session
        if (session == null) {
            String[] sessions = {"AM", "PM", "Back"};
            int sessionChoice = JOptionPane.showOptionDialog(null, "Please select the current session to enter Tool Master", "Tool Master Panel - Select Session", 0, JOptionPane.QUESTION_MESSAGE, null, sessions, sessions[0]);

            switch (sessionChoice) {
                case 0 ->
                    session = "AM";
                case 1 ->
                    session = "PM";
                default ->
                    mainMenu(api);
            }
        }

        // Initialize menu options
        String[] options = {"Quick Scan", "Borrow Tool", "Return Tool", "Tool Report", "Log out of " + session + " Session"};
        int selection = JOptionPane.showOptionDialog(null, "Welcome, Tool Master! Current Session: " + session + "\nWhat would you like to do today?", "Tool Master Panel", 0, 3, null, options, options[0]);
        switch (selection) {
            case 0 ->
                quickScan(api, session);
            case 1 ->
                borrowTool(api, session);
            case 2 ->
                returnTool(api, session);
            case 3 ->
                toolReport(api, session);
            default ->
                mainMenu(api);
        }
    }

    /**
     * The quickScan method allows the tool master to quickly return or borrow a
     * tool by automatically identifying if it should be borrowed or returned
     *
     * @param api contains methods for management
     * @param session current session (AM/PM)
     */
    public static void quickScan(Tool api, String session) {

        // check if students exist
        if (api.studentIDList(true, session).isEmpty()) {
            // Back to tool master if no students found
            JOptionPane.showMessageDialog(null, "No " + session + " Students currently exist", "Tool Master Panel - Borrow Tool", JOptionPane.ERROR_MESSAGE, null);
            toolMaster(api, session);
        }

        // init prime'd bool flag
        boolean cont = true;

        // start quick scan
        while (cont) {
            // Scan tool
            boolean validTool = false;
            String toolSelected = null;

            while (!validTool) {
                // scan tool
                toolSelected = JOptionPane.showInputDialog(null, "Please scan a tool", "Tool Master Panel - Quick Scan", JOptionPane.QUESTION_MESSAGE);
                System.out.println(toolSelected);
                
                // check if tool was scanned
                if (toolSelected != null && toolSelected.contains("MMS")) {
                    toolSelected = toolSelected.replaceAll("[^0-9]+", "");
                    System.out.println(toolSelected);
                } // else return to tool master
                else {
                    toolMaster(api, session);
                }

                // ensure tool status active
                if (api.toolStatus((Integer.parseInt(toolSelected)))) {
                    validTool = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Tool is currently disabled or not found, please scan another tool", "Tool Master Panel - Quick Scan", JOptionPane.ERROR_MESSAGE, null);
                }
            }

            // continue to return or borrow
            // if available, borrow
            if (api.toolAvailability(Integer.parseInt(toolSelected))) {
                // get borrower
                // build student list
                String[] studentList = new String[api.studentNameList(true, session).size()];
                studentList = api.studentNameList(true, session).toArray(studentList);
                Integer[] studentIDList = new Integer[studentList.length];
                studentIDList = api.studentIDList(true, session).toArray(studentIDList);

                // add id to student list that already contains name
                for (int i = 0; i < studentList.length; i++) {
                    studentList[i] += ", ID: " + studentIDList[i];
                }

                // select a student from dropdown
                String studentSelected = (String) JOptionPane.showInputDialog(null, "Please select the student borrowing a tool", "Tool Master Panel - Quick Scan", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                studentSelected = studentSelected.replaceAll("[^0-9]+", ""); // strip everything but id

                // borrow tool successful
                if (api.borrowTool(Integer.parseInt(studentSelected), Integer.parseInt(toolSelected))) {
                    var successchoice = JOptionPane.showConfirmDialog(null, "Tool Successfully Borrowed:\nTool Name: " + api.getToolName(Integer.parseInt(toolSelected)) + "\n\nWould you like to continue scanning?", "Tool Master Panel - Quick Scan", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
                    // check if continue loop to continue quick scan, or exit loop
                    if (successchoice == JOptionPane.OK_OPTION) {
                        cont = true;
                    } else {
                        // return to tool master
                        toolMaster(api, session);
                    }
                } // return tool unsuccessful
                else {
                    var unsuccesfulchoice = JOptionPane.showConfirmDialog(null, "UNSUCCESSFUL BORROW\n" + api.getToolName(Integer.parseInt(toolSelected)) + " was not successfully borrowed, unknown error.\n\nWould you like to continue scanning?", "Tool Master Panel - Return Tool", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null);
                    if (unsuccesfulchoice == JOptionPane.OK_OPTION) {
                        cont = true;
                    } else {
                        // return to tool master
                        toolMaster(api, session);
                    }
                }
            } // else if unavailable, return
            else {
                // return tool successful
                if (api.returnTool(Integer.parseInt(toolSelected))) {
                    var successchoice = JOptionPane.showConfirmDialog(null, "Tool Successfully Returned:\nTool Name: " + api.getToolName(Integer.parseInt(toolSelected)) + "\n\nWould you like to continue scanning?", "Tool Master Panel - Quick Scan", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
                    // check if continue loop to continue quick scan, or exit loop
                    if (successchoice == JOptionPane.OK_OPTION) {
                        cont = true;
                    } else {
                        // return to tool master
                        toolMaster(api, session);
                    }
                } // return tool unsuccessful
                else {
                    var unsuccesfulchoice = JOptionPane.showConfirmDialog(null, "UNSUCCESSFUL RETURN\n" + api.getToolName(Integer.parseInt(toolSelected)) + " was not successfully returned, unknown error.\n\nWould you like to continue scanning?", "Tool Master Panel - Return Tool", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null);
                    if (unsuccesfulchoice == JOptionPane.OK_OPTION) {
                        cont = true;
                    } else {
                        // return to tool master
                        toolMaster(api, session);
                    }
                }
            }
        }
    }

    /**
     * The borrowTool method allows the tool master to handle returning a tool
     *
     * @param api contains methods for management
     * @param session current session (AM/PM)
     */
    public static void returnTool(Tool api, String session) {
        // init prime bool
        boolean cont = true;

        while (cont) {
            // re-config prime bool
            cont = false;
            // Check if any tools available to return
            if (api.toolIDList(true, false).isEmpty()) {
                // Back to tool master if no tools found
                JOptionPane.showMessageDialog(null, "No tools are currently being borrowed", "Tool Master Panel - Return Tool", JOptionPane.ERROR_MESSAGE, null);
                toolMaster(api, session);
            } // else continue
            else {
                // barcode scanner (new)
                String toolSelected;
                toolSelected = JOptionPane.showInputDialog(null, "Please scan the tool to return", "Tool Master Panel - Return Tool", JOptionPane.QUESTION_MESSAGE);
                if (toolSelected != null && toolSelected.contains("MMS")) {
                    toolSelected = toolSelected.replaceAll("[^0-9]+", "");
                    // borrow the tool
                    if (!api.toolAvailability(Integer.parseInt(toolSelected)) && api.returnTool(Integer.parseInt(toolSelected))) {
                        var choice = JOptionPane.showConfirmDialog(null, "Tool Successfully Returned:\nTool Name: " + api.getToolName(Integer.parseInt(toolSelected)) + "\n\nWould you like to return another tool?", "Tool Master Panel - Return Tool", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
                        // check if continue loop to return again, or exit loop
                        if (choice == JOptionPane.OK_OPTION) {
                            cont = true;
                        }
                    } else if (api.toolAvailability(Integer.parseInt(toolSelected))) {
                        var choice = JOptionPane.showConfirmDialog(null, "UNSUCCESSFUL RETURN\n" + api.getToolName(Integer.parseInt(toolSelected)) + " is not currently borrowed, and cannot be returned.\n\nWould you like to scan another tool instead?", "Tool Master Panel - Return Tool", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null);
                        if (choice == JOptionPane.OK_OPTION) {
                            cont = true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "UNSUCCESSFUL RETURN\nTool: " + api.getToolName(Integer.parseInt(toolSelected)) + " was not successfully returned, unknown error.", "Tool Master Panel - Return Tool", JOptionPane.ERROR_MESSAGE, null);
                    }
                } else {
                    // return to tool master panel
                    toolMaster(api, session);
                }
            }
        }

        // return back to tool master panel once all completed
        toolMaster(api, session);
    }

    /**
     * The borrowTool method allows the tool master to handle borrowing a tool
     * for the student requiring it
     *
     * @param api contains methods for management
     * @param session current session (AM/PM)
     */
    public static void borrowTool(Tool api, String session) {
        // init prime bool
        boolean cont = true;
        while (cont) {
            // re-config prime bool
            cont = false;
            // Check if any tools available to borrow
            if (api.toolIDList(true, true).isEmpty()) {
                // Back to tool master if no tools found
                JOptionPane.showMessageDialog(null, "No tools are currently able to be borrowed", "Tool Master Panel - Borrow Tool", JOptionPane.ERROR_MESSAGE, null);
                toolMaster(api, session);
            } // else continue 
            else {
                // build student list
                String[] studentList = new String[api.studentNameList(true, session).size()];
                studentList = api.studentNameList(true, session).toArray(studentList);
                Integer[] studentIDList = new Integer[studentList.length];
                studentIDList = api.studentIDList(true, session).toArray(studentIDList);

                // add id to student list that already contains name
                for (int i = 0; i < studentList.length; i++) {
                    studentList[i] += ", ID: " + studentIDList[i];
                }

                // select a student from dropdown
                String studentSelected = (String) JOptionPane.showInputDialog(null, "Please select the student borrowing a tool", "Tool Master Panel - Borrow Tool", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                if (studentSelected != null) {
                    studentSelected = studentSelected.replaceAll("[^0-9]+", ""); // strip everything but id
                    // barcode scanner (new)
                    String toolSelected;
                    toolSelected = JOptionPane.showInputDialog(null, "Please scan the tool to borrow", "Tool Master Panel - Borrow Tool", JOptionPane.QUESTION_MESSAGE);
                    if (toolSelected != null && toolSelected.contains("MMS")) {
                        toolSelected = toolSelected.replaceAll("[^0-9]+", "");
                        // borrow the tool
                        if (api.toolAvailability(Integer.parseInt(toolSelected)) && api.borrowTool(Integer.parseInt(studentSelected), Integer.parseInt(toolSelected))) {
                            var choice = JOptionPane.showConfirmDialog(null, "Tool Successfully Borrowed:\nTool Name: " + api.getToolName(Integer.parseInt(toolSelected)) + "\n\nWould you like to borrow another tool?", "Tool Master Panel - Borrow Tool", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
                            // check if continue loop to borrow again, or exit loop
                            if (choice == JOptionPane.OK_OPTION) {
                                cont = true;
                            }
                        } else if (!api.toolAvailability(Integer.parseInt(toolSelected))) {
                            var choice = JOptionPane.showConfirmDialog(null, "UNSUCCESSFUL BORROW\n" + api.getToolName(Integer.parseInt(toolSelected)) + " is still being borrowed by " + api.getStudentName(api.getToolBorrowerID(Integer.parseInt(toolSelected))) + "\n\nWould you like to scan another tool instead?", "Tool Master Panel - Borrow Tool", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null);
                            // check if continue loop to borrow again, or exit loop
                            if (choice == JOptionPane.OK_OPTION) {
                                cont = true;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "UNSUCCESSFUL BORROW\nTool: " + api.getToolName(Integer.parseInt(toolSelected)) + " was not successfully borrowed, unknown error.", "Tool Master Panel - Borrow Tool", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // else return to tool master panel
                    else {
                        toolMaster(api, session);
                    }
                } // else return to tool master panel
                else {
                    toolMaster(api, session);
                }
            }
        }
        // return back to tool master panel once all completed
        toolMaster(api, session);
    }

    /**
     * The toolReport method provides various reports for students and tools
     *
     * @param api contains methods for management
     * @param session current session (AM/PM)
     */
    public static void toolReport(Tool api, String session) {
        // Initialize options
        String[] options = {"Available Tools", "Unavailable Tools", "Back to Tool Master Panel"};
        int selection = JOptionPane.showOptionDialog(null, "Please select an action", "Tool Master Panel - Tool Report", 0, 3, null, options, options[0]);

        switch (selection) {
            // Available Tools
            case 0:
                // Check if any available tools
                if (!api.toolIDList(true, true).isEmpty()) {
                    // Get all tool ids
                    ArrayList<Integer> allToolIDSAvailable = api.toolIDList(true, true);

                    // Get all tool names
                    ArrayList<String> allToolNamesAvailable = new ArrayList<>();
                    for (int i = 0; i < allToolIDSAvailable.size(); i++) {
                        allToolNamesAvailable.add(api.getToolName(allToolIDSAvailable.get(i)));
                    }

                    // Build list of available tools
                    String allClassAvailable = "";
                    for (int i = 0; i < allToolIDSAvailable.size(); i++) {
                        allClassAvailable += i + 1 + ") Tool Name: " + allToolNamesAvailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSAvailable.get(i) + "\n\n";
                    }

                    // Display list of available tools
                    JTextArea textArea = new JTextArea("Available Tools \n\n" + allClassAvailable);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setEditable(false);
                    scrollPane.setPreferredSize(new Dimension(500, 500));
                    JOptionPane.showMessageDialog(null, scrollPane, "Tool Master Panel - Tool Report", JOptionPane.YES_NO_OPTION);
                } // Else no tools available
                else {
                    JOptionPane.showMessageDialog(null, "No available tools found", "Tool Master Panel - Tool Report", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool report
                toolReport(api, session);
                break;

            // Unavailable Tools
            case 1:
                unavailableTools(api, session);
                break;

            // Back to tool master panel
            case 2:
                toolMaster(api, session);
                break;
        }
    }

    /**
     * The unavailableTools method provides the methods for viewing the various
     * unavailable tool reports
     *
     * @param api contains methods for management
     * @param session current session (AM/PM)
     */
    public static void unavailableTools(Tool api, String session) {
        // Initialize options
        String[] sessionOptions = {"AM Class", "PM Class", "All Classes", "Back"};

        // Check if any unavailable tools
        if (api.toolIDList(true, false).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No unavailable tools found", "Tool Master Panel - Tool Report", JOptionPane.ERROR_MESSAGE, null);
            toolMaster(api, session);
        }

        // Load all Unavailable Tools
        // Get all tool ids
        ArrayList<Integer> allToolIDSUnavailable = api.toolIDList(true, false);

        // Get all tool names
        ArrayList<String> allToolNamesUnavailable = new ArrayList<>();
        for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
            allToolNamesUnavailable.add(api.getToolName(allToolIDSUnavailable.get(i)));
        }

        // Get all borrower names
        ArrayList<String> allBorrowerNamesUnavailable = new ArrayList<>();
        for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
            allBorrowerNamesUnavailable.add(api.getStudentName(api.getToolBorrowerID(allToolIDSUnavailable.get(i))));
        }

        // Get all borrower sessions
        ArrayList<String> allBorrowerSessionsUnavailable = new ArrayList<>();
        for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
            allBorrowerSessionsUnavailable.add(api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))));
        }

        int unavailableSession = JOptionPane.showOptionDialog(null, "Please select the Class Session to view", "Tool Master Panel - Tool Report", 0, 3, null, sessionOptions, sessionOptions[0]);
        switch (unavailableSession) {

            // AM Class - Unavailable Tools
            case 0 -> {
                // Build list of AM Class unavailable tools
                String amClassUnavailable = "";
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    if (allBorrowerSessionsUnavailable.get(i).equals("AM")) {
                        amClassUnavailable += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                                + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + ""
                                + "\n     - Session: " + api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))) + "\n\n";
                    }
                }

                // Display list of unavailable tools
                JTextArea textArea = new JTextArea("Unavailable Tools - AM Class\n\n" + amClassUnavailable);
                JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                scrollPane.setPreferredSize(new Dimension(500, 500));
                JOptionPane.showMessageDialog(null, scrollPane, "Tool Master Panel - Tool Report", JOptionPane.INFORMATION_MESSAGE);

                // Return to unavailable tools panel
                unavailableTools(api, session);
            }

            // PM Class - Unavailable Tools
            case 1 -> {
                // Build list of PM Class unavailable tools
                String pmClassUnavailable = "";
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    if (allBorrowerSessionsUnavailable.get(i).equals("PM")) {
                        pmClassUnavailable += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                                + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + ""
                                + "\n     - Session: " + api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))) + "\n\n";
                    }
                }

                // Display list of unavailable tools
                JTextArea textArea = new JTextArea("Unavailable Tools - PM Class\n\n" + pmClassUnavailable);
                JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                scrollPane.setPreferredSize(new Dimension(500, 500));
                JOptionPane.showMessageDialog(null, scrollPane, "Tool Master Panel - Tool Report", JOptionPane.INFORMATION_MESSAGE);

                // Return to unavailable tools panel
                unavailableTools(api, session);
            }

            // All Classes - Unavailable Tools
            case 2 -> {
                // Build list of all unavailable tools
                String allClassUnavailable = "";
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    allClassUnavailable += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                            + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                            + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + ""
                            + "\n     - Session: " + api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))) + "\n\n";
                }

                // Display list of unavailable tools
                JTextArea textArea = new JTextArea("Unavailable Tools - All Classes\n\n" + allClassUnavailable);
                JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                scrollPane.setPreferredSize(new Dimension(500, 500));
                JOptionPane.showMessageDialog(null, scrollPane, "Tool Master Panel - Tool Report", JOptionPane.INFORMATION_MESSAGE);

                // Return to unavailable tools panel
                unavailableTools(api, session);
            }

            // Back to Tool Report Panel
            case 3 ->
                toolReport(api, session);
        }

        // Ensure return to Tool Report Panel
        toolReport(api, session);
    }

    /**
     * The admin method controls authentication and various different admin
     * functions for the admin panel
     *
     * @param api contains methods for management
     */
    public static void admin(Tool api) {
        // Login Field
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        Object[] loginField = {
            "Username", username,
            "Password", password
        };

        // Attempt login till session status is active
        while (!api.sessionStatus()) {
            // Login Dialog
            int option = JOptionPane.showConfirmDialog(null, loginField, "Admin Login", JOptionPane.OK_CANCEL_OPTION);

            // If Ok, check login
            if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {
                JOptionPane.showMessageDialog(null, "Welcome, Login Successful", "Admin Login", JOptionPane.INFORMATION_MESSAGE, null);
            } // If no, exit to menu
            else if (option == JOptionPane.CANCEL_OPTION) {
                mainMenu(api);
            } // Else, login was incorrect
            else {
                JOptionPane.showMessageDialog(null, "Invalid credentials, please try again", "Admin Login", JOptionPane.ERROR_MESSAGE, null);
            }
        }

        // Initialize options
        String[] options = {"Student Management", "Tool Management", "Logout of Admin"};
        int selection = JOptionPane.showOptionDialog(null, "Hi, Admin!\nWhat would you like to do today?", "Admin Panel", 0, 3, null, options, options[0]);
        switch (selection) {
            // Manage Students
            case 0 ->
                studentManagementAdmin(api);
            // Manage Tools
            case 1 ->
                toolManagementAdmin(api);
            // Return to main menu and logout
            case 2 -> {
                api.logout();
                mainMenu(api);
            }
        }
    }

    /**
     * The studentManagementAdmin method provides options for managing students
     * and is used only in the admin class
     *
     * @param api contains methods for management
     */
    public static void studentManagementAdmin(Tool api) {
        // Select Add or Remove or Back
        String[] studentOptions = {"Add Student", "Disable Student", "Re-enable Student", "View Students", "Back to Admin Panel"};
        int studentSelection = JOptionPane.showOptionDialog(null, "Please select an action", "Admin Panel - Student Management", 0, JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);
        switch (studentSelection) {
            case 0 -> {
                // Add student
                // Student Field
                JTextField fullName = new JTextField();
                JTextField studentSession = new JTextField();
                Object[] addStudentField = {
                    "Full Name", fullName,
                    "Session (AM or PM):", studentSession
                };

                // 
                int addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Admin Panel - Add Student", JOptionPane.OK_CANCEL_OPTION);
                // If Ok, attempt student creation
                if (addStudentInfo == JOptionPane.OK_OPTION) {
                    boolean added = false;
                    // If student added
                    if (api.addStudent(fullName.getText(), studentSession.getText())) {
                        String successMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                        JOptionPane.showMessageDialog(null, successMessage, "Add Student", JOptionPane.INFORMATION_MESSAGE, null);
                    } // Else, if student not added, try again till added or cancelled
                    else {
                        while (!added) {
                            JOptionPane.showMessageDialog(null, "Student has not been added, please ensure you enter a valid session.", "Add Student", JOptionPane.ERROR_MESSAGE, null);
                            addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Admin Panel - Add Student", JOptionPane.OK_CANCEL_OPTION);
                            // if yes, continue trying to add student
                            if (addStudentInfo == JOptionPane.YES_OPTION) {
                                if (api.addStudent(fullName.getText(), studentSession.getText())) {
                                    added = true;
                                    String successAddMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                                    JOptionPane.showMessageDialog(null, successAddMessage, "Admin Panel - Add Student", JOptionPane.INFORMATION_MESSAGE, null);
                                }
                            } // if no, exit loop and return to menu
                            else {
                                break;
                            }
                        }
                    }

                    // Return to student management panel
                    studentManagementAdmin(api);
                } // If No, return to student management panel
                else {
                    studentManagementAdmin(api);
                }
            }

            case 1 -> {
                // Disable student
                // if any students are enabled
                if (!api.studentNameList(true).isEmpty()) {
                    // Get all students names
                    ArrayList<String> studentNames = api.studentNameList(true);
                    // Get all student ids
                    ArrayList<Integer> studentIds = api.studentIDList(true);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < studentNames.size(); i++) {
                        tempArray.add(studentNames.get(i) + ", ID: " + studentIds.get(i));
                    }

                    // Arraylist -> Array
                    String[] studentList = new String[tempArray.size()];
                    studentList = tempArray.toArray(studentList);

                    // Select and disable student
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Disable Student", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.disableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully disabled: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Disable Student", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Student has not been disabled. Error.", "Admin Panel - Disable Student", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagementAdmin(api);
                    }
                } // if no enabled students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to disable.", "Admin Panel - Disable Student", JOptionPane.ERROR_MESSAGE, null);
                }
                // Return to student management panel once completed
                studentManagementAdmin(api);
            }
            case 2 -> {
                // if any students are disabled
                if (!api.studentNameList(false).isEmpty()) {
                    // Get all students names
                    ArrayList<String> studentNames = api.studentNameList(false);
                    // Get all student ids
                    ArrayList<Integer> studentIds = api.studentIDList(false);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < studentNames.size(); i++) {
                        tempArray.add(studentNames.get(i) + ", ID: " + studentIds.get(i));
                    }

                    // Arraylist -> Array
                    String[] studentList = new String[tempArray.size()];
                    studentList = tempArray.toArray(studentList);

                    // Select and re-enable student
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Re-enable Student", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);

                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.enableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully re-enabled: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Re-enable Student", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Student has not been re-enabled. Error.", "Admin Panel - Re-enable Student", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagementAdmin(api);
                    }
                } // if no disabled students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to re-enable.", "Admin Panel - Re-enable Student", JOptionPane.ERROR_MESSAGE, null);
                }
                // Return to student management panel once completed
                studentManagementAdmin(api);
            }
            case 3 ->
                viewStudentsAdmin(api);

            default -> // Return back to admin panel
                admin(api);
        }
    }

    /**
     * The viewStudentsAdmin method shows disabled/enabled, and all students
     * used only in the admin method
     *
     * @param api
     */
    public static void viewStudentsAdmin(Tool api) {
        // Check if any student available to borrow
        if (api.studentIDList(false).isEmpty() && api.studentIDList(true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No Students exist", "Admin Panel - View Students", JOptionPane.ERROR_MESSAGE, null);
            toolManagementAdmin(api);
        } // else continue 
        else {
            // initialize flags & arrays
            boolean disabledStudents = false;
            boolean enabledStudents = false;
            String enabledStudentsString = "";
            String disabledStudentsString = "";

            // check disabled students
            if (!api.studentNameList(false).isEmpty()) {
                // update flag
                disabledStudents = true;

                // build disabled students
                // Get disabled student list
                ArrayList<String> disabledStudentNameList = api.studentNameList(false);
                ArrayList<Integer> disabledStudentIDList = api.studentIDList(false);

                // Combine names with ids
                for (int i = 0; i < disabledStudentNameList.size(); i++) {
                    disabledStudentsString += (disabledStudentNameList.get(i) + ", ID: " + disabledStudentIDList.get(i) + ", Session: " + api.getStudentSession(disabledStudentIDList.get(i)) + ", Status: Disabled" + "\n");
                }
            }

            // check enabled students
            if (!api.studentNameList(true).isEmpty()) {
                // update flag
                enabledStudents = true;

                // build enabled students
                // Get enabled student list
                ArrayList<String> enabledStudentNameList = api.studentNameList(true);
                ArrayList<Integer> enabledStudentIDList = api.studentIDList(true);

                // Combine names with ids
                for (int i = 0; i < enabledStudentNameList.size(); i++) {
                    enabledStudentsString += (enabledStudentNameList.get(i) + ", ID: " + enabledStudentIDList.get(i) + ", Session: " + api.getStudentSession(enabledStudentIDList.get(i)) + ", Status: Enabled") + "\n";
                }
            }

            // Initialize Options
            String[] viewStudentOptions = {"View Enabled Students", "View Disabled Students", "View all Students", "Back"};
            int viewStudentSelection = (int) JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - View Students", 0, 3, null, viewStudentOptions, viewStudentOptions[0]);
            switch (viewStudentSelection) {
                // view enabled Students
                case 0 -> {
                    // check if enabled Students found
                    if (enabledStudents) {
                        JTextArea textArea = new JTextArea(enabledStudentsString);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setEditable(false);
                        scrollPane.setPreferredSize(new Dimension(500, 500));
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Enabled Students", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no enabled Students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Enabled Students exist", "Admin Panel - View Enabled Students", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
                // view disabled Students
                case 1 -> {
                    // check if disabled Students found
                    if (disabledStudents) {
                        JTextArea textArea = new JTextArea(disabledStudentsString);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setEditable(false);
                        scrollPane.setPreferredSize(new Dimension(500, 500));
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Disabled Students", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no disabled students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Disabled Students exist", "Admin Panel Panel - View Disabled Students", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
                // view all tools
                case 2 -> {
                    // check if disabled or enabled Students found
                    if (disabledStudents || enabledStudents) {
                        // Combine disabled + enabled
                        String fullStudentString = enabledStudentsString + disabledStudentsString;

                        // Display Students
                        JTextArea textArea = new JTextArea(fullStudentString);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setEditable(false);
                        scrollPane.setPreferredSize(new Dimension(500, 500));
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View All Students", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no enabled or disabled Students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Students exist", "Tool Master Panel - View All Students", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
                // return to Student management
                default ->
                    studentManagementAdmin(api);
            }
        }
        // Return to view students panel once completed
        viewStudentsAdmin(api);
    }

    /**
     * The toolManagementAdmin method provides options for managing students and
     * is used only in the admin method
     *
     * @param api contains methods for management
     */
    public static void toolManagementAdmin(Tool api) {
        // Select Add or Remove or Back
        String[] toolOptions = {"Add tool", "Disable tool", "Re-enable Tool", "View Tools", "Back"};
        int toolSelection = JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - Tool Management", 0, 3, null, toolOptions, toolOptions[0]);
        switch (toolSelection) {
            case 0 -> {
                // Add a tool
                String toolName = JOptionPane.showInputDialog(null, "Enter a name for the new tool", "Admin Panel - Add Tool", JOptionPane.QUESTION_MESSAGE);
                // If yes, add 
                if (toolName != null) {
                    // If tool added
                    int toolID = api.createTool(toolName);
                    if (toolID != 0) {
                        String successAddMessage = "Tool successfully added: " + toolName + ", ID: " + toolID;
                        JOptionPane.showMessageDialog(null, successAddMessage, "Admin Panel - Add Tool", JOptionPane.INFORMATION_MESSAGE);
                    } // Else if tool not added,
                    else {
                        JOptionPane.showMessageDialog(null, "Tool has not been added, error.", "Admin Panel - Add Tool", JOptionPane.ERROR_MESSAGE);
                    }
                    // If no, return to admin panel
                } else {
                    toolManagementAdmin(api);
                }

                // Return to tool management once completed
                toolManagementAdmin(api);
            }

            case 1 -> {
                // Disable a tool
                // Get enabled tool list
                if (!api.toolNameList(true).isEmpty()) {
                    ArrayList<String> toolNameList = api.toolNameList(true);
                    ArrayList<Integer> toolIDList = api.toolIDList(true);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < toolNameList.size(); i++) {
                        tempArray.add(toolNameList.get(i) + ", ID: " + toolIDList.get(i));
                    }

                    // Arraylist -> Array
                    String[] toolList = new String[tempArray.size()];
                    toolList = tempArray.toArray(toolList);

                    // Select and Disable tool
                    String toolToDisable = (String) JOptionPane.showInputDialog(null, "Disable Tool", "Admin Panel - Disable Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToDisable != null) {
                        // Strip String to only ID
                        String toolID = toolToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.disableTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully disabled: " + toolToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Disable Tool", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been disabled. Error.", "Admin Panel - Disable Tool", JOptionPane.ERROR_MESSAGE, null);
                        }

                        // Return to tool management once completed
                        toolManagementAdmin(api);
                        break;
                    } // If No, return to tool management
                    else {
                        toolManagementAdmin(api);
                    }
                } // if no enabled tools
                else {
                    JOptionPane.showMessageDialog(null, "There are no tools to disable.", "Admin Panel - Disable Tool", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool management panel once completed
                toolManagementAdmin(api);
            }

            // Re-enable tools
            case 2 -> {
                // Get disabled tool list
                if (!api.toolNameList(false).isEmpty()) {
                    ArrayList<String> toolNameList = api.toolNameList(false);
                    ArrayList<Integer> toolIDList = api.toolIDList(false);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < toolNameList.size(); i++) {
                        tempArray.add(toolNameList.get(i) + ", ID: " + toolIDList.get(i));
                    }

                    // Arraylist -> Array
                    String[] toolList = new String[tempArray.size()];
                    toolList = tempArray.toArray(toolList);

                    // Select and re-enable tool
                    String toolToDisable = (String) JOptionPane.showInputDialog(null, "Re-enable Tool", "Admin Panel - Re-enable Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToDisable != null) {
                        // Strip String to only ID
                        String toolID = toolToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.enableTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully re-enabled: " + toolToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Re-enable Tool", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been re-enabled. Error.", "Admin Panel - Re-enable Tool", JOptionPane.ERROR_MESSAGE, null);
                        }

                        // Return to tool management once completed
                        toolManagementAdmin(api);
                        break;
                    } // If no, return to tool management
                    else {
                        toolManagementAdmin(api);
                    }
                } // If no enabled tools
                else {
                    JOptionPane.showMessageDialog(null, "There are no tools to re-enable.", "Admin Panel - Re-enable Tool", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool management panel once completed
                toolManagementAdmin(api);
            }

            // View tools
            case 3 ->
                viewToolsAdmin(api);

            default -> // Back to admin panel
                admin(api);
        }
    }

    /**
     * The viewToolsAdmin method shows disabled/enabled tools, and all tools
     * used only in the admin method
     *
     * @param api used only in the admin class
     */
    public static void viewToolsAdmin(Tool api) {
        // Check if any tools available to borrow
        if (api.toolIDList(false).isEmpty() && api.toolIDList(true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No Tools exist", "Admin Panel - View Tools", JOptionPane.ERROR_MESSAGE, null);
            toolManagementAdmin(api);
        } // else continue 
        else {
            // initialize flags & arrays
            boolean disabledTools = false;
            boolean enabledTools = false;
            String enabledToolsString = "";
            String disabledToolsString = "";
            
            // check disabled tools
            if (!api.toolNameList(false).isEmpty()) {
                // update flag
                disabledTools = true;

                // build disabled tools
                // Get disabled tool list
                ArrayList<String> disabledToolNameList = api.toolNameList(false);
                ArrayList<Integer> disabledToolIDList = api.toolIDList(false);

                // Combine names with ids
                for (int i = 0; i < disabledToolNameList.size(); i++) {
                    disabledToolsString += (disabledToolNameList.get(i) + ", ID: " + disabledToolIDList.get(i) + ", Status: Disabled" + "\n");
                }
            }

            // check enabled tools
            if (!api.toolNameList(true).isEmpty()) {
                // update flag
                enabledTools = true;

                // build enabled tools
                // Get enabled tool list
                ArrayList<String> enabledToolNameList = api.toolNameList(true);
                ArrayList<Integer> enabledToolIDList = api.toolIDList(true);

                // Combine names with ids
                for (int i = 0; i < enabledToolNameList.size(); i++) {
                    enabledToolsString += (enabledToolNameList.get(i) + ", ID: " + enabledToolIDList.get(i) + ", Status: Enabled" + "\n");
                }   
            }

            // Initialize Options
            String[] viewToolOptions = {"View Enabled Tools", "View Disabled Tools", "View all Tools", "Back"};
            int viewToolSelection = (int) JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - View Tools", 0, 3, null, viewToolOptions, viewToolOptions[0]);
            switch (viewToolSelection) {
                // view enabled tools
                case 0 -> {
                    // check if enabled tools found
                    if (enabledTools) {
                        JTextArea textArea = new JTextArea(enabledToolsString);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setEditable(false);
                        scrollPane.setPreferredSize(new Dimension(500, 500));
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Enabled Tools", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no enabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Enabled Tools exist", "Admin Panel - View Enabled Tools", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
                // view disabled tools
                case 1 -> {
                    // check if disabled tools found
                    if (disabledTools) {
                        JTextArea textArea = new JTextArea(disabledToolsString);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setEditable(false);
                        scrollPane.setPreferredSize(new Dimension(500, 500));
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Disabled Tools", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no disabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Disabled Tools exist", "Admin Panel Panel - View Disabled Tools", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
                // view all tools
                case 2 -> {
                    // check if disabled or enabled tools found
                    if (disabledTools || enabledTools) {
                        // combine both disabled + enabled
                        String allTools = enabledToolsString + disabledToolsString;
                        
                        JTextArea textArea = new JTextArea(allTools);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setEditable(false);
                        scrollPane.setPreferredSize(new Dimension(500, 500));
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View All Tools", JOptionPane.INFORMATION_MESSAGE, null);

                    } // else no disabled or enabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Tools exist", "Tool Master Panel - View All Tools", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
                // return to tool management
                default ->
                    toolManagementAdmin(api);
            }
        }
        // Return to view tools panel once completed
        viewToolsAdmin(api);
    }
}
