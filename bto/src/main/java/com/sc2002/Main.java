package com.sc2002;

import java.util.Scanner;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.AuthService;
import com.sc2002.controller.InitializationService;
import com.sc2002.model.User;
import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.EnquiryRepo;
import com.sc2002.repositories.OfficerRegistrationRepo;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;
import com.sc2002.view.mainAppView;


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
        mainAppView mainAppView = new mainAppView();
        // Declaring InitilizationService
        InitializationService initialService = new InitializationService();
        // Declaring the repositories
        EnquiryRepo enquiryList = new EnquiryRepo();
        UserRepo userList = new UserRepo();
        ProjectRepo projectList = new ProjectRepo();
        ApplicationRepo applicationList = new ApplicationRepo();
        OfficerRegistrationRepo officerRegistrationList = new OfficerRegistrationRepo();
        // Initialize userList
        initialService.initializeUsers(userList);
        // Declaring variables
        User currentUser = null;
        // AppContext service, To make things less clustered, improving readibility
        AppContext appContext = new AppContext(scanner,authService,currentUser,userList,projectList,enquiryList,applicationList, officerRegistrationList);
        // Project
        mainAppView.startMenu(appContext);
    }// end of public main class
}// end of main class
