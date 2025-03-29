package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.sc2002.entities.Enquiry;
import com.sc2002.entities.User;

public class ApplicationRepo {
    private List<Enquiry> applications;
    
    /**
     * Constructor initializes an empty list of applications
     */
    public ApplicationRepo() {
        this.applications = new ArrayList<>();
    }

    public Optional<BTOApplication> findbyID(long applicationId) {
        for (BTOApplication application : applications) {
            if (application.getNRIC().equals(application)) {
                return application;
            }
        }
        return null;
    }

    public void save(BTOApplication application) {
        // Check if the application already exists
        Optional<BTOApplication> existingApplication = findById(String.valueOf(application.getId()));
        
        if(existingApplication.isPresent()) {
            // Remove the existing application
            applications.remove(existingApplication.get());
        }
        
        // Add the new or updated application
        applications.add(application);

        return application;
    }

    public Optional<BTOApplication> findActiveByApplicantID(String userID) {
        return applications.stream()
                .filter(application -> application.getApplicantNRIC().equals(userID))
                .collect(Collectors.toList());
    }

    public List<BTOApplication> findByProjectID(String projectID) {
        return applications.stream()
        .filter(enquiry -> String.valueOf(enquiry.getProjectId()).equals(projectID))
        .collect(Collectors.toList());
    }
}
