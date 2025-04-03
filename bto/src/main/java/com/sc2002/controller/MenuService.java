package com.sc2002.controller;

import java.util.List;
import java.util.Scanner;

import com.sc2002.model.User;
import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.EnquiryRepo;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;

public class MenuService {

    /**
     * The MainMenu function displays a menu with options for login,
     * registration, and exiting the application, and returns the user's choice.
     *
     * @param scanner The `scanner` parameter in your `MainMenu` method is of
     * type `Scanner`. This parameter is used to read input from the user. In
     * your code, you are using `scanner.nextLine()` to read the user's input
     * for selecting an option from the main menu.
     *
     * @return The `MainMenu` method returns a String value based on the user
     * input. "1" for login, "2" for register a new account. If the user enters
     * "3", the application prints a message and exits. If the user enters any
     * other value, it prompts the user to try again.
     */
    public String MainMenu(Scanner scanner) {
        String userInput;
        while (true) {
            System.out.println("--Main Menu--");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Please select an option: ");
            userInput = scanner.nextLine();
            switch (userInput) {
                case "1":
                    return "1";
                case "2":
                    return "2";
                case "3":
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * The `LoginMenu` function in Java takes user input for NRIC and password,
     * authenticates the user, and returns the current user if successful.
     *
     * @param scanner The `Scanner` class in Java is used for obtaining user
     * input from the console. It allows you to read different types of input
     * such as strings, numbers, etc. In the `LoginMenu` method you provided,
     * the `Scanner scanner` parameter is used to read user input for NRIC and
     * @param userList The `userList` parameter in your `LoginMenu` method seems
     * to be an instance of a `UserRepo` class, which likely contains a
     * collection of user objects. This parameter is used to retrieve a user
     * object based on the provided NRIC during the login process.
     * @return The method `LoginMenu` is returning a `User` object, which
     * represents the user who has successfully logged in.
     */
    public User LoginMenu(Scanner scanner, UserRepo userList) {
        String nric;
        String password;
        User currentUser = null;
        while (true) {
            System.out.println("--Login to your account--");
            System.out.print("Please enter your NRIC: ");
            nric = scanner.nextLine();
            // TODO: Add NRIC validation (REMEMBER TO OFF CASE SENSITIVITY)
            System.out.print("Please enter your password: ");
            password = scanner.nextLine();

            currentUser = userList.getUserByNRIC(nric);

            try {
                currentUser.authenticate(password);
                System.out.println("Login successful!");
                return currentUser;
            } catch (Exception e) {
                System.out.println("Login failed: " + e.getMessage());
            }
        }
    }

    public User RegisterMenu(Scanner scanner, UserRepo userList) {
        System.out.println("--Register new user--");
        // TODO: 
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * Menu Options: "Create BTO Project", "Edit BTO Project", "Delete BTO
     * Project", "Toggle Project Visibility", "View All Project", "View Project
     * Details", "Approve Officer Registration", "Reject Officer Registration",
     * "Approve Application", "Reject Application", "Approve Withdrawal",
     * "Reject Withdrawal", "Generate Reports", "Logout"
     */
    public User HDBManagerMenu(Scanner scanner, AuthService authService, User currentUser, UserRepo userList, ProjectRepo projectList, EnquiryRepo enquiryList, ApplicationRepo applicationList) {
        // TODO: Menu for HDB Manager
        String userInput = "";
        List<String> menus = currentUser.getMenuOptions();

        // Service declaration
        ProjectManagementService projectManagementService = new ProjectManagementService();

        System.out.println("--HDB Manager Menu--");
        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.print("Please select an option: ");
        userInput = scanner.nextLine();

        switch (userInput) { // violates s-SRP for (SOLID), could be implemented better later-on
            case "1" -> {
            // Option 1: Create a new BTO project
            projectList.save(projectManagementService.createProject(projectList.getLastProjectID(), scanner, currentUser));
            }
            case "2" -> {
            // Option 2: Edit an existing BTO project
            }
            case "3" -> {
            // Option 3: Delete an existing BTO project
            }
            case "4" -> {
            // Option 4: Toggle the visibility of a BTO project
            }
            case "5" -> {
            // Option 5: View all BTO projects
            }
            case "6" -> {
            // Option 6: View details of a specific BTO project
            }
            case "7" -> {
            // Option 7: Approve officer registration
            }
            case "8" -> {
            // Option 8: Reject officer registration
            }
            case "9" -> {
            // Option 9: Approve an application
            }
            case "10" -> {
            // Option 10: Reject an application
            }
            case "11" -> {
            // Option 11: Approve a withdrawal request
            }
            case "12" -> {
            // Option 12: Reject a withdrawal request
            }
            case "13" -> {
            // Option 13: Generate reports
            }
            case "14" -> {
            // Option 14: Logout
            System.out.println("Logging out...");
            return null;
            }
            default -> {
            // Invalid option selected
            System.out.println("Please select a valid option!");
            }
        }

        return currentUser;
    }

    public User ApplicantMenu(Scanner scanner, AuthService authService, User currentUser, UserRepo userList, ProjectRepo projectList, EnquiryRepo enquiryList, ApplicationRepo applicationList) {
        // TODO: Menu for Applicant
        System.out.println("Applicant Menu:");
        return currentUser;
    }

    public User HDBOfficerMenu(Scanner scanner, AuthService authService, User currentUser, UserRepo userList, ProjectRepo projectList, EnquiryRepo enquiryList, ApplicationRepo applicationList) {
        // TODO: Menu for HDB Officer
        System.out.println("HDB Officer Menu:");
        return currentUser;
    }

}
