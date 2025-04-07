package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Optional<BTOApplicationModel> findActiveByApplicantID(String userID) {
        return applications.stream()
                .filter(application -> String.valueOf(application.getApplicantUserID()).equals(userID))
                .findFirst();
    }

    public List<BTOApplicationModel> findByProjectID(String projectID) {
        return applications.stream()
        .filter(application -> String.valueOf(application.getProjectID()).equals(projectID))
        .collect(Collectors.toList());
    }
}
