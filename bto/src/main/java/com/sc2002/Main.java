package com.sc2002;

import java.util.Scanner;

import com.sc2002.controller.AppContext;
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

        // AppContext service, To make things less clustered, improving readibility
        AppContext appContext = new AppContext(scanner,authService,currentUser,userList,projectList,enquiryList,applicationList);
        // Project
        System.out.println("Welcome to the BTO Project Management System!");
        while (true) {
            userInput = menuService.MainMenu(appContext.getScanner()); // We should be using VIEW to print Menu, JUST TAKE NOTE DURING MEETING AGENDA
            if (userInput.equals("1")) {
                appContext.setCurrentUser(menuService.LoginMenu(appContext.getScanner(), appContext.getUserRepo()));
            } else if (userInput.equals("2")) {
                appContext.setCurrentUser(menuService.RegisterMenu(appContext.getScanner(), appContext.getUserRepo()));// we will use this as 'login token'
            }
            while (appContext.getCurrentUser() != null) {
                // Handle different user roles using a switch-case
                if(authService.isApplicant(appContext.getCurrentUser())){
                    menuService.ApplicantMenu(appContext);
                }else if(authService.isOfficer(appContext.getCurrentUser())){
                    menuService.HDBOfficerMenu(appContext);
                }else if(authService.isManager(appContext.getCurrentUser())){
                    menuService.HDBManagerMenu(appContext);
                }
                else{
                    System.out.println("Unknown role. Logging out...");
                    appContext.setCurrentUser(null);
                }// End of if-else for checking user type
            }// end of while loop for when currentUser!=null
        }// while program is running
    }// end of public main class
}// end of main class
