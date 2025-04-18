package com.sc2002.model;

import com.sc2002.enums.OfficerRegistrationStatus;

/**
 * Represents a registration request submitted by an HDB Officer to manage a specific BTO project.
 * Contains details about the officer, the project, and the status of the registration request.
 */
public class OfficerRegistrationModel {
    /** Static counter for generating unique registration IDs. */
    private static int nextRegistrationID = 1; // Static counter for auto-incrementing IDs
    /** The unique ID for this registration request. */
    private final int registrationID;
    /** The HDB Officer user submitting the registration request. */
    private UserModel officer;
    /** The ID of the BTO project the officer wants to manage. */
    private int projectID;
    /** The current status of the registration request (e.g., PENDING, APPROVED, REJECTED). */
    private OfficerRegistrationStatus status;

    /**
     * Constructs a new OfficerRegistrationModel instance.
     * Assigns a unique registration ID and sets the initial status to PENDING.
     *
     * @param officer The UserModel of the officer registering.
     * @param projectID The ID of the project the officer wants to register for.
     */
    public OfficerRegistrationModel(UserModel officer, int projectID) {
        this.registrationID = nextRegistrationID++;
        this.officer = officer;
        this.projectID = projectID;
        this.status = OfficerRegistrationStatus.PENDING;
    }

    /**
     * Gets the unique ID of this registration request.
     * @return The registration ID.
     */
    public int getRegistrationID(){
        return this.registrationID;
    }

    /**
     * Gets the ID of the project associated with this registration request.
     * @return The project ID.
     */
    public int getProjectID() {
        return this.projectID;
    }

    /**
     * Gets the name of the officer who submitted this registration request.
     * @return The officer's name.
     */
    public String getOfficerName(){
        return this.officer.getName();
    }

    /**
     * Gets the User ID of the officer who submitted this registration request.
     * @return The officer's User ID.
     */
    public int getUserID() {
        return this.officer.getUserID();
    }

    /**
     * Gets the UserModel object representing the officer who submitted this request.
     * @return The officer's UserModel.
     */
    public UserModel getOfficerUser(){
        return this.officer;
    }

    /**
     * Gets the current status of this registration request.
     * @return The OfficerRegistrationStatus enum value.
     */
    public OfficerRegistrationStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the status of this registration request.
     * @param status The new OfficerRegistrationStatus to set.
     */
    public void setStatus(OfficerRegistrationStatus status){
        this.status=status;
    }
}
