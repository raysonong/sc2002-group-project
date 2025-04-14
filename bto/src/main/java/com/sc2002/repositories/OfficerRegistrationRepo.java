package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.interfaces.RepoInterface; // Ensure this is the correct package for RepoInterface

public class OfficerRegistrationRepo implements RepoInterface<OfficerRegistrationModel, Long> {
    private List<OfficerRegistrationModel> applications;
    
    /**
     * Constructor initializes an empty list of applications
     */
    public OfficerRegistrationRepo() {
        this.applications = new ArrayList<>();
    }

    @Override
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

    @Override
    public List<OfficerRegistrationModel> findAll() {
        return new ArrayList<>(applications);
    }

    @Override
    public OfficerRegistrationModel findByID(Long userID) {
        Optional<OfficerRegistrationModel> result = findbyUserID(userID);
        return result.orElse(null);
    }

    @Override
    public boolean delete(Long userID) {
        Optional<OfficerRegistrationModel> application = findbyUserID(userID);
        if (application.isPresent()) {
            applications.remove(application.get());
            return true;
        }
        return false;
    }

    public Optional<OfficerRegistrationModel> findbyUserID(long userId) {
        for (OfficerRegistrationModel application : this.applications) {
            if (application.getUserID() == userId) {
                return Optional.of(application);
            }
        }
        return Optional.empty();
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
