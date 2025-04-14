package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sc2002.enums.ApplicationStatus;
import com.sc2002.model.BTOApplicationModel;


public class ApplicationRepo {
    private List<BTOApplicationModel> applications;
    
    /**
     * Constructor initializes an empty list of applications
     */
    public ApplicationRepo() {
        this.applications = new ArrayList<>();
    }

    public Optional<BTOApplicationModel> findbyUserID(long userId) {
        for (BTOApplicationModel application : this.applications) {
            if (application.getApplicantUserID() == userId) {
                return Optional.of(application);
            }
        }
        return Optional.empty();
    }

    public void save(BTOApplicationModel application) {
        // Check if the application already exists
        Optional<BTOApplicationModel> existingApplication = findbyUserID(application.getApplicantUserID());
        
        if(existingApplication.isPresent()) {
            // Remove the existing application
            applications.remove(existingApplication.get());
        }
        
        // Add the new or updated application
        applications.add(application);
    }

    public Optional<BTOApplicationModel> findActiveByApplicantID(String userID) { // returns a Optional, to tell whether the user has an active Application
        return applications.stream()
                .filter(application -> String.valueOf(application.getApplicantUserID()).equals(userID))
                .findFirst();
    }
    // Finds all application by a specific userid
    public List<BTOApplicationModel> findApplicationByApplicantID(int UserID){
        return applications.stream()
        .filter(application -> application.getApplicantUserID() == UserID)
        .collect(Collectors.toList());
    }

    public List<BTOApplicationModel> findByProjectID(int projectID) {
        return applications.stream()
        .filter(application -> application.getProjectID() == projectID)
        .collect(Collectors.toList());
    }

    public List<BTOApplicationModel> findBookedByProjectID(int projectID) {
        return applications.stream()
        .filter(application -> application.getProjectID() == projectID // projectID filter
                && application.getStatus() == ApplicationStatus.BOOKED) // Also check if BOOKED
        .collect(Collectors.toList());
    }
    public List<BTOApplicationModel> findPendingByProjectID(int projectID) {
        return applications.stream()
        .filter(application -> application.getProjectID() == projectID // projectID filter
                && application.getStatus() == ApplicationStatus.PENDING) // Also check if BOOKED
        .collect(Collectors.toList());
    }
    public List<BTOApplicationModel> findPendingWithDrawalByProjectID(int projectID) {
        return applications.stream()
        .filter(application -> application.getProjectID() == projectID // projectID filter
                && application.getWithdrawalRequested()) // Also check if withdrawal is requested
        .collect(Collectors.toList());
    }

    public Optional<BTOApplicationModel> findByApplicationID(int applicationID) {
        return applications.stream()
                .filter(app -> app.getApplicationID() == applicationID)
                .findFirst();
    }

    public Optional<BTOApplicationModel> findByNRIC(String nric) {
        return applications.stream()
                .filter(app -> app.getApplicantNRIC().equals(nric))
                .findFirst();
    }

    public boolean hasUserAppliedForProject(int userID, int projectID) {
        for (BTOApplicationModel application : applications) {
            if (application.getApplicantUserID() == userID && application.getProjectID() == projectID) {
                return true; // If an application exists return true, no matter status
            }// therefore can be PENDING,SUCCESSFUL,BOOKED,WITHDRAWN.
        }
        return false;
    }

    public ApplicationStatus getApplicationStatus(int userID, int projectID) {
        for (BTOApplicationModel application : applications) {
            if (application.getApplicantUserID() == userID && application.getProjectID() == projectID) {
                return application.getStatus();
            }
        }
        return null;
    }
}
