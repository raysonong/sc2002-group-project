package com.sc2002.services;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.sc2002.config.AppContext;
import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.utilities.Receipt;

/**
 * Contains the core logic for managing BTO applications. With business logic input validation.
 * This service handles tasks like viewing projects, applying, checking status,
 * approving/rejecting applications and withdrawals, and generating receipts.
 * It interacts with repositories to access and modify data.
 */
public class ApplicationService {

    /** The application context providing access to repositories and current user state. */
    private AppContext appContext;

    /**
     * Creates an ApplicationService using the shared application context.
     *
     * @param appContext Provides access to repositories and the current user.
     */
    public ApplicationService(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Shows the current applicant a list of BTO projects they can apply for.
     * It filters projects based on visibility, applicant eligibility, and whether they've already applied.
     */
    public void viewAvailableProjectsForApplicant() {
        UserModel currentUser = this.appContext.getCurrentUser();

        // get the list of available projects from the project repository
        List<BTOProjectModel> allProjects = this.appContext.getProjectRepo().findAll();

        for (BTOProjectModel project : allProjects) {
            if (isProjectVisibleForApplicant(currentUser, project)) {
                project.printAll();
            }
        }
    }

    /**
     * Checks if a specific project should be visible to an applicant.
     * Considers project visibility, previous applications, and applicant eligibility rules (age, marital status, flat availability).
     *
     * @param currentUser The applicant checking the project.
     * @param project The project to check visibility for.
     * @return True if the applicant can see and potentially apply for this project, false otherwise.
     */
    private boolean isProjectVisibleForApplicant(UserModel currentUser, BTOProjectModel project) {
        // if project is not visible
        if (!project.isVisible()) {
            return false;
        }

        // if user have applied for this project before
        if (this.appContext.getApplicationRepo().hasUserAppliedForProject(currentUser.getUserID(), project.getProjectID())) {
            return false;
        }

        // criteria for single 35 years old and above can only apply for 2-Room flats, hence we only allow viewing if
        // two room count > 0,
        if (!currentUser.getMaritalStatus() && currentUser.getAge() >= 35) {
            return project.getTwoRoomCount() > 0;
        }
        // For married individuals check age
        if (currentUser.getMaritalStatus() && currentUser.getAge() > 21) {
            // this will remove married under 21, technically not needed since we only allow 21 above to register
            // check if have any more room to book
            return !(project.getTwoRoomCount() <= 0 && project.getThreeRoomCount() <= 0);
        }

        return false;
    }

    /**
     * Guides an applicant (or an officer acting as one) through applying for a BTO project.
     * Prompts for project ID and flat type, validates input and eligibility, and saves the application if valid.
     *
     * @param projectRepo Repository to access project data.
     * @param scanner Scanner for getting user input.
     * @param currentUser The user submitting the application.
     * @return True if the application was successfully created, false if the user cancelled or an error occurred.
     */
    public Boolean applyToProject(ProjectRepo projectRepo, Scanner scanner, UserModel currentUser) {
        // Check role
        if (currentUser.getUsersRole() != UserRole.APPLICANT && currentUser.getUsersRole() != UserRole.HDB_OFFICER) {
            System.out.println("You do not have permission to apply an application to join a project.");
            return false;
        }
        ApplicantModel applicant = (ApplicantModel) currentUser;
        int input_projectID = 0;

        while (true) {
            System.out.printf("Enter Project ID (Input -1 to return back to menu): ");
            if (scanner.hasNextInt()) {
                List<BTOProjectModel> managedProjects = appContext.getProjectRepo().getProjectsByOfficer(appContext.getCurrentUser());
                input_projectID = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                // Return back to menu
                if (input_projectID == -1) {
                    return false;
                }

                // Condition that officer cannot apply a BTO project that he is managing
                if (!managedProjects.isEmpty()) {
                    if (managedProjects.get(0).getProjectID() == input_projectID) {
                        System.out.println("You cannot apply to your own project.");
                        return false;
                    }
                }

                // Validate input
                if (projectRepo.findByID(input_projectID) == null) {
                    System.out.println("This project ID does not exist. Please enter a valid ID.");
                } else {
                    break;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid ID.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        String userInput2;
        FlatType inputFlatType = null;
        while (true) {
            System.out.printf("Enter Flat Type (2-Room or 3-Room) (Input -1 to return back to menu): ");
            userInput2 = scanner.nextLine().trim().toLowerCase();

            // Validate input
            if (userInput2.equals("2-room")) {
                // no need check married people since user can only register if >=21
                if (!currentUser.getMaritalStatus() && currentUser.getAge() < 35) { // if is single and younger then 35
                    System.out.println(currentUser.getMaritalStatus());
                    System.out.println("User is single, can't apply for any projects.");
                    return false;
                }
                if (projectRepo.findByID(input_projectID).getTwoRoomCount() <= 0) {
                    System.out.println("No more 2-room.");
                    continue;
                } else { // if there is 2-room avail then allow him to book.
                    inputFlatType = FlatType.TWO_ROOM;
                    break;
                }
            } else if (userInput2.equals("3-room")) {
                if (!currentUser.getMaritalStatus()) { // if is single 
                    System.out.println("User can only apply 2-room.");
                    continue; // reloop him
                }
                if (projectRepo.findByID(input_projectID).getThreeRoomCount() <= 0) {
                    System.out.println("No more 3-room.");
                    continue;
                } else { // if there is 3-room avail then allow him to book.
                    inputFlatType = FlatType.THREE_ROOM;
                    break;
                }
            } else if (userInput2.equals("-1")) {
                return false;
            } else {
                System.out.println("Invalid flat type. Please enter a valid flat type (\"2-Room\" or \"3-Room\" or \"-1\" to exit).");
            }
        }
        BTOProjectModel selectedProject = appContext.getProjectRepo().findByID(input_projectID);
        this.appContext.getApplicationRepo().save(new BTOApplicationModel(currentUser, selectedProject, inputFlatType));
        return true;
    }

    /**
     * Gets the current status of a specific application.
     *
     * @param application The application to check.
     * @return The current status (e.g., PENDING, SUCCESSFUL).
     */
    public ApplicationStatus viewApplicationStatus(BTOApplicationModel application) {
        return application.getStatus();
    }

    /**
     * Directly sets the status of an application.
     * Use with caution; prefer methods like `approveApplicantApplication` for status changes with logic.
     *
     * @param application The application to update.
     * @param status The new status to set.
     */
    public void updateApplicationStatus(BTOApplicationModel application, ApplicationStatus status) {
        application.setStatus(status);
    }

    /**
     * A simple method to mark an application as SUCCESSFUL.
     * Does not include checks for flat availability or user authorization.
     *
     * @param application The application to mark as approved.
     */
    public void approveApplication(BTOApplicationModel application) {
        updateApplicationStatus(application, ApplicationStatus.SUCCESSFUL);
    }

    /**
     * A simple method to mark an application as UNSUCCESSFUL.
     * Does not include user authorization checks.
     *
     * @param application The application to mark as rejected.
     */
    public void rejectApplication(BTOApplicationModel application) {
        updateApplicationStatus(application, ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Creates a receipt object containing details of a BTO application.
     *
     * @param application The application to generate a receipt for.
     * @return A Receipt object.
     */
    public Receipt generateReceipt(BTOApplicationModel application) {
        return new Receipt(application);
    }

    /**
     * Approves an applicant's BTO application after checking authorization and flat availability.
     * If approved, updates the application status and deducts the flat from the project's count.
     *
     * @param application The application to approve.
     * @return True if approval was successful, false if unauthorized or no flats available.
     */
    public Boolean approveApplicantApplication(BTOApplicationModel application) {
        try { // for both Managers and Officer @Rayson
            // Do initial authentication checking to see if currentUser can actually change values
            UserModel currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().findByID(application.getProjectID());
            if (currentUser.getUserID() != project.getManagerUserID()) {// If manager of project
                if (!project.isManagingOfficer(currentUser)) {
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            //Able  to  approve  or  reject  Applicant’s  BTO  application  –  approval  is  
            // limited to the supply of the flats (number of units for the respective flat 
            // types)
            // we will immediately deduct from total count once the user's booking is approved.
            if (application.getFlatType() == FlatType.TWO_ROOM) {
                if (project.getTwoRoomCount() == 0) {
                    throw new RuntimeException("Not enough rooms.");// check at least 1

                }
                application.setStatus(ApplicationStatus.SUCCESSFUL);
                project.setTwoRoomCount(project.getTwoRoomCount() - 1);
            } else if (application.getFlatType() == FlatType.THREE_ROOM) {
                if (project.getThreeRoomCount() == 0) {
                    throw new RuntimeException("Not enough rooms.");// check at least 1

                }
                application.setStatus(ApplicationStatus.SUCCESSFUL);
                project.setThreeRoomCount(project.getThreeRoomCount() - 1);
            } else {
                throw new RuntimeException("RoomType not implemented.");
            }
            return true;
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rejects an applicant's BTO application after checking authorization.
     * Updates the application status to UNSUCCESSFUL.
     *
     * @param application The application to reject.
     * @return True if rejection was successful, false if unauthorized.
     */
    public Boolean rejectApplicantApplication(BTOApplicationModel application) {
        try { // for both Managers and Officer @Rayson
            // Do initial authentication checking to see if currentUser can actually change values
            UserModel currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().findByID(application.getProjectID());
            if (currentUser.getUserID() != project.getManagerUserID()) {// If manager of project
                if (!project.isManagingOfficer(currentUser)) {
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            // Only need do initial checks, afterwards just change status to unsucessful
            application.setStatus(ApplicationStatus.UNSUCCESSFUL);
            return true;
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Approves an applicant's request to withdraw their application.
     * Checks authorization, updates status to WITHDRAWN, adds the flat back to the project count,
     * and clears withdrawal flags/booking details.
     *
     * @param application The application whose withdrawal is being approved.
     * @return True if withdrawal approval was successful, false if unauthorized or an error occurred.
     */
    public boolean approveApplicantWithdrawalApplication(BTOApplicationModel application) {
        try {
            UserModel currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().findByID(application.getProjectID());
            if (currentUser.getUserID() != project.getManagerUserID()) { // If manager of project
                if (!project.isManagingOfficer(currentUser)) {
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            // Handle adding back the room to total count
            if (application.getFlatType() == FlatType.TWO_ROOM) {
                project.setTwoRoomCount(project.getTwoRoomCount() + 1);
            } else if (application.getFlatType() == FlatType.THREE_ROOM) {
                project.setThreeRoomCount(project.getThreeRoomCount() + 1);
            } else {
                throw new RuntimeException("RoomType not implemented.");
            }

            // Change statuses and variables to withdrawn
            application.setStatus(ApplicationStatus.WITHDRAWN);
            application.setWithdrawalRequested(false);
            application.clearBookedUnit();
            return true;
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rejects an applicant's request to withdraw their application.
     * Checks authorization and resets the withdrawal request flag on the application.
     *
     * @param application The application whose withdrawal is being rejected.
     * @return True if withdrawal rejection was successful, false if unauthorized.
     */
    public boolean rejectApplicantWithdrawalApplication(BTOApplicationModel application) {
        try {
            UserModel currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().findByID(application.getProjectID());
            if (currentUser.getUserID() != project.getManagerUserID()) { // If manager of project
                if (!project.isManagingOfficer(currentUser)) {
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            // Change status to withdrawn
            application.setWithdrawalRequested(false);
            return true;
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an application's status from SUCCESSFUL to BOOKED.
     * Typically used after the flat selection process.
     *
     * @param applicationID The ID of the application to update.
     * @return True if the application was found, was SUCCESSFUL, and is now BOOKED. False otherwise.
     */
    public boolean updateApplicationStatusToBooked(int applicationID) {
        // Fetch the application by its ID
        Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findByApplicationID(applicationID);

        if (applicationOpt.isPresent()) {
            BTOApplicationModel application = applicationOpt.get();

            // Check if the current status is 'SUCCESSFUL'
            if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
                // Update the status to 'BOOKED'
                application.setStatus(ApplicationStatus.BOOKED);
                appContext.getApplicationRepo().save(application);
                return true;
            }
        }
        return false; // Return false if the application is not found or the status is not 'SUCCESSFUL'
    }

    /**
     * Changes the flat type selected in an application.
     * Validates the new flat type against available types and updates project flat counts accordingly.
     *
     * @param applicationID The ID of the application to modify.
     * @param newFlatType The desired new flat type.
     * @param availableFlatTypes A list of flat types currently available in the project.
     * @return True if the update was successful, false if the application wasn't found or the type is invalid.
     */
    public boolean updateApplicationFlatType(int applicationID, FlatType newFlatType, List<FlatType> availableFlatTypes) {
        // Fetch the application by its ID
        Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findByApplicationID(applicationID);

        // Validate if the flat type is available
        if (!availableFlatTypes.contains(newFlatType)) {
            System.out.println("The selected flat type is not available. Please try again.");
            return false;
        }

        if (applicationOpt.isPresent()) {
            BTOApplicationModel application = applicationOpt.get();
            BTOProjectModel project = appContext.getProjectRepo().findByID(application.getProjectID());

            if (newFlatType == FlatType.TWO_ROOM) {
                System.out.println("old 2 room count: " + project.getTwoRoomCount());
                System.out.println("old 3 room count: " + project.getThreeRoomCount());
                project.setTwoRoomCount(project.getTwoRoomCount() - 1);
                project.setThreeRoomCount(project.getThreeRoomCount() + 1);
                System.out.println("\nNew 2 room count: " + project.getTwoRoomCount());
                System.out.println("New 3 room count: " + project.getThreeRoomCount());
            } else if (newFlatType == FlatType.THREE_ROOM) {
                System.out.println("old 2 room count: " + project.getTwoRoomCount());
                System.out.println("old 3 room count: " + project.getThreeRoomCount());
                project.setTwoRoomCount(project.getTwoRoomCount() + 1);
                project.setThreeRoomCount(project.getThreeRoomCount() - 1);
                System.out.println("\nNew 2 room count: " + project.getTwoRoomCount());
                System.out.println("New 3 room count: " + project.getThreeRoomCount());
            }

            // Update the flat type
            application.setFlatType(newFlatType);
            appContext.getApplicationRepo().save(application);
            return true;
        }

        // If the application is not found, print an error message
        System.out.println("Failed to update flat type. Please check the Application ID.");
        return false;
    }

    /**
     * Finds an application within a specific project based on the applicant's NRIC.
     * Used by officers/managers to look up applications. Checks if the officer is authorized for the project.
     *
     * @param projectID The ID of the project to search within.
     * @param nric The NRIC of the applicant.
     * @param outputApplication An array (size 1) to hold the found application.
     * @return True if an application by that NRIC exists in that project and the officer is authorized, false otherwise.
     */
    public boolean viewApplicationByNRIC(int projectID, String nric, BTOApplicationModel[] outputApplication) {
        Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findByNRIC(nric);

        if (applicationOpt.isEmpty()) {
            return false; // No application found for the provided NRIC
        }

        BTOApplicationModel application = applicationOpt.get();
        if (application.getProjectID() != projectID) {
            return false; // Officer is not authorized to view applications for this project
        }

        // Store the application in the output parameter
        outputApplication[0] = application;
        return true; // Application found and authorized
    }
}
