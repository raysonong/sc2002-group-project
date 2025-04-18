package com.sc2002.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sc2002.enums.FlatType; // for FlatType enums
import com.sc2002.enums.UserRole; // for UserRole enums

/**
 * Represents an Applicant user in the BTO Management System.
 * This class extends the abstract {@link UserModel}, inheriting common user attributes and behaviors.
 * It adds specific details relevant to applicants, such as tracking applied projects or booked flats (though some fields might be redundant or better managed elsewhere, e.g., via BTOApplicationModel).
 * Implements role-specific behavior like providing applicant-specific menu options.
 */
public class ApplicantModel extends UserModel {
    /**
     * The ID of the BTO project the applicant has applied to. May not be needed. Do check before code submission.
     * 
     */
    private int appliedProject; // ProjectID in INT

    /**
     * The type of flat booked by the applicant (e.g., TWO_ROOM, THREE_ROOM). May not be needed. Do check before code submission.
     * 
     */
    private FlatType bookedFlatType; // view com.sc2002.enums.FlatType for more info

    /**
     * The BTO project object the applicant has booked a flat in. May not be needed. Do check before code submission.
     * 
     */
    private BTOProjectModel bookedProject;

    /**
     * Constructs a new ApplicantModel instance.
     * Initializes the applicant using the superclass constructor and sets the appropriate role.
     *
     * @param name The applicant's full name.
     * @param nric The applicant's NRIC
     * @param age The applicant's age.
     * @param isMarried A string indicating marital status ("Married" or "Single"). Case-insensitive.
     * @param password The applicant's chosen plain text password.
     * @param role The user role, typically {@code UserRole.APPLICANT}.
     */
    public ApplicantModel(String name, String nric, int age, String isMarried, String password, UserRole role){
        super(nric, name, age, isMarried, password, role);
    }

    /**
     * Provides the list of menu options available specifically to an Applicant user.
     * Overrides the abstract method from {@link UserModel}.
     *
     * @return A List of strings representing the applicant's menu actions.
     */
    @Override
    public List<String> getMenuOptions() { // Handles the printing of menu options for applicant, each user has their own menu options
        List<String> options = new ArrayList<>();
        options.addAll(Arrays.asList(
            "View Project (Filterable)",
        "Apply for BTO Project",
        "View Application Status",
        "Generate Flat Selection Receipt",
        "Submit Enquiry",
        "View Enquiry"
        ));
        return options;
    }

    /**
     * Retrieves the ID of the project the applicant has applied to. May not be needed. Do check before code submission.
     * 
     *
     * @return The integer ID of the applied project.
     */
    public int getAppliedProjectID(){ // may not be needed
        return this.appliedProject;
    }

    /**
     * Retrieves the type of flat booked by the applicant. May not be needed. Do check before code submission.
     * 
     *
     * @return The {@link FlatType} enum value representing the booked flat type.
     */
    public FlatType getFlatType(){ // may not be needed
        return this.bookedFlatType;
    }

}


