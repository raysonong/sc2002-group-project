package com.sc2002.controller;

import java.util.ArrayList;
import java.util.Scanner;


import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.HDBManagerModel;
import com.sc2002.model.HDBOfficerModel;
import com.sc2002.model.User; 
import com.sc2002.repositories.*;
import com.sc2002.utilities.*;

public class MenuService {

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
    public User LoginMenu(Scanner scanner, UserRepo userList) {
        String nric;
        String password;
        User currentUser = null;
        while(true){
            System.out.println("--Login to your account--");
            System.out.print("Please enter your NRIC: ");
            nric = scanner.nextLine();
            // TODO: Add NRIC validation (REMEMBER TO OFF CASE SENSITIVITY)
            System.out.print("Please enter your password: ");
            password = scanner.nextLine();

            currentUser = userList.getUserByNRIC(nric);

            try{
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

    public void HDBManagerMenu(Scanner scanner, ArrayList<BTOProjectModel> btoProjectModels) {
        System.out.printf("Total amount of BTO Projects: %d%n", btoProjectModels.size());

        System.out.println("\nPrinting all BTO Projects:");
        for (BTOProjectModel project : btoProjectModels) {
            project.printAll();
            System.out.println("----------------------------");
        }
    }

}
