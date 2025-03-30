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
        public void initializeUsers(UserRepo userList){
            // Initializing userList from Excel
            ArrayList<List<Object>> userData = XLSXReader.readUserList("ApplicantList.xlsx");
            addUserbyArrayList(userData, UserRole.APPLICANT, userList);
            userData = XLSXReader.readUserList("ManagerList.xlsx");
            addUserbyArrayList(userData, UserRole.HDB_MANAGER, userList);
            userData = XLSXReader.readUserList("OfficerList.xlsx");
            addUserbyArrayList(userData, UserRole.HDB_OFFICER, userList);
        }

        // Technically this would be better somewhere else, but I assume since we only use it once in here so should be ok?
        private static void addUserbyArrayList(ArrayList<List<Object>> userData,UserRole UserType,UserRepo userList) { // Private to prevent others from accessing
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
