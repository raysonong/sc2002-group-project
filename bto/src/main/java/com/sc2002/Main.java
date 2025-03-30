package com.sc2002;

import java.lang.reflect.Array;
import java.util.*;


import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.HDBManagerModel;
import com.sc2002.model.HDBOfficerModel;
import com.sc2002.model.User;
import com.sc2002.repositories.*;
import com.sc2002.utilities.*;
import com.sc2002.controller.MenuService;
import com.sc2002.controller.InitializationService;
/**
 * The main entry point for the BTO project management application.
 */
public class Main {

    /**
     * The main method to run the application.
     *
     * @param args Command-line arguments (not used).
     */
    private ArrayList<BTOProjectModel> btoProjectModels = new ArrayList<>();
    

    public static void main(String[] args) {
        // Declaring Scanner
        Scanner scanner = new Scanner(System.in);
        // Declaring MenuManagerService
        MenuService menuService = new MenuService();
        // Declaring InitilizationService
        InitializationService initialService = new InitializationService();
        // Declaring the repositories
        EnquiryRepo enquiryRepo = new EnquiryRepo();
        UserRepo userList = new UserRepo();
        // Initialize userList
        initialService.initializeUsers(userList);
        // Declaring variables
        User currentUser = null;
        String userInput = null;
        User newUser = null;
        
        // Project
        System.out.println("Hello world!");
        System.out.println("Welcome to the BTO Project Management System!");
        while(true){
            userInput=menuService.MainMenu(scanner);
            if(userInput.equals("1")){
                currentUser=menuService.LoginMenu(scanner, userList);
            }else if(userInput.equals("2")){
                currentUser=menuService.RegisterMenu(scanner, userList);
            }
            while(currentUser!=null){ // we will use this as 'login token'
                // TODO: Add a switch case for the different user roles
                System.out.println("Logging out...");
                currentUser=null;
            }
        }

        // // Testing Code
        // HDBManagerModel hdbManagerModel1 = new HDBManagerModel("Tew",
        //                                                         "123",
        //                                                         18,
        //                                                         "Married",
        //                                                         "123"
        //                                                         );
        // HDBManagerModel hdbManagerModel2 = new HDBManagerModel("Huw",
        //                                                         "126",
        //                                                         18,
        //                                                         "Married",
        //                                                         "123"
        //                                                         );

        // ArrayList<HDBManagerModel> hDBManagerModels = new ArrayList<>();
        // hDBManagerModels.add(hdbManagerModel1);
        // hDBManagerModels.add(hdbManagerModel2);

        // HDBManager hdbManager = new HDBManager(hdbManagerModel1);

        // String input = null;

    }




}
