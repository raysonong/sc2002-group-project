package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sc2002.model.OfficerRegistrationModel;


public class OfficerRegistrationRepo {
    private List<OfficerRegistrationModel> applications;
    
    /**
     * Constructor initializes an empty list of applications
     */
    public OfficerRegistrationRepo() {
        this.applications = new ArrayList<>();
    }

    public Optional<OfficerRegistrationModel> findbyUserID(long userId) {
        for (OfficerRegistrationModel application : this.applications) {
            if (application.getUserID() == userId) {
                return Optional.of(application);
            }
        }
        return Optional.empty();
    }

    public void save(OfficerRegistrationModel application) {
        // Check if the application already exists
        Optional<OfficerRegistrationModel> existingApplication = findbyUserID(application.getUserID());
        
        if(existingApplication.isPresent()) {
            // Remove the existing application
            applications.remove(existingApplication.get());
        }
        
        // Add the new or updated application
        applications.add(application);
    }

    public Optional<OfficerRegistrationModel> findActiveByOfficerID(String userID) {
        return applications.stream()
                .filter(application -> String.valueOf(application.getUserID()).equals(userID))
                .findFirst();
    }

    public List<OfficerRegistrationModel> findByProjectID(String projectID) {
        return applications.stream()
        .filter(application -> String.valueOf(application.getProjectID()).equals(projectID))
        .collect(Collectors.toList());
    }


}
