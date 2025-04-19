package com.sc2002.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.sc2002.config.AppContext;
import com.sc2002.enums.OfficerRegistrationStatus;
import com.sc2002.enums.UserRole;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.OfficerRegistrationRepo;
import com.sc2002.repositories.ProjectRepo;

/**
 * Service responsible for handling the logic related to HDB Officer
 * registration requests for managing projects. Includes submitting, approving,
 * and rejecting registration applications.
 */
public class OfficerRegistrationService {

    /**
     * The application context providing access to repositories and current user
     * state.
     */
    private AppContext appContext;

    /**
     * Constructs an OfficerRegistrationService with the given application
     * context.
     *
     * @param appContext The application context.
     */
    public OfficerRegistrationService(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Handles the process for the current HDB Officer user to register to
     * manage a specific project. Prompts for project ID, performs validation,
     * and saves the registration request.
     *
     * @return True if the registration request was successfully submitted,
     * false otherwise.
     */
    public boolean registerForProject() {
        // Initialize appContext required
        ProjectRepo projectRepo = this.appContext.getProjectRepo();
        ApplicationRepo applicationRepo = this.appContext.getApplicationRepo();
        OfficerRegistrationRepo officerRegistrationRepo = this.appContext.getOfficerRegistrationRepo();

        // Check role
        UserModel currentUser = this.appContext.getCurrentUser();
        if (currentUser.getUsersRole() != UserRole.HDB_OFFICER) {
            System.out.println("You do not have permission to apply an application to join a project.");
            return false;
        }

        // Retrieve the projects managed by the officer
        List<BTOProjectModel> managedProjects = projectRepo.getProjectsByOfficer(appContext.getCurrentUser());
        LocalDate today = LocalDate.now();

        if (managedProjects != null && !managedProjects.isEmpty()) {
            for (BTOProjectModel project : managedProjects) {
                if (project == null) {
                    continue; // Skip null projects in the list
                }
                LocalDate openingDate = project.getOpeningDate();
                LocalDate closingDate = project.getClosingDate();

                // Ensure dates are not null before comparing
                if (openingDate != null && closingDate != null) {
                    // Check if today is within the project's active period (inclusive)
                    // Equivalent to: openingDate <= today <= closingDate
                    boolean isOpenOrAfter = !today.isBefore(openingDate); // today >= openingDate
                    boolean isClosedOrBefore = !today.isAfter(closingDate); // today <= closingDate

                    if (isOpenOrAfter && isClosedOrBefore) {
                        // Found an active project managed by the user
                        System.out.println("You can no longer register for a project as you are already managing a project.");
                        return false; // Return the first active project found
                    }
                } else {
                    // Log a warning if dates are missing, as they are needed for the check
                    System.out.println("Warning: Project ID " + project.getProjectID() + " is missing opening or closing dates.");
                }
            }
        }

        Scanner scanner = this.appContext.getScanner();
        int input_projectID = 0;

        while (true) {
            System.out.printf("Enter Project ID (Input -1 to return back to menu): ");
            if (scanner.hasNextInt()) {
                input_projectID = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                // Return back to menu
                if (input_projectID == -1) {
                    return false;
                }

                // Validate input
                if (projectRepo.findByID(input_projectID) == null) {
                    System.out.println("This project ID does not exist. Please enter a valid ID.");
                    return false;
                }

                // Check if the officer has already applied as an applicant
                if (applicationRepo.findByUserAndProject(currentUser.getUserID(), input_projectID) != null) {
                    System.out.println("You cannot register for a project you have already applied to as an applicant.");
                    return false;
                }

                break;
            } else {
                System.out.println("Invalid input. Please enter a valid ID.");
                scanner.nextLine(); // Consume invalid input
            }
        }

        // save in repository
        officerRegistrationRepo.save(new OfficerRegistrationModel(currentUser, input_projectID));

        return true;
    }

    /**
     * Approves an officer's registration request. Performs authorization checks
     * (only the project manager can approve). Updates the registration status
     * and adds the officer to the project's managing list if successful.
     *
     * @param registration The OfficerRegistrationModel instance to approve.
     * @return True if the approval was successful, false otherwise (e.g.,
     * unauthorized, project full).
     */
    public boolean approveRegistration(OfficerRegistrationModel registration) {
        try {
            //Checking if isManager and is Project's manager
            BTOProjectModel project = this.appContext.getProjectRepo().findByID(registration.getProjectID());
            if (this.appContext.getAuthController().isManager(this.appContext.getCurrentUser()) && project.getManagerUserID() == this.appContext.getCurrentUser().getUserID()) {
                // Add to project 
                if (project.addManagingOfficerUser(registration.getOfficerUser())) {
                    registration.setStatus(OfficerRegistrationStatus.APPROVED);
                    return true;
                } else {
                    throw new RuntimeException("Failed to add Officer to project. (limit hit)");
                }
            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rejects an officer's registration request. Performs authorization checks
     * (only the project manager can reject). Updates the registration status to
     * REJECTED.
     *
     * @param registration The OfficerRegistrationModel instance to reject.
     * @return True if the rejection was successful, false if unauthorized.
     */
    public boolean rejectRegistration(OfficerRegistrationModel registration) {
        try {
            //Checking if isManager and is Project's manager
            BTOProjectModel project = this.appContext.getProjectRepo().findByID(registration.getProjectID());
            if (this.appContext.getAuthController().isManager(this.appContext.getCurrentUser()) && project.getManagerUserID() == this.appContext.getCurrentUser().getUserID()) {
                // Add to project 
                if (project.removeManagingOfficerUser(registration.getOfficerUser())) {
                    registration.setStatus(OfficerRegistrationStatus.REJECTED);
                    return true;
                } else {
                    throw new RuntimeException("Failed to remove Officer from project.");
                }
            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
}
