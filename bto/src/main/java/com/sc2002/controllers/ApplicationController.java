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
 * Controller for handling application-related operations.
 * Acts as an intermediary between the view and the ApplicationService.
 */
public class ApplicationController {
    
    private ApplicationService applicationService;
    
    /**
     * Constructor for ApplicationController.
     * 
     * @param appContext The application context
     */
    public ApplicationController(AppContext appContext) {
        this.applicationService = new ApplicationService(appContext);
    }
    
    /**
     * Constructor for ApplicationController with an existing ApplicationService.
     * 
     * @param applicationService The application service to use
     */
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
    
    /**
     * Displays projects that are available for the current applicant to apply to.
     */
    public void viewAvailableProjectsForApplicant() {
        applicationService.viewAvailableProjectsForApplicant();
    }
    
    /**
     * Allows a user to apply to a project.
     * 
     * @param projectRepo The project repository
     * @param scanner The scanner for user input
     * @param currentUser The current user
     * @return Boolean indicating if the application was successful
     */
    public Boolean applyToProject(ProjectRepo projectRepo, Scanner scanner, UserModel currentUser) {
        return applicationService.applyToProject(projectRepo, scanner, currentUser);
    }
    
    /**
     * Gets the status of an application.
     * 
     * @param application The application
     * @return The application status
     */
    public ApplicationStatus viewApplicationStatus(BTOApplicationModel application) {
        return applicationService.viewApplicationStatus(application);
    }
    
    /**
     * Updates the status of an application.
     * 
     * @param application The application
     * @param status The new status
     */
    public void updateApplicationStatus(BTOApplicationModel application, ApplicationStatus status) {
        applicationService.updateApplicationStatus(application, status);
    }
    
    /**
     * Approves an application.
     * 
     * @param application The application to approve
     */
    public void approveApplication(BTOApplicationModel application) {
        applicationService.approveApplication(application);
    }
    
    /**
     * Rejects an application.
     * 
     * @param application The application to reject
     */
    public void rejectApplication(BTOApplicationModel application) {
        applicationService.rejectApplication(application);
    }
    
    /**
     * Generates a receipt for an application.
     * 
     * @param application The application
     * @return The generated receipt
     */
    public Receipt generateReceipt(BTOApplicationModel application) {
        return applicationService.generateReceipt(application);
    }
    
    /**
     * Approves an applicant's application.
     * 
     * @param application The application to approve
     * @return Boolean indicating if the approval was successful
     */
    public Boolean approveApplicantApplication(BTOApplicationModel application) {
        return applicationService.approveApplicantApplication(application);
    }
    
    /**
     * Rejects an applicant's application.
     * 
     * @param application The application to reject
     * @return Boolean indicating if the rejection was successful
     */
    public Boolean rejectApplicantApplication(BTOApplicationModel application) {
        return applicationService.rejectApplicantApplication(application);
    }
    
    /**
     * Approves an applicant's withdrawal application.
     * 
     * @param application The application to approve withdrawal for
     * @return Boolean indicating if the approval was successful
     */
    public boolean approveApplicantWithdrawalApplication(BTOApplicationModel application) {
        return applicationService.approveApplicantWithdrawalApplication(application);
    }
    
    /**
     * Rejects an applicant's withdrawal application.
     * 
     * @param application The application to reject withdrawal for
     * @return Boolean indicating if the rejection was successful
     */
    public boolean rejectApplicantWithdrawalApplication(BTOApplicationModel application) {
        return applicationService.rejectApplicantWithdrawalApplication(application);
    }
    
    /**
     * Updates an application's status to booked.
     * 
     * @param applicationID The ID of the application
     * @return Boolean indicating if the update was successful
     */
    public boolean updateApplicationStatusToBooked(int applicationID) {
        return applicationService.updateApplicationStatusToBooked(applicationID);
    }
    
    /**
     * Updates an application's flat type.
     * 
     * @param applicationID The ID of the application
     * @param newFlatType The new flat type
     * @param availableFlatTypes The list of available flat types
     * @return Boolean indicating if the update was successful
     */
    public boolean updateApplicationFlatType(int applicationID, FlatType newFlatType, List<FlatType> availableFlatTypes) {
        return applicationService.updateApplicationFlatType(applicationID, newFlatType, availableFlatTypes);
    }
    
    /**
     * Views an application by NRIC.
     * 
     * @param projectID The ID of the project
     * @param nric The NRIC to search for
     * @param outputApplication Array to store the found application
     * @return Boolean indicating if the application was found
     */
    public boolean viewApplicationByNRIC(int projectID, String nric, BTOApplicationModel[] outputApplication) {
        return applicationService.viewApplicationByNRIC(projectID, nric, outputApplication);
    }
}
