package com.billy.marinemotorsportsmanagement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    // Init vars for Tool Scan
    public static int in = 0, out = 0, studentID = 0;
    public static boolean error = false;

    public static void main(String[] args) {
        // Initiate api
        Tool api = new Tool();

        // Ensure Database Connection before continuing
        if (!api.databaseConnection()) {
            JOptionPane.showMessageDialog(null, "Database Error\nNot Connected, please contact support");
            api.exit();
        }

        // Configure UI (student)
        uiConfig(false);

        // Load main menu
        mainMenu(api);

        // Exit
        api.exit();
    }

    /**
     * The uiConfig method configures the way that the UI is displayed
     *
     * @param admin true if admin config must be loaded, else student config
     * loaded
     */
    public static void uiConfig(boolean admin) {
        // JOptionPane
        // Fonts
        UIManager.put("OptionPane.messageForeground", Color.white);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.BOLD, 32));
        UIManager.put("OptionPane.textFieldFont", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("OptionPane.listFont", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.BOLD, 32));

        // JOptionPane Size
        UIManager.put("OptionPane.border", new EmptyBorder(120, 120, 120, 120));
        UIManager.put("OptionPane.background", new Color(66, 66, 100));
        UIManager.put("Panel.background", new Color(66, 66, 100));

        // Btns
        UIManager.put("OptionPane.cancelButtonText", "Back");
        UIManager.put("OptionPane.okButtonText", "Continue");
        UIManager.put("Button.background", new Color(97, 97, 114));
        UIManager.put("Button.foreground", Color.white);

        // Text Field
        // Fonts
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 28));

        // Admin Config
        if (admin) {
            UIManager.put("Button.border", new EmptyBorder(25, 25, 25, 25));
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 20));
        } 
        // Student Config
        else {
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 32));
            UIManager.put("Button.border", new EmptyBorder(35, 50, 35, 50));
        }
    }

    /**
     * The mainMenu method displays the options Student, Admin, or exit.
     *
     * @param api the api
     */
    public static void mainMenu(Tool api) {
        // Configure UI (default, student)
        uiConfig(false);

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
                // Configure UI (teacher)
                uiConfig(true);
                admin(api);
                break;

            // exit application
            default:
                // Login Field
                // JLabel exitMessage
                JLabel exitMessage = new JLabel("Please enter admininistrator credentials to exit the application.");
                exitMessage.setFont(new Font("Segoe UI", Font.BOLD, 28));
                exitMessage.setForeground(Color.white);

                // JTextField & JLabel Username (Username field for login)
                JTextField username = new JTextField();
                JLabel usernameTitle = new JLabel("Username");
                usernameTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
                usernameTitle.setForeground(Color.white);

                // JPasswordField & JLabel Password (Password field for login)
                JPasswordField password = new JPasswordField();
                JLabel passwordTitle = new JLabel("Password");
                passwordTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
                passwordTitle.setForeground(Color.white);

                // Display Array
                Object[] loginField = {
                    exitMessage,
                    usernameTitle, username,
                    passwordTitle, password
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
                        JOptionPane.showMessageDialog(null, "Invalid credentials, please try again", "Admin Login", JOptionPane.PLAIN_MESSAGE, null);
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
        // Build Student List
        String[] studentList = new String[api.studentNameList(true, session).size()];
        studentList = api.studentNameList(true, session).toArray(studentList);
        Integer[] studentIDList = new Integer[studentList.length];
        studentIDList = api.studentIDList(true, session).toArray(studentIDList);

        // Add StudentID to Student List containing Student Name already
        for (int i = 0; i < studentList.length; i++) {
            studentList[i] += ", ID: " + studentIDList[i];
        }

        // Run quick scan till exit
        while (true) {
            // Update vars
            studentID = 0;
            in = 0;
            out = 0;
            error = false;

            // Get chosen student or exit quick scan
            String studentSelected = (String) JOptionPane.showInputDialog(null, "Select a Student", "Tool Master Panel - Quick Scan", JOptionPane.PLAIN_MESSAGE, null, studentList, studentList[0]);

            // Ensure a student was chosen, otherwise return
            if (studentSelected != null) {
                // Get student id
                studentSelected = studentSelected.replaceAll("[^0-9]+", ""); // strip everything but id
                studentID = Integer.parseInt(studentSelected);
            } else {
                // Exit quick scan
                toolMaster(api, session);
            }

            // Initialize Tool Scan GUI
            // JLabel Scan Title (display user tools being scanned for)
            JLabel scanTitle = new JLabel("Scanning tools for: " + api.getStudentName(studentID));
            scanTitle.setFont(new Font("Segoe UI", Font.BOLD, 34));
            scanTitle.setForeground(Color.white);

            // JLabel Scan Stats (borrow/return count)
            JLabel scanStats = new JLabel("Tools Borrowed: 0 | Tools Returned: 0");
            scanStats.setFont(new Font("Segoe UI", Font.PLAIN, 28));
            scanStats.setForeground(Color.white);

            // JTextArea Tools Scanned (display tools scanned in/out)
            JTextArea toolsScanned = new JTextArea();
            toolsScanned.setFont(new Font("Segoe UI", Font.PLAIN, 26));
            toolsScanned.setLineWrap(true);
            toolsScanned.setWrapStyleWord(true);
            toolsScanned.setEditable(false);

            // JScrollPane scroll (display JTextArea with scrollbar)
            JScrollPane scroll = new JScrollPane(toolsScanned);
            scroll.setPreferredSize(new Dimension(200, 350));

            // JTextField tool (Text box to read scanned tool)
            JTextField tool = new JTextField() {
                // Set focus on TextField
                public void addNotify() {
                    super.addNotify();
                    requestFocus();
                }
            };

            // Barcode Scan Handler/Listener
            tool.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    // If enter is pressed
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        // Check if text box is empty
                        if (tool.getText().isBlank()) {
                            toolsScanned.append("Invalid Scan Error\n");
                        } // Else, if text box contains MMS-, scan is valid, continue
                        else if (tool.getText().toUpperCase().contains("MMS-")) {
                            // Parse Tool ID (remove MMS-)
                            String scannedTool = tool.getText();
                            scannedTool = scannedTool.replaceAll("[^0-9]+", "");
                            int toolID = Integer.parseInt(scannedTool);

                            // Ensure scanned tool is valid & active
                            if (api.toolStatus(toolID)) {
                                // If tool isn't available, return tool
                                if (!api.toolAvailability(toolID)) {
                                    // Return success
                                    if (api.returnTool(toolID)) {
                                        toolsScanned.append("Returned: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                        in++;
                                    } // Return error
                                    else {
                                        toolsScanned.append("Return Error: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                        error = true;
                                    }
                                } // Else if tool is available, borrow tool
                                else if (api.toolAvailability(toolID)) {
                                    // Borrow success
                                    if (api.borrowTool(studentID, toolID)) {
                                        toolsScanned.append("Borrowed: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                        out++;
                                    } // Borrow error
                                    else {
                                        toolsScanned.append("Borrow Error: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                        error = true;
                                    }
                                }
                            } // Else, check if tool doesn't exist or is just inactive
                            else {
                                error = true;
                                // Tool doesn't exist
                                if (api.getToolName(toolID) == null) {
                                    toolsScanned.append("Unknown Tool Error, ID: " + toolID + "\n");
                                } // Tool exists, but is inactive
                                else {
                                    toolsScanned.append("Inactive Tool Error: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                }
                            }
                        } // Else unknown error
                        else {
                            toolsScanned.append("Unknown Scan Error\n");
                            error = true;
                        }

                        // Clear textbox input, update scan stats
                        tool.setText("");
                        scanStats.setText("Tools Borrowed: " + out + " | Tools Returned: " + in);
                    }
                }
            });

            // JButton Finish (Finish scanning button)
            JButton finish = new JButton("Finish Scan");
            finish.setFont(new Font("Segoe UI", Font.BOLD, 28));

            // Create Action Listener for finish scan button
            ActionListener finishEvent = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    // Check if any errors occured
                    if (error) {
                        // Ask user if they want to continue scanning or exit
                        // JLabel Error Titles (Title containing info/student with errors)
                        JLabel errorTitle = new JLabel("One or more errors occured scanning tools for " + api.getStudentName(studentID));
                        JLabel errorTitle2 = new JLabel("\nPlease continue if all the tools were scanned, otherwise go back to scanning");
                        errorTitle.setFont(new Font("Segoe UI", Font.BOLD, 34));
                        errorTitle.setForeground(Color.white);
                        errorTitle2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
                        errorTitle2.setForeground(Color.white);

                        // Error Display Array
                        Object[] errorDisplay = {
                            errorTitle, // Title Containing info/student with errors
                            errorTitle2,// Title Containing info/student with errors
                            scroll // Tools Scanned (display tools scanned in/out)
                        };

                        // Display Error Message
                        int cont = JOptionPane.showConfirmDialog(null, errorDisplay, "Tool Scan Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        // If OK, exit tool scan successfully
                        if (cont == JOptionPane.OK_OPTION) {
                            JOptionPane.getRootFrame().dispose();
                        }
                    } // Else if no errors, successfully exit tool scan
                    else {
                        JOptionPane.getRootFrame().dispose();
                    }
                }
            };

            // Add Finish Button Listener
            finish.addActionListener(finishEvent);

            // Tool Scan Display Array
            Object[] toolScanDisplay = {
                scanTitle, // Scan Title (display user tools being scanned for)
                scanStats, // Scan Stats (borrow/return count)
                scroll, // Tools Scanned (display tools scanned in/out)
                tool, // Tool (Text box to read scanned tool)
                finish // Finish (Finish scanning button)
            };

            // Start Student Scan Session
            JOptionPane.showOptionDialog(null, toolScanDisplay, "Tool Scan", 0, -1, null, new Object[]{}, null);
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
            JOptionPane.showMessageDialog(null, "No borrowed tools found", "Tool Master Panel - Tool Report", JOptionPane.PLAIN_MESSAGE, null);
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

        // Init unavailable tool vars
        String unavailableTools = "";
        int toolsOut = 0;
        switch (session) {
            // AM Class - Unavailable Tools
            case "AM" -> {
                // Build list of AM Class unavailable tools
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    if (allBorrowerSessionsUnavailable.get(i).equals("AM")) {
                        unavailableTools += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                                + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + "\n\n";

                        // Increment tool out counter
                        toolsOut++;
                    }
                }
            }

            // PM Class - Unavailable Tools
            case "PM" -> {
                // Build list of PM Class unavailable tools
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    if (allBorrowerSessionsUnavailable.get(i).equals("PM")) {
                        unavailableTools += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                                + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + "\n\n";

                        // Increment tool out counter
                        toolsOut++;
                    }
                }
            }

            // Back to Tool Master Panel
            default ->
                toolMaster(api, session);
        }

        // Display Results
        // JLabel Unavailable Title (display amount of tools unavailable + session)
        JLabel unavailableTitle = new JLabel("(" + toolsOut + ")" + " Unavailable Tools - " + session + " Session\n\n");
        unavailableTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        unavailableTitle.setForeground(Color.white);

        // JTextArea Text Area (display all unavailable tools)
        JTextArea textArea = new JTextArea(unavailableTools);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        // Check if no unavailable tools, and display to user if so in Text Area
        if (unavailableTools.isEmpty()) {
            textArea.setText("All tools are currently available for " + session + " Session");
        }

        // JScrollPane scroll (display JTextArea with scrollbar)
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 500));

        // Unavailable Tools Display Array
        Object[] display = {
            unavailableTitle, // Unavailable Title (display amount of tools unavailable + session)
            scroll // Text Area (display all unavailable tools)
        };

        // Display Unavailable Tools
        JOptionPane.showMessageDialog(null, display, "Tool Master Panel - Tool Report", JOptionPane.PLAIN_MESSAGE);

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
        // JLabel Login Title (self-explanatory)
        JLabel loginTitle = new JLabel("Please enter your credentials");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginTitle.setForeground(Color.white);

        // JTextField & JLabel Username (Username field for login)
        JTextField username = new JTextField();
        JLabel usernameTitle = new JLabel("Username");
        usernameTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        usernameTitle.setForeground(Color.white);

        // JPasswordField & JLabel Password (Password field for login)
        JPasswordField password = new JPasswordField();
        JLabel passwordTitle = new JLabel("Password");
        passwordTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        passwordTitle.setForeground(Color.white);

        // Login Field Display Array
        Object[] loginField = {
            loginTitle, // Login Title (self-explanatory)
            usernameTitle, username, // Username (Username field for login)
            passwordTitle, password // Password (Password field for login)
        };

        // Attempt login till session status is active
        while (!api.sessionStatus()) {
            // Display Login Dialog
            int option = JOptionPane.showConfirmDialog(null, loginField, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // If Ok, check login
            if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {
                // Display Success Message
                JOptionPane.showMessageDialog(null, "Welcome, Login Successful", "Admin Login", JOptionPane.PLAIN_MESSAGE, null);
            } // If no, exit to menu
            else if (option == JOptionPane.CANCEL_OPTION) {
                mainMenu(api);
            } // Else, login was incorrect
            else {
                // Display Invalid Credentials Message
                JOptionPane.showMessageDialog(null, "Invalid credentials, please try again", "Admin Login", JOptionPane.PLAIN_MESSAGE, null);
            }
        }

        // Initialize Options
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
        int studentSelection = JOptionPane.showOptionDialog(null, "Please select an action", "Admin Panel - Student Management", 0, JOptionPane.PLAIN_MESSAGE, null, studentOptions, studentOptions[0]);
        switch (studentSelection) {
            case 0 -> {
                // Add Student Field
                // JLabel Add Student Title (Title)
                JLabel addStudentTitle = new JLabel("Add Student");
                addStudentTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
                addStudentTitle.setForeground(Color.white);

                // JTextField & JLabel Full Name (Text box to enter Full Student Name)
                JTextField fullName = new JTextField();
                JLabel fullNameTitle = new JLabel("Full Name");
                fullNameTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
                fullNameTitle.setForeground(Color.white);

                // JTextField & JLabel Student Session (Text box to enter Student Session)
                JTextField studentSession = new JTextField();
                JLabel studentSessionTitle = new JLabel("Session (AM or PM)");
                studentSessionTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
                studentSessionTitle.setForeground(Color.white);

                // Add Student Field Display Array
                Object[] addStudentField = {
                    addStudentTitle,
                    fullNameTitle, fullName, // Full Name (Text box to enter Full Student Name)
                    studentSessionTitle, studentSession // Student Session (Text box to enter Student Session)
                };

                // Display Add Student Field
                int addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Admin Panel - Add Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If Ok, attempt student creation
                if (addStudentInfo == JOptionPane.OK_OPTION) {
                    boolean added = false;
                    // If student added
                    if (api.addStudent(fullName.getText(), studentSession.getText())) {
                        String successMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                        JOptionPane.showMessageDialog(null, successMessage, "Add Student", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else, if student not added, try again till added or cancelled
                    else {
                        while (!added) {
                            JOptionPane.showMessageDialog(null, "Student has not been added, please ensure you enter a valid session.", "Add Student", JOptionPane.PLAIN_MESSAGE, null);
                            addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Admin Panel - Add Student", JOptionPane.OK_CANCEL_OPTION);
                            // If yes, continue trying to add student
                            if (addStudentInfo == JOptionPane.YES_OPTION) {
                                if (api.addStudent(fullName.getText(), studentSession.getText())) {
                                    added = true;
                                    String successAddMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                                    JOptionPane.showMessageDialog(null, successAddMessage, "Admin Panel - Add Student", JOptionPane.PLAIN_MESSAGE, null);
                                }
                            } // If no, exit loop and return to menu
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
                            JOptionPane.showMessageDialog(null, "Student has not been disabled. Error.", "Admin Panel - Disable Student", JOptionPane.PLAIN_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagementAdmin(api);
                    }
                } // if no enabled students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to disable.", "Admin Panel - Disable Student", JOptionPane.PLAIN_MESSAGE, null);
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
                            JOptionPane.showMessageDialog(null, "Student has not been re-enabled. Error.", "Admin Panel - Re-enable Student", JOptionPane.PLAIN_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagementAdmin(api);
                    }
                } // if no disabled students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to re-enable.", "Admin Panel - Re-enable Student", JOptionPane.PLAIN_MESSAGE, null);
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
            JOptionPane.showMessageDialog(null, "No Students exist", "Admin Panel - View Students", JOptionPane.PLAIN_MESSAGE, null);
            toolManagementAdmin(api);
        } // Else continue 
        else {
            // Initialize flags & arrays
            boolean disabledStudents = false;
            boolean enabledStudents = false;
            String enabledStudentsString = "";
            String disabledStudentsString = "";

            // Check disabled students
            if (!api.studentNameList(false).isEmpty()) {
                // Update flag
                disabledStudents = true;

                // Build disabled students
                // Get disabled student list
                ArrayList<String> disabledStudentNameList = api.studentNameList(false);
                ArrayList<Integer> disabledStudentIDList = api.studentIDList(false);

                // Combine names with ids
                for (int i = 0; i < disabledStudentNameList.size(); i++) {
                    disabledStudentsString += (disabledStudentNameList.get(i) + ", ID: " + disabledStudentIDList.get(i) + ", Session: " + api.getStudentSession(disabledStudentIDList.get(i)) + ", Status: Disabled" + "\n");
                }
            }

            // Check enabled students
            if (!api.studentNameList(true).isEmpty()) {
                // Update flag
                enabledStudents = true;

                // Build enabled students
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
                // View Enabled Students
                case 0 -> {
                    // Check if enabled Students found
                    if (enabledStudents) {
                        // JLabel View Enabled Students Title (Title)
                        JLabel viewEnabledStudents = new JLabel("View Enabled Students");
                        viewEnabledStudents.setFont(new Font("Segoe UI", Font.BOLD, 28));
                        viewEnabledStudents.setForeground(Color.white);

                        // JTextArea Students Enabled (display enabled students)
                        JTextArea studentsEnabled = new JTextArea(enabledStudentsString);
                        studentsEnabled.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                        studentsEnabled.setLineWrap(true);
                        studentsEnabled.setWrapStyleWord(true);
                        studentsEnabled.setEditable(false);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new JScrollPane(studentsEnabled);
                        scroll.setPreferredSize(new Dimension(500, 500));

                        // View Enabled Students Array
                        Object[] display = {
                            viewEnabledStudents,
                            scroll
                        };

                        // Display Enabled Students
                        JOptionPane.showMessageDialog(null, display, "Admin Panel - View Enabled Students", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no enabled Students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Enabled Students exist", "Admin Panel - View Enabled Students", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // View Disabled Students
                case 1 -> {
                    // Check if Disabled Students found
                    if (disabledStudents) {
                        // JLabel View Disabled Students Title (Title)
                        JLabel viewDisabledStudents = new JLabel("View Disabled Students");
                        viewDisabledStudents.setFont(new Font("Segoe UI", Font.BOLD, 28));
                        viewDisabledStudents.setForeground(Color.white);

                        // JTextArea Students Disabled (display disabled students)
                        JTextArea studentsDisabled = new JTextArea(disabledStudentsString);
                        studentsDisabled.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                        studentsDisabled.setLineWrap(true);
                        studentsDisabled.setWrapStyleWord(true);
                        studentsDisabled.setEditable(false);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new JScrollPane(studentsDisabled);
                        scroll.setPreferredSize(new Dimension(500, 500));

                        // View Disabled Students Array
                        Object[] display = {
                            viewDisabledStudents,
                            scroll
                        };

                        // Display Disabled Students
                        JOptionPane.showMessageDialog(null, display, "Admin Panel - View Disabled Students", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no disabled students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Disabled Students exist", "Admin Panel Panel - View Disabled Students", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // View All students
                case 2 -> {
                    // Check if Disabled or Enabled Students found
                    if (disabledStudents || enabledStudents) {
                        // Combine Disabled + Enabled
                        String fullStudentString = enabledStudentsString + disabledStudentsString;

                        // JLabel View All Students Title (Title)
                        JLabel viewAllStudents = new JLabel("View All Students");
                        viewAllStudents.setFont(new Font("Segoe UI", Font.BOLD, 28));
                        viewAllStudents.setForeground(Color.white);

                        // JTextArea All Students (display all students)
                        JTextArea allStudents = new JTextArea(fullStudentString);
                        allStudents.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                        allStudents.setLineWrap(true);
                        allStudents.setWrapStyleWord(true);
                        allStudents.setEditable(false);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new JScrollPane(allStudents);
                        scroll.setPreferredSize(new Dimension(500, 500));

                        // View All Students Array
                        Object[] display = {
                            viewAllStudents,
                            scroll
                        };

                        // Display All Students
                        JOptionPane.showMessageDialog(null, display, "Admin Panel - View All Students", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no students exist (at all)
                    else {
                        JOptionPane.showMessageDialog(null, "No Students exist", "Tool Master Panel - View All Students", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // Return to Student management
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
                        JOptionPane.showMessageDialog(null, "Tool has not been added, error.", "Admin Panel - Add Tool", JOptionPane.PLAIN_MESSAGE);
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
                            JOptionPane.showMessageDialog(null, "Tool has not been disabled. Error.", "Admin Panel - Disable Tool", JOptionPane.PLAIN_MESSAGE, null);
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
                    JOptionPane.showMessageDialog(null, "There are no tools to disable.", "Admin Panel - Disable Tool", JOptionPane.PLAIN_MESSAGE, null);
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
                            JOptionPane.showMessageDialog(null, "Tool has not been re-enabled. Error.", "Admin Panel - Re-enable Tool", JOptionPane.PLAIN_MESSAGE, null);
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
                    JOptionPane.showMessageDialog(null, "There are no tools to re-enable.", "Admin Panel - Re-enable Tool", JOptionPane.PLAIN_MESSAGE, null);
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
            JOptionPane.showMessageDialog(null, "No Tools exist", "Admin Panel - View Tools", JOptionPane.PLAIN_MESSAGE, null);
            toolManagementAdmin(api);
        } // Else continue 
        else {
            // Initialize flags & arrays
            boolean disabledTools = false;
            boolean enabledTools = false;
            String enabledToolsString = "";
            String disabledToolsString = "";

            // Check Disabled Tools
            if (!api.toolNameList(false).isEmpty()) {
                // Update flag
                disabledTools = true;

                // Build Disabled tools
                // Get disabled tool list
                ArrayList<String> disabledToolNameList = api.toolNameList(false);
                ArrayList<Integer> disabledToolIDList = api.toolIDList(false);

                // Combine names with ids
                for (int i = 0; i < disabledToolNameList.size(); i++) {
                    disabledToolsString += (disabledToolNameList.get(i) + ", ID: " + disabledToolIDList.get(i) + ", Status: Disabled" + "\n");
                }
            }

            // Check Enabled tools
            if (!api.toolNameList(true).isEmpty()) {
                // Update flag
                enabledTools = true;

                // Build enabled tools
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
                // View Enabled Tools
                case 0 -> {
                    // Check if enabled tools found
                    if (enabledTools) {
                        // JTextArea Enabled Tools (display all enabled tools)
                        JTextArea toolsEnabled = new JTextArea(enabledToolsString);
                        toolsEnabled.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                        toolsEnabled.setLineWrap(true);
                        toolsEnabled.setWrapStyleWord(true);
                        toolsEnabled.setEditable(false);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new JScrollPane(toolsEnabled);
                        scroll.setPreferredSize(new Dimension(500, 500));

                        // Display Enabled Tools
                        JOptionPane.showMessageDialog(null, scroll, "Admin Panel - View Enabled Tools", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no enabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Enabled Tools exist", "Admin Panel - View Enabled Tools", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // View Disabled Tools
                case 1 -> {
                    // Check if Disabled Tools found
                    if (disabledTools) {
                        // JTextArea Disabled Tools (display all disabled tools)
                        JTextArea toolsDisabled = new JTextArea(disabledToolsString);
                        toolsDisabled.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                        toolsDisabled.setLineWrap(true);
                        toolsDisabled.setWrapStyleWord(true);
                        toolsDisabled.setEditable(false);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new JScrollPane(toolsDisabled);
                        scroll.setPreferredSize(new Dimension(500, 500));

                        /// Display Disabled Tools
                        JOptionPane.showMessageDialog(null, scroll, "Admin Panel - View Disabled Tools", JOptionPane.PLAIN_MESSAGE, null);
                    } // else no disabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Disabled Tools exist", "Admin Panel Panel - View Disabled Tools", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // View All Tools
                case 2 -> {
                    // check if disabled or enabled tools found
                    if (disabledTools || enabledTools) {
                        // Combine both disabled + enabled
                        String allTools = enabledToolsString + disabledToolsString;

                        // JTextArea toolsAll (display all tools)
                        JTextArea toolsAll = new JTextArea(allTools);
                        toolsAll.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                        toolsAll.setLineWrap(true);
                        toolsAll.setWrapStyleWord(true);
                        toolsAll.setEditable(false);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new JScrollPane(toolsAll);
                        scroll.setPreferredSize(new Dimension(500, 500));

                        // Display All Tools
                        JOptionPane.showMessageDialog(null, scroll, "Admin Panel - View All Tools", JOptionPane.PLAIN_MESSAGE, null);

                    } // Else no Disabled or Enabled Tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Tools exist", "Tool Master Panel - View All Tools", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // Return to tool management
                default ->
                    toolManagementAdmin(api);
            }
        }
        // Return to view tools panel once completed
        viewToolsAdmin(api);
    }
}
