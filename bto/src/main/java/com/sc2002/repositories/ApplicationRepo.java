package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sc2002.enums.ApplicationStatus;
import com.sc2002.interfaces.RepoInterface;
import com.sc2002.model.BTOApplicationModel; // Ensure this is the correct package for RepoInterface

/**
 * Manages the storage and retrieval of BTO application data. Implements the
 * RepoInterface for standard repository operations.
 */
public class ApplicationRepo implements RepoInterface<BTOApplicationModel, Integer> {

    /**
     * In-memory list to store all BTO application instances.
     */
    private List<BTOApplicationModel> applications;

    /**
     * Constructs an ApplicationRepo, initializing an empty list to hold
     * applications.
     */
    public ApplicationRepo() {
        this.applications = new ArrayList<>();
    }

    /**
     * Saves or updates an application. If an application with the same ID
     * exists, it's replaced.
     *
     * @param application The application to save.
     */
    @Override
    public void save(BTOApplicationModel application) {
        Optional<BTOApplicationModel> existingApplication = findByApplicationID(application.getApplicationID());
        if (existingApplication.isPresent()) {
            applications.remove(existingApplication.get());
        }
        applications.add(application);
    }

    /**
     * Retrieves a list of all applications.
     *
     * @return A new list containing all stored applications.
     */
    @Override
    public List<BTOApplicationModel> findAll() {
        return new ArrayList<>(applications);
    }

    /**
     * Finds an application by its unique Application ID.
     *
     * @param applicationID The ID of the application to find.
     * @return The found application, or null if not found.
     */
    @Override
    public BTOApplicationModel findByID(Integer applicationID) {
        Optional<BTOApplicationModel> result = findByApplicationID(applicationID);
        return result.orElse(null);
    }

    /**
     * Deletes an application by its unique Application ID.
     *
     * @param applicationID The ID of the application to delete.
     * @return True if the application was found and deleted, false otherwise.
     */
    @Override
    public boolean delete(Integer applicationID) {
        Optional<BTOApplicationModel> application = findByApplicationID(applicationID);
        if (application.isPresent()) {
            applications.remove(application.get());
            return true;
        }
        return false;
    }

