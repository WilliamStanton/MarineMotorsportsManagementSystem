package com.billy.marinemotorsportsmanagement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
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

        // Ensure Database Connection before continuing
        if (!api.databaseConnection()) {
            JOptionPane.showMessageDialog(null, "Database Error\nNot Connected, please contact support");
            api.exit();
        }

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
        UIManager.put("OptionPane.messageForeground", Color.white);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.BOLD, 32));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 32));
        UIManager.put("OptionPane.textFieldFont", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("OptionPane.listFont", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.BOLD, 28));

        // JOptionPane Size
        UIManager.put("OptionPane.border", new EmptyBorder(120, 120, 120, 120));
        UIManager.put("OptionPane.background", new Color(66, 66, 100));
        UIManager.put("Panel.background", new Color(66, 66, 100));

        // Btns
        UIManager.put("OptionPane.cancelButtonText", "Back");
        UIManager.put("OptionPane.okButtonText", "Continue");
        UIManager.put("Button.border", new EmptyBorder(35, 50, 35, 50));
        UIManager.put("Button.background", new Color(97, 97, 114));
        UIManager.put("Button.foreground", Color.white);

        // Text Field
        // Fonts
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 28));
    }

    /**
     * The mainMenu method displays the options Student, Admin, or exit.
     *
     * @param api the api
     */
    public static void mainMenu(Tool api) {
        // Initialize menu options
        String[] options = {"Tool Master", "Teacher"};
        int selection = JOptionPane.showOptionDialog(null, "Welcome to the Marine Motorsports Management System\nPlease select who you are:", "Marine Motorsports Management System", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
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
                // Login Field
                JLabel exitMessage = new JLabel("Please enter the admininistrator login to exit the application.");
                exitMessage.setFont(new Font("Segoe UI", Font.PLAIN, 28));
                exitMessage.setForeground(Color.white);
                JTextField username = new JTextField();
                JPasswordField password = new JPasswordField();
                Object[] loginField = {
                    exitMessage,
                    "Username", username,
                    "Password", password
                };

                // Attempt login till session status is active
                while (!api.sessionStatus()) {
                    // Login Dialog
                    int option = JOptionPane.showConfirmDialog(null, loginField, "Exit Application", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    // If Ok, check login
                    if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {
                        api.exit();
                    } // If no, exit to menu
                    else if (option == JOptionPane.CANCEL_OPTION) {
                        mainMenu(api);
                    } // Else, login was incorrect
                    else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials, please try again", "Admin Login", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
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
            int sessionChoice = JOptionPane.showOptionDialog(null, "Tool Master - Select the current session", "Tool Master Panel - Select Session", 0, JOptionPane.PLAIN_MESSAGE, null, sessions, sessions[0]);

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
        String[] options = {"Scan Tools", "View Borrowed Tools", "Log out of " + session + " Session"};
        int selection = JOptionPane.showOptionDialog(null, "Tool Master - Current Session: " + session + "\nWhat would you like to do?", "Tool Master Panel", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (selection) {
            case 0 ->
                quickScan(api, session);
            case 1 ->
                unavailableTools(api, session);
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
        // Run quick scan till exit
        while (true) {
            // init var
            int studentID = 0;
            // Get chosen student or exit quick scan
            String studentSelected = (String) JOptionPane.showInputDialog(null, "Select a Student", "Tool Master Panel - Quick Scan", JOptionPane.PLAIN_MESSAGE, null, studentList, studentList[0]);

            // ensure student chosen, otherwise return
            if (studentSelected != null) {
                // get student id
                studentSelected = studentSelected.replaceAll("[^0-9]+", ""); // strip everything but id
                studentID = Integer.parseInt(studentSelected);
            } else {
                // exit quick scan
                toolMaster(api, session);
            }

            // run specific student till exit 
            int errors = 0;
            boolean studentSession = true;
            ArrayList<String> resultList = new ArrayList<>();
            while (studentSession) {
                String results = "";
                // Build resultList
                for (int i = 0; i < resultList.size(); i++) {
                    results += resultList.get(i) + "\n";
                }
                String toolSelected = JOptionPane.showInputDialog(null, "Scanning tools for: " + api.getStudentName(studentID) + "\n\n\n" + results, "Tool Scan: " + api.getStudentName(studentID), JOptionPane.PLAIN_MESSAGE);

                // check if a tool was actually scanned
                if (toolSelected != null) {
                    if (toolSelected.toUpperCase().contains("MMS")) {
                        toolSelected = toolSelected.replaceAll("[^0-9]+", ""); // clean input
                        int toolID = Integer.parseInt(toolSelected); // string -> int

                        // borrow or return tool
                        // tool active, continue
                        if (api.toolStatus(toolID)) {
                            // check if can borrow
                            if (api.toolAvailability(toolID)) {
                                // borrow tool
                                if (api.borrowTool(studentID, toolID)) {
                                    resultList.add("Borrowed: " + api.getToolName(toolID) + ", ID: " + toolID);
                                } else {
                                    resultList.add("Borrow Error, ID:" + toolID);
                                    errors++;
                                }
                            } // else, check if can return
                            else if (!api.toolAvailability(toolID)) {
                                // return tool
                                if (api.returnTool(toolID)) {
                                    resultList.add("Returned: " + api.getToolName(toolID) + ", ID: " + toolID);
                                } else {
                                    resultList.add("Return Error, ID: " + toolID);
                                    errors++;
                                }
                            } // else, unknown error
                            else {
                                resultList.add("Unknown Error - ID: " + toolID);
                                errors++;
                            }
                        } // error, tool inactive, must scan another tool
                        else {
                            resultList.add("Error: " + api.getToolName(toolID) + " is inactive, Tool ID: " + toolID);
                            errors++;
                        }
                    } // Input does not contain MMS 
                    else {
                        resultList.add("Error: Invalid Input");
                        errors++;
                    }
                } // exit quick scan
                else {
                    if (errors > 0) {
                        JOptionPane.showMessageDialog(null, "There were " + errors + " errors scanning tools for: " + api.getStudentName(studentID) + ".\nPlease ensure all tools were properly scanned: \n\n" + results);
                    }
                    studentSession = false;
                }
            }
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

        // Check if any unavailable tools
        if (api.toolIDList(true, false).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No borrowed tools found", "Tool Master Panel - Tool Report", JOptionPane.ERROR_MESSAGE, null);
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

        switch (session) {
            // AM Class - Unavailable Tools
            case "AM" -> {
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
                JOptionPane.showMessageDialog(null, scrollPane, "Tool Master Panel - Tool Report", JOptionPane.PLAIN_MESSAGE);

                // Return to tool master panel
                toolMaster(api, session);
            }

            // PM Class - Unavailable Tools
            case "PM" -> {
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
                JOptionPane.showMessageDialog(null, scrollPane, "Tool Master Panel - Tool Report", JOptionPane.PLAIN_MESSAGE);

                // Return to tool master panel
                toolMaster(api, session);
            }

            // Back to Tool Master Panel
            default ->
                toolMaster(api, session);
        }

        // Ensure return to Tool Master Panel
        toolMaster(api, session);
    }

    /**
     * The admin method controls authentication and various different admin
     * functions for the admin panel
     *
     * @param api contains methods for management
     */
    public static void admin(Tool api) {
        // Login Field
        JLabel loginMessage = new JLabel("Please enter your credentials");
        loginMessage.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        loginMessage.setForeground(Color.white);
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        Object[] loginField = {
            loginMessage,
            "Username", username,
            "Password", password
        };

        // Attempt login till session status is active
        while (!api.sessionStatus()) {
            // Login Dialog
            int option = JOptionPane.showConfirmDialog(null, loginField, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // If Ok, check login
            if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {
                JOptionPane.showMessageDialog(null, "Welcome, Login Successful", "Admin Login", JOptionPane.PLAIN_MESSAGE, null);
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
        int selection = JOptionPane.showOptionDialog(null, "Hi, Admin!\nWhat would you like to do today?", "Admin Panel", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
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
                                    JOptionPane.showMessageDialog(null, successAddMessage, "Admin Panel - Add Student", JOptionPane.PLAIN_MESSAGE, null);
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
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Disable Student", JOptionPane.PLAIN_MESSAGE, null, studentList, studentList[0]);
                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.disableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully disabled: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Disable Student", JOptionPane.PLAIN_MESSAGE, null);
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
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Re-enable Student", JOptionPane.PLAIN_MESSAGE, null, studentList, studentList[0]);

                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.enableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully re-enabled: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Re-enable Student", JOptionPane.PLAIN_MESSAGE, null);
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
            int viewStudentSelection = (int) JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - View Students", 0, JOptionPane.PLAIN_MESSAGE, null, viewStudentOptions, viewStudentOptions[0]);
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Enabled Students", JOptionPane.PLAIN_MESSAGE, null);
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Disabled Students", JOptionPane.PLAIN_MESSAGE, null);
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View All Students", JOptionPane.PLAIN_MESSAGE, null);
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
        int toolSelection = JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - Tool Management", 0, JOptionPane.PLAIN_MESSAGE, null, toolOptions, toolOptions[0]);
        switch (toolSelection) {
            case 0 -> {
                // Add a tool
                String toolName = JOptionPane.showInputDialog(null, "Enter a name for the new tool", "Admin Panel - Add Tool", JOptionPane.PLAIN_MESSAGE);
                // If yes, add 
                if (toolName != null) {
                    // If tool added
                    int toolID = api.createTool(toolName);
                    if (toolID != 0) {
                        String successAddMessage = "Tool successfully added: " + toolName + ", ID: " + toolID;
                        JOptionPane.showMessageDialog(null, successAddMessage, "Admin Panel - Add Tool", JOptionPane.PLAIN_MESSAGE);
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
                    String toolToDisable = (String) JOptionPane.showInputDialog(null, "Disable Tool", "Admin Panel - Disable Tool", JOptionPane.PLAIN_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToDisable != null) {
                        // Strip String to only ID
                        String toolID = toolToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.disableTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully disabled: " + toolToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Disable Tool", JOptionPane.PLAIN_MESSAGE, null);
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
                    String toolToDisable = (String) JOptionPane.showInputDialog(null, "Re-enable Tool", "Admin Panel - Re-enable Tool", JOptionPane.PLAIN_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToDisable != null) {
                        // Strip String to only ID
                        String toolID = toolToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.enableTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully re-enabled: " + toolToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Re-enable Tool", JOptionPane.PLAIN_MESSAGE, null);
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
            int viewToolSelection = (int) JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - View Tools", 0, JOptionPane.PLAIN_MESSAGE, null, viewToolOptions, viewToolOptions[0]);
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Enabled Tools", JOptionPane.PLAIN_MESSAGE, null);
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View Disabled Tools", JOptionPane.PLAIN_MESSAGE, null);
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Admin Panel - View All Tools", JOptionPane.PLAIN_MESSAGE, null);

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
