package com.sc2002.view;

import java.util.List;
import java.util.Scanner;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.OfficerRegistrationService;
import com.sc2002.controller.ProjectManagementService;
import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.model.User;
import com.sc2002.repositories.UserRepo;
import com.sc2002.view.ApplicantView;
import com.sc2002.view.HDBOfficerView;
import com.sc2002.view.HDBManagerView;
public class mainAppView {
    public void startMenu(AppContext appContext){
        System.out.println("Welcome to the BTO Project Management System!");
        String userInput=null;
        ApplicantView applicantView = new ApplicantView();
        HDBOfficerView officerView = new HDBOfficerView();
        HDBManagerView managerView = new HDBManagerView();
        while (true) {
            userInput = MainMenu(appContext.getScanner());
            if(userInput.equals("1")){
                appContext.setCurrentUser(LoginMenu(appContext.getScanner(), appContext.getUserRepo()));
            } else if (userInput.equals("2")) {
                appContext.setCurrentUser(RegisterMenu(appContext.getScanner(), appContext.getUserRepo()));// we will use this as 'login token'
            }
            while (appContext.getCurrentUser() != null) {
                // Handle different user roles using a switch-case
                if(appContext.getAuthService().isApplicant(appContext.getCurrentUser())){
                    applicantView.ApplicantMenu(appContext);
                }else if(appContext.getAuthService().isOfficer(appContext.getCurrentUser())){
                    officerView.HDBOfficerMenu(appContext);
                }else if(appContext.getAuthService().isManager(appContext.getCurrentUser())){
                    managerView.HDBManagerMenu(appContext);
                }
                else{
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
    public User LoginMenu(Scanner scanner, UserRepo userList) {
        String nric;
        String password;
        User currentUser = null;
        while (true) {
            System.out.println("--Login to your account--");
            System.out.print("Please enter your NRIC: ");
            nric = scanner.nextLine();
            // TODO: Add NRIC validation (REMEMBER TO OFF CASE SENSITIVITY) & DO CHECKING
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






}
