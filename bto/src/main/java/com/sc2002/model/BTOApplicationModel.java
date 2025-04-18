package com.sc2002.model;

import java.util.Date;

import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;

/**
 * Represents a single application submitted by a user for a specific BTO project.
 * Contains details about the applicant, the project, the chosen flat type,
 * the application status, and submission/booking information.
 */
public class BTOApplicationModel {
    /** Static counter for generating unique Application IDs. */
    private static int nextApplicationID = 1; // Static counter for auto-incrementing IDs
    /** The unique ID for this application. */
    private int applicationID;

    /** The user who submitted this application. */
    private UserModel applicantUser;
    /** The BTO project this application is for. */
    private BTOProjectModel project;
    /** The current status of the application (e.g., PENDING, SUCCESSFUL). */
    private ApplicationStatus status;
    /** The type of flat the applicant applied for. */
    private FlatType applicationFlatType;
    /** Details of the booked unit, if the application is successful and booked. Null otherwise. */
    private BookingDetailModel BookedUnit;
    /** The date and time when the application was submitted. */
    private Date submissionDate;
    /** Flag indicating if the applicant has requested to withdraw this application. */
    private Boolean withdrawalRequested;

    /**
     * Constructs a new BTO Application.
     * Performs initial eligibility checks based on age, marital status, and flat type.
     * Initializes status to PENDING and sets the submission date.
     *
     * @param applicantUser The user submitting the application.
     * @param project The project being applied for.
     * @param applicationFlatType The flat type chosen by the applicant.
     * @throws RuntimeException if the applicant is ineligible based on age/marital status/flat type rules.
     */
    public BTOApplicationModel(UserModel applicantUser, BTOProjectModel project, FlatType applicationFlatType) {
        //Cond 1, Singles 35 year old and above, can only apply for 2-Room
        if(applicantUser.getAge()<35 && !applicantUser.getMaritalStatus()){ // Checks for singles less than 35
            throw new RuntimeException("User is Single & age less than 35.");
        } 
        //Cond 2, Married, 21 years old and above, can apply for any flat type
        // No need check 21 lesser since we allow users to register only if they are above 21
        // getMaritalStatus returns true if married.
        if(!applicantUser.getMaritalStatus() && applicationFlatType.equals(FlatType.THREE_ROOM)){ //  check for singles tryna get 3-room
            throw new RuntimeException("User is Single can't apply 3-Room.");
        }
        // free to apply for married individuals
        this.applicationID = nextApplicationID++;
        this.applicantUser=applicantUser;
        this.project=project;
        this.applicationFlatType=applicationFlatType;
        this.status = ApplicationStatus.PENDING;
        this.submissionDate = new Date();
        this.withdrawalRequested=false; //default false

    }

    /**
     * Gets the marital status of the applicant.
     * @return True if married, false otherwise.
     */
    public boolean getApplicantMaritalStatus(){
        return this.applicantUser.getMaritalStatus();
    }

    /**
     * Gets the flat type applied for in this application.
     * @return The FlatType enum value.
     */
    public FlatType getFlatType(){
        return this.applicationFlatType;
    }

    /**
     * Clears the booked unit details. Used during withdrawal.
     * @return True, indicating the operation was performed.
     */
    public boolean clearBookedUnit() {
        this.BookedUnit = null;
        return true;
    }

    /**
     * Gets the name of the applicant.
     * @return The applicant's name.
     */
    public String getApplicantName(){
        return this.applicantUser.getName();
    }

    /**
     * Gets the age of the applicant.
     * @return The applicant's age.
     */
    public int getApplicantAge(){
        return this.applicantUser.getAge();
    }

    /**
     * Gets the unique User ID of the applicant.
     * @return The applicant's User ID.
     */
    public long getApplicantUserID() {
        return this.applicantUser.getUserID();
    }

    /**
     * Gets the NRIC of the applicant.
     * @return The applicant's NRIC.
     */
    public String getApplicantNRIC() {
        return this.applicantUser.getNRIC();
    }

    /**
     * Gets the unique Project ID associated with this application.
     * @return The Project ID.
     */
    public int getProjectID() {
        return this.project.getProjectID();
    }

    /**
     * Gets the current status of this application.
     * @return The ApplicationStatus enum value.
     */
    public ApplicationStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the status of this application.
     * @param applicationStatus The new status to set.
     * @return True, indicating the status was set.
     */
    public boolean setStatus(ApplicationStatus applicationStatus){
        this.status=applicationStatus;
        return true;
    }

    /**
     * Gets the unique Application ID for this application.
     * @return The Application ID.
     */
    public int getApplicationID(){
        return this.applicationID;
    }
    
    /**
     * Gets the date when this application was submitted.
     * @return The submission date.
     */
    public Date getSubmissionDate() {
        return this.submissionDate;
    }

    /**
     * Checks if a withdrawal has been requested for this application.
     * @return True if withdrawal is requested, false otherwise.
     */
    public boolean getWithdrawalRequested(){
        return this.withdrawalRequested;
    }

    /**
     * Sets the withdrawal requested flag for this application.
     * @param isWithdrawing True to request withdrawal, false to cancel request.
     * @return True, indicating the flag was set.
     */
    public boolean setWithdrawalRequested(boolean isWithdrawing){
        this.withdrawalRequested=isWithdrawing;
        return true;
    }
    
    /**
     * Sets the flat type for this application. Used for updates/changes.
     * @param flatType The new flat type.
     */
    public void setFlatType(FlatType flatType) {
        this.applicationFlatType = flatType;
    }
}
