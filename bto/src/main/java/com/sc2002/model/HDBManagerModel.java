package com.sc2002.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sc2002.enums.UserRole; // for FlatType enums

/**
 * Represents an HDB Manager user in the BTO Management System.
 * Extends UserModel and provides manager-specific functionalities and menu options.
 * Managers typically have higher privileges than HDB Officers, such as project creation/deletion.
 */
public class HDBManagerModel extends UserModel {

    /**
     * The projectID that they are handling at the moment
     */
    private Integer projectID;

    /**
     * The constructor
     *
     * @param name The name of the user
     * @param nric The nric of the user
     * @param age The age of the user
     * @param isMarried Marital Status of the user
     * @param password The password of the account
     */
    public HDBManagerModel(String name, String nric, int age, String isMarried, String password) {
        super(nric, name, age, isMarried, password, UserRole.HDB_MANAGER);
    }

    /**
     * To set the project ID
     *
     * @param projectID the project ID they are handling
     */
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    /**
     * To delete the project ID
     */
    public void deleteProjectID() {
        this.projectID = null;
    }

    /**
     * To get the project ID
     *
     * @return the project ID
     */
    public Integer getProjectID() {
        return this.projectID;
    }

    @Override
    public List<String> getMenuOptions() { // Handles the printing of menu options for applicant, each user has their own menu options
        List<String> options = new ArrayList<>();
        //options.addAll(super.getMenuOptions()); // Inherit HDBOfficer's options
        options.addAll(Arrays.asList("View Project (Filterable)","Create BTO Project", "Edit BTO Project",
                "Delete BTO Project", "Toggle Project Visibility", "View All Project",
                "View Your Projects", "View Enquiries", "Reply Enquiries",
                "Approve Officer Registration", "Reject Officer Registration",
                "Approve Application", "Reject Application", "Approve Withdrawal",
                "Reject Withdrawal", "Generate Reports", "Reset Password", "Logout"));
        return options;
    }
}
