package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sc2002.entities.BTOApplication;
import com.sc2002.entities.Enquiry;
import com.sc2002.entities.User;


public class ApplicationRepo {
    private List<BTOApplication> applications;
    
    /**
     * Constructor initializes an empty list of applications
     */
    public ApplicationRepo() {
        this.applications = new ArrayList<>();
    }

    public Optional<BTOApplication> findbyUserID(long applicationId) {
        for (BTOApplication application : this.applications) {
            if (application.getApplicantUserID() == applicationId) {
                return Optional.of(application);
            }
        }
        return Optional.empty();
    }

    public void save(BTOApplication application) {
        // Check if the application already exists
        Optional<BTOApplication> existingApplication = findbyUserID(application.getApplicantUserID());
        
        if(existingApplication.isPresent()) {
            // Remove the existing application
            applications.remove(existingApplication.get());
        }
        
        // Add the new or updated application
        applications.add(application);

        // No return statement needed for a void method
    }

    public Optional<BTOApplication> findActiveByApplicantID(String userID) {
        return applications.stream()
                .filter(application -> String.valueOf(application.getApplicantUserID()).equals(userID))
                .findFirst();
    }

    public List<BTOApplication> findByProjectID(String projectID) {
        return applications.stream()
        .filter(application -> String.valueOf(application.getProjectID()).equals(projectID))
        .collect(Collectors.toList());
    }
}