    /**
     * Finds an application associated with a specific User ID. Assumes a user
     * can only have one active application at a time (returns the last found).
     *
     * @param userID The User ID of the applicant.
     * @return An Optional containing the application if found, otherwise empty.
     */
    public Optional<BTOApplicationModel> findbyUserID(long userID) {
        // Iterate from the last index down to the first (index 0)
        for (int i = this.applications.size() - 1; i >= 0; i--) {
            BTOApplicationModel application = this.applications.get(i); // Get application at index i
            if (application.getApplicantUserID() == userID) {
                // The first match found when iterating backwards is the last one overall
                return Optional.of(application);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds the first active (non-withdrawn, non-unsuccessful) application for
     * a given User ID (provided as String).
     *
     * @param userID The User ID of the applicant as a String.
     * @return An Optional containing the active application if found, otherwise
     * empty.
     */
    public Optional<BTOApplicationModel> findActiveByApplicantID(String userID) { // returns a Optional, to tell whether the user has an active Application
        return applications.stream()
                .filter(application -> String.valueOf(application.getApplicantUserID()).equals(userID))
                .findFirst();
    }

    /**
     * Finds all applications submitted by a specific user.
     *
     * @param UserID The User ID of the applicant.
     * @return A list of all applications associated with the User ID.
     */
    public List<BTOApplicationModel> findApplicationByApplicantID(int UserID) {
        return applications.stream()
                .filter(application -> application.getApplicantUserID() == UserID)
                .collect(Collectors.toList());
    }

    /**
     * Finds all applications associated with a specific Project ID.
     *
     * @param projectID The ID of the project.
     * @return A list of applications for the specified project.
     */
    public List<BTOApplicationModel> findByProjectID(int projectID) {
        return applications.stream()
                .filter(application -> application.getProjectID() == projectID)
                .collect(Collectors.toList());
    }

    /**
     * Finds all applications with status BOOKED for a specific Project ID.
     *
     * @param projectID The ID of the project.
     * @return A list of booked applications for the specified project.
     */
    public List<BTOApplicationModel> findBookedByProjectID(int projectID) {
        return applications.stream()
                .filter(application -> application.getProjectID() == projectID // projectID filter
                && application.getStatus() == ApplicationStatus.BOOKED) // Also check if BOOKED
                .collect(Collectors.toList());
    }

    /**
     * Finds all applications with status PENDING for a specific Project ID.
     *
     * @param projectID The ID of the project.
     * @return A list of pending applications for the specified project.
     */
    public List<BTOApplicationModel> findPendingByProjectID(int projectID) {
        return applications.stream()
                .filter(application -> application.getProjectID() == projectID // projectID filter
                && application.getStatus() == ApplicationStatus.PENDING) // Also check if BOOKED
                .collect(Collectors.toList());
    }

    /**
     * Finds all applications within a project where a withdrawal has been
     * requested.
     *
     * @param projectID The ID of the project.
     * @return A list of applications with pending withdrawal requests for the
     * specified project.
     */
    public List<BTOApplicationModel> findPendingWithDrawalByProjectID(int projectID) {
        return applications.stream()
                .filter(application -> application.getProjectID() == projectID // projectID filter
                && application.getWithdrawalRequested()) // Also check if withdrawal is requested
                .collect(Collectors.toList());
    }

    /**
     * Finds an application by its unique Application ID using streams.
     *
     * @param applicationID The ID of the application to find.
     * @return An Optional containing the application if found, otherwise empty.
     */
    public Optional<BTOApplicationModel> findByApplicationID(int applicationID) {
        return applications.stream()
                .filter(app -> app.getApplicationID() == applicationID)
                .findFirst();
    }

    /**
     * Finds an application by the applicant's NRIC. Assumes NRIC is unique
     * across applications (returns the first found).
     *
     * @param nric The NRIC of the applicant.
     * @return An Optional containing the application if found, otherwise empty.
     */
    public Optional<BTOApplicationModel> findByNRIC(String nric) {
        return applications.stream()
                .filter(app -> app.getApplicantNRIC().equals(nric))
                .findFirst();
    }

    /**
     * Checks if a user has ever submitted an application for a specific
     * project, regardless of status.
     *
     * @param userID The User ID of the applicant.
     * @param projectID The ID of the project.
     * @return True if any application exists for this user and project, false
     * otherwise.
     */
    public boolean hasUserAppliedForProject(int userID, int projectID) {
        for (BTOApplicationModel application : applications) {
            if (application.getApplicantUserID() == userID && application.getProjectID() == projectID) {
                return true; // If an application exists return true, no matter status
            }// therefore can be PENDING,SUCCESSFUL,BOOKED,WITHDRAWN.
        }
        return false;
    }

    /**
     * Gets the status of a specific application identified by User ID and
     * Project ID.
     *
     * @param userID The User ID of the applicant.
     * @param projectID The ID of the project.
     * @return The ApplicationStatus if found, or null if no matching
     * application exists.
     */
    public ApplicationStatus getApplicationStatus(int userID, int projectID) {
        for (BTOApplicationModel application : applications) {
            if (application.getApplicantUserID() == userID && application.getProjectID() == projectID) {
                return application.getStatus();
            }
        }
        return null;
    }

    /**
     * Checks if a user is allowed to apply for a new project. A user can apply
     * if they have no existing applications or if all their existing
     * applications are WITHDRAWN.
     *
     * @param userID The User ID of the applicant.
     * @return True if the user can apply for a new project, false otherwise.
     */
    public boolean canApplyForProject(int userID) {
        List<BTOApplicationModel> applications = findApplicationByApplicantID(userID);

        for (BTOApplicationModel application : applications) {
            if (application.getStatus() != ApplicationStatus.WITHDRAWN && application.getStatus() != ApplicationStatus.UNSUCCESSFUL) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds an active (not WITHDRAWN or UNSUCCESSFUL) application for a
     * specific user and project.
     *
     * @param userID The User ID of the applicant.
     * @param projectID The ID of the project.
     * @return The active application if found, otherwise null.
     */
    public BTOApplicationModel findByUserAndProject(int userID, int projectID) {
        for (BTOApplicationModel application : applications) {
            if (application.getApplicantUserID() == userID && application.getProjectID() == projectID
                    && application.getStatus() != ApplicationStatus.WITHDRAWN && application.getStatus() != ApplicationStatus.UNSUCCESSFUL) {
                return application;
            }
        }
        return null;
    }
}
