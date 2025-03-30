package com.sc2002.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is a model class that stores the information of each HDB Manager
 */
import com.sc2002.enums.FlatType; // for FlatType enums
import com.sc2002.enums.UserRole; // for UserRole enums

public class HDBManagerModel extends User {

    /**
     * The projectID that they are handling
     */
    private int projectID;

    /**
     * The constructor
     * 
     * @param name      The name of the user
     * @param nric      The nric of the user
     * @param age       The age of the user
     * @param isMarried Marital Status of the user
     * @param password  The password of the account
     */
    public HDBManagerModel(String name, String nric, int age, String isMarried, String password) {
        super(nric, name, age, isMarried, password, UserRole.HDB_MANAGER);
    }

    /**
     * To set the project ID
     * 
     * @param projectID the project ID they are handling
     */
    private void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    /**
     * To get the project ID
     * 
     * @return the project ID
     */
    private int getProjectID() {
        return this.projectID;
    }

    @Override
    public List<String> getMenuOptions() {
        List<String> options = new ArrayList<>();
        //options.addAll(super.getMenuOptions()); // Inherit HDBOfficer's options
        options.addAll(Arrays.asList("Create BTO Project", "Edit BTO Project",
                "Delete BTO Project", "Toggle Project Visibility", "View Project Details",
                "Approve Officer Registration", "Reject Officer Registration",
                "Approve Application", "Reject Application", "Approve Withdrawal",
                "Reject Withdrawal", "Generate Reports"));
        return options;
    }
}