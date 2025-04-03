package com.sc2002;

import java.util.Scanner;

import com.sc2002.controller.AuthService;
import com.sc2002.controller.InitializationService;
import com.sc2002.controller.MenuService;
import com.sc2002.model.User;
import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.EnquiryRepo;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;

/**
 * The main entry point for the BTO project management application.
 */
public class Main {

    /**
     * The main method to run the application.
     *
     * @param args Command-line arguments (not used).
     */
    // private ArrayList<BTOProjectModel> btoProjectModels = new ArrayList<>();
    public static void main(String[] args) {
        // Declaring Scanner
        Scanner scanner = new Scanner(System.in);
        // Declaring AuthService
        AuthService authService = new AuthService();
        // Declaring MenuManagerService
        MenuService menuService = new MenuService();
        // Declaring InitilizationService
        InitializationService initialService = new InitializationService();
        // Declaring the repositories
        EnquiryRepo enquiryList = new EnquiryRepo();
        UserRepo userList = new UserRepo();
        ProjectRepo projectList = new ProjectRepo();
        ApplicationRepo applicationList = new ApplicationRepo();
        // Initialize userList
        initialService.initializeUsers(userList);
        // Declaring variables
        User currentUser = null;
        String userInput = null;

        // Project
        System.out.println("Welcome to the BTO Project Management System!");
        while (true) {
            userInput = menuService.MainMenu(scanner); // We should be using VIEW to print Menu, JUST TAKE NOTE DURING MEETING AGENDA
            if (userInput.equals("1")) {
                currentUser = menuService.LoginMenu(scanner, userList);
            } else if (userInput.equals("2")) {
                currentUser = menuService.RegisterMenu(scanner, userList);// we will use this as 'login token'
            }
            while (currentUser != null) {
                // Handle different user roles using a switch-case
                if(authService.isApplicant(currentUser)){
                    currentUser = menuService.ApplicantMenu(scanner,
                                                            authService,
                                                            currentUser,
                                                            userList,
                                                            projectList,
                                                            enquiryList,
                                                            applicationList
                                                            );
                }else if(authService.isOfficer(currentUser)){
                    currentUser = menuService.HDBOfficerMenu(scanner,
                                                            authService,
                                                            currentUser,
                                                            userList,
                                                            projectList,
                                                            enquiryList,
                                                            applicationList
                                                            );
                }else if(authService.isOfficer(currentUser)){
                    currentUser = menuService.HDBManagerMenu(scanner,
                                                            authService,
                                                            currentUser,
                                                            userList,
                                                            projectList,
                                                            enquiryList,
                                                            applicationList
                                                            );
                }
                else{
                    System.out.println("Unknown role. Logging out...");
                    currentUser = null;
                }// End of if-else for checking user type
            }// end of while loop for when currentUser!=null
        }// while program is running
    }// end of public main class
}// end of main class
