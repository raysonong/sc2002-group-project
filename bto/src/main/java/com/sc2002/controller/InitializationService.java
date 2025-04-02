package com.sc2002.controller;

import java.util.ArrayList;
import java.util.List;

import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.HDBManagerModel;
import com.sc2002.model.HDBOfficerModel;
import com.sc2002.model.User;
import com.sc2002.repositories.UserRepo;
import com.sc2002.utilities.XLSXReader;
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
        String projectDir = System.getProperty("user.dir") + "/sc2002-group-project/bto/src/main/data";
        // Initializing userList from Excel
        ArrayList<List<Object>> userData = XLSXReader.readUserList(projectDir + "/ApplicantList.xlsx");
        addUserbyArrayList(userData, UserRole.APPLICANT, userList);
        userData = XLSXReader.readUserList(projectDir + "/ManagerList.xlsx");
        addUserbyArrayList(userData, UserRole.HDB_MANAGER, userList);
        userData = XLSXReader.readUserList(projectDir + "/OfficerList.xlsx");
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
                        newUser = new ApplicantModel(name, nric, age, maritalStatus, password);
                        break;
                }
                userList.addUser(newUser); // Add the new User to the list
            } catch (Exception e) {
                System.out.println("Error creating user: " + e.getMessage());
                continue; // Skip to the next user if there's an error
            }
        }
    }
}
