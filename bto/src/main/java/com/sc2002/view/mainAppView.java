package com.sc2002.view;

import java.util.Scanner;

import com.sc2002.model.UserModel;
import com.sc2002.repositories.UserRepo;
import com.sc2002.services.UserService;
import com.sc2002.utilities.NRICValidator;
import com.sc2002.utilities.PasswordValidator;
import com.sc2002.config.AppContext;
import com.sc2002.controllers.UserController;

public class mainAppView {

    private final UserController userController = new UserController();

    public void startMenu(AppContext appContext) {
        System.out.println("Welcome to the BTO Project Management System!");
        String userInput = null;
        ApplicantView applicantView = new ApplicantView();
        HDBOfficerView officerView = new HDBOfficerView();
        HDBManagerView managerView = new HDBManagerView();
        while (true) {
            userInput = MainMenu(appContext.getScanner());
            if (userInput.equals("1")) {
                appContext.setCurrentUser(LoginMenu(appContext.getScanner(), appContext.getUserRepo()));
            } else if (userInput.equals("2")) {
                appContext.setCurrentUser(RegisterMenu(appContext.getScanner(), appContext.getUserRepo()));// we will use this as 'login token'
            }
            while (appContext.getCurrentUser() != null) {
                // Handle different user roles using a switch-case
                if (appContext.getAuthService().isApplicant(appContext.getCurrentUser())) {
                    applicantView.ApplicantMenu(appContext);
                } else if (appContext.getAuthService().isOfficer(appContext.getCurrentUser())) {
                    officerView.HDBOfficerMenu(appContext);
                } else if (appContext.getAuthService().isManager(appContext.getCurrentUser())) {
                    managerView.HDBManagerMenu(appContext);
                } else {
                    System.out.println("Unknown role. Logging out...");
                    appContext.setCurrentUser(null);
                }// End of if-else for checking user type
            }// end of while loop for when currentUser!=null
        }// while program is running
    }

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
    public UserModel LoginMenu(Scanner scanner, UserRepo userList) {
        String nric;
        String password;
        UserModel currentUser = null;

        while (true) {
            System.out.println("\n--Login to your account--");
            System.out.print("Please enter your NRIC: ");
            nric = scanner.nextLine().trim();

            // Validate NRIC format
            if (!NRICValidator.isValidNRIC(nric)) {
                System.out.println("Invalid NRIC format. Please enter a valid Singapore NRIC/FIN.");
                continue;
            }

            System.out.print("Please enter your password: ");
            password = scanner.nextLine();

            currentUser = userController.authenticateUser(nric, password, userList);

            if (currentUser != null) {
                System.out.println("Login successful!");

                if ("password".equals(password.toLowerCase())) { // check if it is 1st login
                    System.out.println("Please reset your password");
                    userController.resetPassword(currentUser, scanner);
                }

                return currentUser;
            } else {
                System.out.println("Would you like to try again? (yes/no): ");
                String option = scanner.nextLine().trim().toLowerCase();
                if (!option.equals("yes")) {
                    return null;
                }
            }
        }
    }

    public UserModel RegisterMenu(Scanner scanner, UserRepo userList) {
        System.out.println("\n--Register new user--");

        String nric;
        String name;
        int age;
        String maritalStatus;
        String password = "";
        String confirmPassword = "";

        // Input and validate NRIC
        while (true) {
            System.out.print("Enter your NRIC (e.g., S1234567D): ");
            nric = scanner.nextLine().trim();

            // Validate NRIC format
            if (!NRICValidator.isValidNRIC(nric)) {
                System.out.println("Invalid NRIC format. Please enter a valid NRIC (Start with T or S).");
                continue;
            }

            // Check if NRIC exists (using uppercase for consistency)
            if (userList.getUserByNRIC(NRICValidator.formatNRIC(nric)) != null) {
                System.out.println("An account with this NRIC already exists. Please login instead.");
                System.out.print("Press enter to continue...");
                scanner.nextLine();
                return null;
            }

            break;
        }

        // Input name
        while (true) {
            System.out.print("Enter your full name: ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
                continue;
            }

            break;
        }

        // Input age, we check for age atleast 21. Since only age 21 can apply for BTO
        while (true) {
            System.out.print("Enter your age: ");
            try {
                age = Integer.parseInt(scanner.nextLine().trim());

                if (age < 21) {
                    System.out.println("You must be at least 21 years old to register.");
                    continue;
                }

                if (age > 120) {
                    System.out.println("Please enter a valid age.");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for age.");
            }
        }

        // Input marital status
        while (true) {
            System.out.print("Enter your marital status (Married/Single): ");
            maritalStatus = scanner.nextLine().trim();

            if (!maritalStatus.equalsIgnoreCase("Married") && !maritalStatus.equalsIgnoreCase("Single")) {
                System.out.println("Please enter either 'Married' or 'Single'.");
                continue;
            }

            break;
        }

        // Input password, by right Ass. document ask for default password, but thats bad ? right ?
        do {
            if (password.isEmpty()) {
                System.out.print("Enter your password: ");
                password = scanner.nextLine();

                if (!PasswordValidator.isValid(password)) {
                    for (String message : PasswordValidator.getValidationMessages(password)) {
                        System.out.println(message);
                    }
                    password = "";
                    continue;
                }
            }

            System.out.print("Confirm your password: ");
            confirmPassword = scanner.nextLine();

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
            }
        } while (!PasswordValidator.isValid(password) || !password.equals(confirmPassword));

        // Register user using service
        UserModel newUser = userController.registerApplicant(nric, name, age, maritalStatus, password, userList);

        if (newUser != null) {
            System.out.println("Registration successful! You are now logged in.");
            return newUser;
        } else {
            System.out.println("Registration failed. Please try again later.");
            System.out.print("Press enter to continue...");
            scanner.nextLine();
            return null;
        }
    }

}
