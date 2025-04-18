package com.sc2002;

import java.util.Scanner;

import com.sc2002.model.UserModel;

import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.EnquiryRepo;
import com.sc2002.repositories.OfficerRegistrationRepo;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;

import com.sc2002.config.AppContext;

import com.sc2002.controllers.AuthController;
import com.sc2002.controllers.InitializationController;

import com.sc2002.view.mainAppView;


/**
 * The main entry point for the BTO Management System application.
 * Initializes the application context, loads initial data, and starts the main application view loop.
 */
public class Main {

    /**
     * Default constructor for the Main class.
     */
    public Main() {
        // Default constructor
    }

    /**
     * The main method to run the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Declaring Scanner
        Scanner scanner = new Scanner(System.in);
        // Declaring AuthService
        AuthController authController = new AuthController();
        // Declaring MenuManagerService
        mainAppView mainAppView = new mainAppView();
        // Declaring InitilizationService
        InitializationController initController = new InitializationController();
        // Declaring the repositories
        EnquiryRepo enquiryList = new EnquiryRepo();
        UserRepo userList = new UserRepo();
        ProjectRepo projectList = new ProjectRepo();
        ApplicationRepo applicationList = new ApplicationRepo();
        OfficerRegistrationRepo officerRegistrationList = new OfficerRegistrationRepo();
        // Initialize userList
        initController.initializeUsers(userList);
        initController.initializeProjects(projectList,userList,authController);
        // Declaring variables
        UserModel currentUser = null;
        // AppContext service, To make things less clustered, improving readibility
        AppContext appContext = new AppContext(scanner,authController,currentUser,userList,projectList,enquiryList,applicationList, officerRegistrationList);
        // Project
        try{
            mainAppView.startMenu(appContext);
        }catch (Exception e){
            System.out.println("A critical occurred: " + e.getMessage());
            System.out.println("Exiting Program.");
        }finally{
            scanner.close();
        }
        
    }// end of public main class
}// end of main class
