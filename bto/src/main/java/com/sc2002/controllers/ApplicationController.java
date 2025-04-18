package com.sc2002.controllers;

import java.util.List;
import java.util.Scanner;

import com.sc2002.config.AppContext;
import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.services.ApplicationService;
import com.sc2002.utilities.Receipt;

/**
 * Handles requests related to BTO project applications, acting as an intermediary
 * between the user interface (views) and the application logic (ApplicationService).
 * Future implementation of input validation can be handled here.
 */
public class ApplicationController {
    
    /** The service layer handling core application logic. */
    private ApplicationService applicationService;

    /**
     * Default constructor for ApplicationController.
     * Note: The ApplicationService is not initialized here. Use the constructor
     * with AppContext or set the service manually if using this constructor.
     */
    public ApplicationController() {
        // Default constructor - ApplicationService must be set separately
    }
    
    /**
     * Constructs an ApplicationController, initializing the ApplicationService
     * using the provided AppCntext.
     *
     * @param appContext The shared application context containing currentUser, repositories and services.
     */
    public ApplicationController(AppContext appContext) {
        this.applicationService = new ApplicationService(appContext);
    }
    
    /**
     * Constructs an ApplicationController with a pre-existing ApplicationService instance.
     * Useful for testing or specific dependency injection scenarios.
     *
     * @param applicationService The ApplicationService instance to use.
     */
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
    
    /**
     * Initiates the process to display BTO projects that the current applicant is eligible to apply for.
     * Delegates the logic to the ApplicationService.
     */
    public void viewAvailableProjectsForApplicant() {
        applicationService.viewAvailableProjectsForApplicant();
    }
    
    /**
     * Handles the process for a user to submit an application for a specific BTO project.
     * Delegates the application logic to the ApplicationService.
     *
     * @param projectRepo Repository to access project data.
     * @param scanner Scanner instance for capturing user input (e.g., flat type choice).
     * @param currentUser The user model representing the applicant.
     * @return True if the application submission was successful, false otherwise.
     */
    public Boolean applyToProject(ProjectRepo projectRepo, Scanner scanner, UserModel currentUser) {
        return applicationService.applyToProject(projectRepo, scanner, currentUser);
    }
    
    /**
     * Retrieves the current status of a specific BTO application.
     *
     * @param application The BTOApplicationModel instance to check.
     * @return The ApplicationStatus enum value representing the application's current state.
     */
    public ApplicationStatus viewApplicationStatus(BTOApplicationModel application) {
        return applicationService.viewApplicationStatus(application);
    }
    
    /**
     * Updates the status of a given BTO application.
     *
     * @param application The BTOApplicationModel instance to update.
     * @param status The new ApplicationStatus to set.
     */
    public void updateApplicationStatus(BTOApplicationModel application, ApplicationStatus status) {
        applicationService.updateApplicationStatus(application, status);
    }
    
    /**
     * Sets the status of a given BTO application to SUCCESSFUL.
     * Note: This is a simple status update. Use {@code approveApplicantApplication} for logic involving checks and balances.
     *
     * @param application The BTOApplicationModel instance to approve.
     */
    public void approveApplication(BTOApplicationModel application) {
        applicationService.approveApplication(application);
    }
    
    /**
     * Sets the status of a given BTO application to UNSUCCESSFUL.
     * Note: This is a simple status update. Use {@code rejectApplicantApplication} for logic involving checks and balances.
     *
     * @param application The BTOApplicationModel instance to reject.
     */
    public void rejectApplication(BTOApplicationModel application) {
        applicationService.rejectApplication(application);
    }
    
    /**
     * Generates a receipt object for a given BTO application.
     *
     * @param application The BTOApplicationModel instance for which to generate a receipt.
     * @return A Receipt object containing application details.
     */
    public Receipt generateReceipt(BTOApplicationModel application) {
        return applicationService.generateReceipt(application);
    }
    
    /**
     * Handles the logic for approving an applicant's BTO application, including necessary checks (e.g., flat availability).
     * Delegates the core approval logic to the ApplicationService.
     *
     * @param application The BTOApplicationModel instance to approve.
     * @return True if the application was successfully approved, false otherwise (e.g., no flats left, authorization failure).
     */
    public Boolean approveApplicantApplication(BTOApplicationModel application) {
        return applicationService.approveApplicantApplication(application);
    }
    
    /**
     * Handles the logic for rejecting an applicant's BTO application.
     * Delegates the core rejection logic to the ApplicationService.
     *
     * @param application The BTOApplicationModel instance to reject.
     * @return True if the application was successfully rejected, false otherwise (e.g., authorization failure).
     */
    public Boolean rejectApplicantApplication(BTOApplicationModel application) {
        return applicationService.rejectApplicantApplication(application);
    }
    
    /**
     * Handles the logic for approving an applicant's request to withdraw their BTO application.
     * This typically involves updating status and potentially adjusting flat counts.
     * Delegates the core withdrawal approval logic to the ApplicationService.
     *
     * @param application The BTOApplicationModel instance whose withdrawal request is being approved.
     * @return True if the withdrawal was successfully approved, false otherwise (e.g., authorization failure).
     */
    public boolean approveApplicantWithdrawalApplication(BTOApplicationModel application) {
        return applicationService.approveApplicantWithdrawalApplication(application);
    }
    
    /**
     * Handles the logic for rejecting an applicant's request to withdraw their BTO application.
     * This typically involves resetting the withdrawal request flag.
     * Delegates the core withdrawal rejection logic to the ApplicationService.
     *
     * @param application The BTOApplicationModel instance whose withdrawal request is being rejected.
     * @return True if the withdrawal rejection was successful, false otherwise (e.g., authorization failure).
     */
    public boolean rejectApplicantWithdrawalApplication(BTOApplicationModel application) {
        return applicationService.rejectApplicantWithdrawalApplication(application);
    }
    
    /**
     * Updates the status of a specific application to BOOKED.
     * Delegates the update logic to the ApplicationService.
     *
     * @param applicationID The unique ID of the application to update.
     * @return True if the status was successfully updated, false otherwise (e.g., application not found).
     */
    public boolean updateApplicationStatusToBooked(int applicationID) {
        return applicationService.updateApplicationStatusToBooked(applicationID);
    }
    
    /**
     * Updates the chosen flat type for a specific application, ensuring the new type is valid.
     * Delegates the update logic to the ApplicationService.
     *
     * @param applicationID The unique ID of the application to update.
     * @param newFlatType The desired new FlatType.
     * @param availableFlatTypes A list of currently available flat types for validation.
     * @return True if the flat type was successfully updated, false otherwise (e.g., invalid type, application not found).
     */
    public boolean updateApplicationFlatType(int applicationID, FlatType newFlatType, List<FlatType> availableFlatTypes) {
        return applicationService.updateApplicationFlatType(applicationID, newFlatType, availableFlatTypes);
    }
    
    /**
     * Searches for and retrieves a specific BTO application within a project based on the applicant's NRIC.
     * Delegates the search logic to the ApplicationService.
     *
     * @param projectID The ID of the project to search within.
     * @param nric The NRIC of the applicant whose application is sought.
     * @param outputApplication An array (expected size 1) to store the found application if successful.
     * @return True if an application matching the criteria was found and placed in {@code outputApplication}, false otherwise.
     */
    public boolean viewApplicationByNRIC(int projectID, String nric, BTOApplicationModel[] outputApplication) {
        return applicationService.viewApplicationByNRIC(projectID, nric, outputApplication);
    }
}
