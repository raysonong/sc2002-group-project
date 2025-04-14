package com.sc2002.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sc2002.enums.Neighborhood;
import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.HDBManagerModel;
import com.sc2002.model.HDBOfficerModel;
import com.sc2002.model.User;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;
import com.sc2002.utilities.CSVReader;
// THIS FILE CONTROLS UserService such as adding

public class InitializationService {

    /**
     * The `initializeUsers` function reads user data from Excel files and adds
     * users to a user repository based on their roles.
     *
     * @param userList The `userList` parameter is an instance of the `UserRepo`
     * class, which presumably represents a repository or collection of user
     * objects. In the `initializeUsers` method, this parameter is used to store
     * user data read from Excel files for different roles such as APPLICANT,
     * HDB_MANAGER
     */
    public void initializeUsers(UserRepo userList) {
        String projectDir = System.getProperty("user.dir") + "/bto/src/main/data";
        // Initializing userList from Excel
        ArrayList<List<Object>> userData = CSVReader.readUserList(projectDir + "/ApplicantList.csv");
        addUserbyArrayList(userData, UserRole.APPLICANT, userList);
        userData = CSVReader.readUserList(projectDir + "/ManagerList.csv");
        addUserbyArrayList(userData, UserRole.HDB_MANAGER, userList);
        userData = CSVReader.readUserList(projectDir + "/OfficerList.csv");
        addUserbyArrayList(userData, UserRole.HDB_OFFICER, userList);
    }

    // Technically this would be better somewhere else, but I assume since we only use it once in here so should be ok?
    /**
     * The function `addUserbyArrayList` takes a list of user data, user role,
     * and user repository, creates new user objects based on the user role, and
     * adds them to the user repository while handling any exceptions.
     *
     * @param userData The `userData` parameter is an `ArrayList` containing
     * lists of `Object` elements. Each list represents a user's data with the
     * following elements in order:
     * @param UserType The `UserType` parameter in the `addUserbyArrayList`
     * method is an enum type `UserRole` that represents different types of
     * users in the system. The possible values for `UserType` are
     * `HDB_MANAGER`, `HDB_OFFICER`, and `APPLICANT`.
     * @param userList The `userList` parameter in the `addUserbyArrayList`
     * method is of type `UserRepo`. It is used to store and manage a collection
     * of user objects. The method adds a new user to this list based on the
     * provided user data and user role.
     */
    private static void addUserbyArrayList(ArrayList<List<Object>> userData, UserRole UserType, UserRepo userList) {  // Private to prevent others from accessing
        User newUser = null;
        for (List<Object> user : userData) {
            String name = (String) user.get(0);
            String nric = (String) user.get(1);
            int age = (int) user.get(2);
            String maritalStatus = (String) user.get(3);
            String password = (String) user.get(4);
            try {
                switch (UserType) {
                    case HDB_MANAGER:
                        newUser = new HDBManagerModel(name, nric, age, maritalStatus, password);
                        break;
                    case HDB_OFFICER:
                        newUser = new HDBOfficerModel(name, nric, age, maritalStatus, password);
                        break;
                    case APPLICANT:
                    default: // Default to Applicant if no specific type is provided
                        newUser = new ApplicantModel(name, nric, age, maritalStatus, password, UserRole.APPLICANT);
                        break;
                }
                userList.addUser(newUser); // Add the new User to the list
            } catch (Exception e) {
                System.out.println("Error creating user: " + e.getMessage());
                continue; // Skip to the next user if there's an error
            }
        }
    }

    public void initializeProjects(ProjectRepo projectList, UserRepo userList, AuthService authService) {
        String projectDir = System.getProperty("user.dir") + "/bto/src/main/data";
        ArrayList<List<Object>> projectData = CSVReader.readProjectList(projectDir + "/ProjectList.csv");
        addProjectByArrayList(projectData, projectList, userList, authService);
    }

    private static void addProjectByArrayList(ArrayList<List<Object>> projectData, ProjectRepo projectList, UserRepo userList, AuthService authService) {
        for (List<Object> project : projectData) { // Loop the ArrayList for the lists
            try {
                // Extract project details for XLSX file
                String projectName = (String) project.get(0);
                String neighborhood = (String) project.get(1);
                String type1 = (String) project.get(2);
                int unitsType1 = (int) project.get(3);
                int priceType1 = (int) project.get(4);
                String type2 = (String) project.get(5);
                int unitsType2 = (int) project.get(6);
                int priceType2 = (int) project.get(7);
                LocalDate openingDate = (LocalDate) project.get(8);
                LocalDate closingDate = (LocalDate) project.get(9);
                String managerName = (String) project.get(10);
                int officerSlots = (int) project.get(11);
                String officerNames = (String) project.get(12); // comma separated list of Officer names
                // Retrieve manager's User Object with name
                User manager = userList.getUserByName(managerName);
                if (manager == null) {
                    System.err.println("Manager with name '" + managerName + "' not found. Skipping project.");
                    continue;
                }

                // Determine flat types and counts
                int twoRoomCount = 0, twoRoomPrice = 0, threeRoomCount = 0, threeRoomPrice = 0;
                if (type1.equalsIgnoreCase("2-Room")) {
                    twoRoomCount = unitsType1;
                    twoRoomPrice = priceType1;
                } else if (type1.equalsIgnoreCase("3-Room")) {
                    threeRoomCount = unitsType1;
                    threeRoomPrice = priceType1;
                }
                if (type2.equalsIgnoreCase("2-Room")) {
                    twoRoomCount = unitsType2;
                    twoRoomPrice = priceType2;
                } else if (type2.equalsIgnoreCase("3-Room")) {
                    threeRoomCount = unitsType2;
                    threeRoomPrice = priceType2;
                }
                // Check if neighborhood is a valid enum value
                Neighborhood neighborhoodEnum = null;
                try {
                    neighborhoodEnum = Neighborhood.valueOf(neighborhood.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid neighborhood: " + neighborhood + ". Skipping project.");
                    continue;
                }
                // Create and save the project
                BTOProjectModel newProject = new BTOProjectModel(
                        projectName, neighborhoodEnum, twoRoomCount, twoRoomPrice, threeRoomCount, threeRoomPrice,
                        openingDate, closingDate, officerSlots, manager.getUserID()
                );
                projectList.save(newProject);
                // Check for additional officers starting from index 12 of the arrayList
                String[] officerNameArray = officerNames.split(",");
                int addedOfficers = 0;

                for (String officerName : officerNameArray) {
                    if (addedOfficers >= officerSlots) {
                        break; // Stop adding officers if the maximum slots are filled
                    }

                    User officer = userList.getUserByName(officerName.trim());
                    if (officer != null && authService.isOfficer(officer)) {
                        newProject.addManagingOfficerUser(officer);
                        addedOfficers++;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing project: " + e.getMessage());
            }
        }
    }
}
