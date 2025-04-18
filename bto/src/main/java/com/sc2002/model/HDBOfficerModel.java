package com.sc2002.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sc2002.enums.FlatType; // for FlatType enums
import com.sc2002.enums.UserRole;

/**
 * Represents an HDB Officer user in the BTO Management System.
 * Extends ApplicantModel (consider if extending UserModel directly is more appropriate)
 * and provides officer-specific functionalities and menu options.
 * Officers manage specific projects and handle applications/enquiries related to them.
 */
public class HDBOfficerModel extends ApplicantModel {

    /** The ID of the project this officer is currently assigned to manage. */
    private int projectID;
    /** Status indicating if the officer is actively registered to manage a project. */
    private boolean registrationStatus; // true if registered, false if not

    /** The type of flat booked by the officer (if they applied as an applicant). Consider removing if redundant. */
    private FlatType bookedFlatType; // view com.sc2002.enums.FlatType for more info

    /** The BTO project object the officer has booked a flat in (if they applied as an applicant). Consider removing if redundant. */
    private BTOProjectModel bookedProject;

    /**
     * Constructs a new HDBOfficerModel instance.
     * Initializes the officer using the superclass constructor and sets the role to HDB_OFFICER.
     *
     * @param name The officer's full name.
     * @param nric The officer's NRIC.
     * @param age The officer's age.
     * @param isMarried A string indicating marital status ("Married" or "Single").
     * @param password The officer's chosen plain text password.
     */
    public HDBOfficerModel(String name, String nric, int age, String isMarried, String password) {
        super(name, nric, age, isMarried, password, UserRole.HDB_OFFICER);
    }

    /**
     * Sets the ID of the project this officer is assigned to manage.
     * @param projectID The project ID to set.
     */
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    /**
     * Gets the ID of the project this officer is currently assigned to manage.
     * @return The project ID.
     */
    public int getProjectID() {
        return this.projectID;
    }

    /**
     * Gets the registration status of the officer for managing a project.
     * @return True if registered, false otherwise.
     */
    public boolean getRegistrationStatus() {
        return this.registrationStatus;
    }

    /**
     * Retrieves the menu options available to the HDB Officer.
     * Inherits options from ApplicantModel and adds officer-specific options.
     *
     * @return A list of menu options for the officer.
     */
    @Override
    public List<String> getMenuOptions() {
        List<String> options = new ArrayList<>();
        options.addAll(super.getMenuOptions()); // Inherit Applicant's options
        options.addAll(Arrays.asList(
                "Register for Project Team",
                "View Registration Status",
                "Manage Applications",
                "Generate Flat Selection Receipt",
                "Manage Enquiries",
                "Reset Password",
                "Logout"
        ));
        return options;
    }
}
