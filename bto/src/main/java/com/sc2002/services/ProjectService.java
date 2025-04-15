package com.sc2002.services;

import java.time.LocalDate;
import java.util.List;

import com.sc2002.config.AppContext;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.UserModel;

public class ProjectService {

    private AppContext appContext;

    public ProjectService(AppContext appContext) {
        this.appContext = appContext;
    }

    public void viewProjectByID(int projectID) {
        try {
            if (this.appContext.getAuthService().isApplicant(this.appContext.getCurrentUser())) {
                // viewProject for applicant, cannot view if not visible
                BTOProjectModel project = this.appContext.getProjectRepo().findByID(projectID);
                if (project != null) {
                    ApplicantModel currentUser = (ApplicantModel) this.appContext.getCurrentUser();
                    if (project.isVisible()) {
                        project.printAll();
                    } else if (currentUser.getAppliedProjectID() == projectID) { // is safe since projectID in BTOProjectModel is final int
                        project.printAll();
                    } else {
                        throw new RuntimeException("No Project Found.");
                    }
                } else {
                    throw new RuntimeException("No Project Found.");
                }
            } else if (this.appContext.getAuthService().isOfficer(this.appContext.getCurrentUser()) || this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())) {
                // viewProject for Officer/Manager, view regardless of visilibty
                BTOProjectModel project = this.appContext.getProjectRepo().findByID(projectID);
                if (project != null) {
                    project.printAll();
                } else {
                    throw new RuntimeException("No Project Found.");
                }
            } else {
                throw new RuntimeException("UserRole undefined.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return;
        }
    }

    public BTOProjectModel viewManagingProject() { // Returns the project user is managing
        UserModel currentUser = appContext.getCurrentUser();
        LocalDate today = LocalDate.now();
        List<BTOProjectModel> managedProjects = null;

        // 1. Get the list of projects managed by the user based on their role
        try {
            if (appContext.getAuthService().isManager(currentUser)) {
                // Assuming getProjectsByManagerID exists and takes String ID
                managedProjects = appContext.getProjectRepo().getProjectsByManagerID(currentUser.getUserID());
            } else if (appContext.getAuthService().isOfficer(currentUser)) {
                // *** Verify this call ***
                // Assuming getProjectsByOfficer exists and takes UserModel.
                // Adjust parameter if it expects ID/NRIC (e.g., currentUser.getUserID())
                managedProjects = appContext.getProjectRepo().getProjectsByOfficer(currentUser);
            } else {
                // User role is not one that manages projects in this context
                // System.out.println("Info: User role (" + currentUser.getRole() + ") does not manage projects.");
                return null; // Not an error, just doesn't manage projects
            }
        } catch (Exception e) {
            // Catch potential errors during repository access
            System.err.println("Error retrieving projects for user " + currentUser.getName() + ": " + e.getMessage());
            // e.printStackTrace(); // Optionally print stack trace for debugging
            return null; // Return null on repository error
        }

        // 2. Check if the list is valid and iterate to find an *active* project
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
                        return project; // Return the first active project found
                    }
                } else {
                    // Log a warning if dates are missing, as they are needed for the check
                    System.out.println("Warning: Project ID " + project.getProjectID() + " is missing opening or closing dates.");
                }
            }
        }

        // 3. If loop finished without returning, no *active* project was found
        // System.out.println("Info: No projects actively managed by user " + currentUser.getName() + " today.");
        return null; // Return null signifies no active project found
    }
}
