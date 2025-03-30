package com.sc2002;

import java.lang.reflect.Array;
import java.util.*;

import com.sc2002.entities.User;
import com.sc2002.enums.UserRole;
import com.sc2002.entities.HDBManagerModel;
import com.sc2002.entities.ApplicantModel;
import com.sc2002.entities.HDBOfficerModel;
import com.sc2002.entities.BTOProjectModel;

import com.sc2002.Services.HDBManager;

import com.sc2002.repositories.*;
import com.sc2002.utilities.*;

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
        // Declaring the repositories
        EnquiryRepo enquiryRepo = new EnquiryRepo();
        UserRepo userList = new UserRepo();
        // Declaring variables
        User currentUser = null;
        String userInput = null;
        User newUser = null;
        // Initializing userList from Excel
        ArrayList<List<Object>> userData = XLSXReader.readUserList("ApplicantList.xlsx");
        addUserbyArrayList(userData, UserRole.APPLICANT, userList);
        userData = XLSXReader.readUserList("ManagerList.xlsx");
        addUserbyArrayList(userData, UserRole.HDB_MANAGER, userList);
        userData = XLSXReader.readUserList("OfficerList.xlsx");
        addUserbyArrayList(userData, UserRole.HDB_OFFICER, userList);
        // Project
        System.out.println("Hello world!");
        System.out.println("Welcome to the BTO Project Management System!");
        while(true){
            userInput=mainMenu(scanner);
            if(userInput.equals("1")){
                currentUser=loginMenu(scanner, userList);
            }else if(userInput.equals("2")){
                currentUser=registerMenu(scanner, userList);
            }
            while(currentUser!=null){ // we will use this as 'login token'
                // TODO: Add a switch case for the different user roles
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

    public static String mainMenu(Scanner scanner) {
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

    public static User loginMenu(Scanner scanner, UserRepo userList) {
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

    public static User registerMenu(Scanner scanner, UserRepo userList) {
        System.out.println("--Register new user--");
        // TODO: 
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void HDBManagerMenu(Scanner scanner) {
        System.out.printf("Total amount of BTO Projects: %d%n", btoProjectModels.size());

        System.out.println("\nPrinting all BTO Projects:");
        for (BTOProjectModel project : btoProjectModels) {
            project.printAll();
            System.out.println("----------------------------");
        }
    }

    // Could be a service class (TBD)
    public static void addUserbyArrayList(ArrayList<List<Object>> userData,UserRole UserType,UserRepo userList) {
        User newUser = null;
        for (List<Object> user : userData) {
            String name = (String) user.get(0);
            String nric = (String) user.get(1);
            int age = (int) user.get(2);
            String maritalStatus = (String) user.get(3);
            String password = (String) user.get(4);
            try{
                switch(UserType) { 
                    case HDB_MANAGER:
                        newUser = new HDBManagerModel(name, nric, age, maritalStatus, password);
                        break;
                    case HDB_OFFICER:
                        newUser = new HDBOfficerModel(name, nric, age, maritalStatus, password);
                        break;
                    case APPLICANT:
                    default: // Default to Applicant if no specific type is provided
                        newUser = new ApplicantModel(name, nric, age, maritalStatus, password);
                        break;
                }
                userList.addUser(newUser); // Add the new User to the list
            }catch (Exception e) {
                System.out.println("Error creating user: " + e.getMessage());
                continue; // Skip to the next user if there's an error
            }
        }
    }

}
