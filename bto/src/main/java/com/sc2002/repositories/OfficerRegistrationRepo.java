package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.interfaces.RepoInterface; // Ensure this is the correct package for RepoInterface

/**
 * Manages the storage and retrieval of officer registration application data.
 * Implements the RepoInterface for standard repository operations.
 */
public class OfficerRegistrationRepo implements RepoInterface<OfficerRegistrationModel, Long> {
    /** In-memory list to store all officer registration application instances. */
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

    /**
     * Finds an officer registration application associated with a specific User ID.
     * Assumes an officer can only have one active registration application at a time (returns the first found).
     *
     * @param userID The User ID of the officer.
     * @return An Optional containing the registration application if found, otherwise empty.
     */
    public Optional<OfficerRegistrationModel> findbyUserID(long userID) {
        for (OfficerRegistrationModel application : this.applications) {
            if (application.getUserID() == userID) {
                return Optional.of(application);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds the first active (non-approved, non-rejected) registration application for a given Officer User ID (provided as String).
     *
     * @param userID The User ID of the officer as a String.
     * @return An Optional containing the active registration application if found, otherwise empty.
     */
    public Optional<OfficerRegistrationModel> findActiveByOfficerID(String userID) {
        return applications.stream()
                .filter(application -> String.valueOf(application.getUserID()).equals(userID))
                .findFirst();
    }

    /**
     * Finds all officer registration applications associated with a specific Project ID (provided as String).
     *
     * @param projectID The ID of the project as a String.
     * @return A list of registration applications for the specified project.
     */
    public List<OfficerRegistrationModel> findByProjectID(String projectID) {
        return applications.stream()
        .filter(application -> String.valueOf(application.getProjectID()).equals(projectID))
        .collect(Collectors.toList());
    }
}
