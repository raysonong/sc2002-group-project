package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sc2002.model.EnquiryModel;
import com.sc2002.interfaces.RepoInterface; // Ensure this is the correct package for RepoInterface

/**
 * Manages the storage and retrieval of enquiry data.
 * Implements the RepoInterface for standard repository operations.
 */
public class EnquiryRepo implements RepoInterface<EnquiryModel, Integer> {
    /** In-memory list to store all enquiry instances. */
    private List<EnquiryModel> enquiries;
    
    /**
     * Constructor initializes an empty list of enquiries.
     */
    public EnquiryRepo() {
        this.enquiries = new ArrayList<>();
    }
    
    /**
     * Saves or updates an enquiry in the repository.
     * If an enquiry with the same ID exists, it replaces the existing one.
     * Otherwise, it adds the new enquiry to the list.
     *
     * @param enquiry The EnquiryModel object to save or update.
     */
    @Override
    public void save(EnquiryModel enquiry) {
        // Check for existing enquiry with the same ID
        for (int i = 0; i < enquiries.size(); i++) {
            if (enquiries.get(i).getID() == enquiry.getID()) {
                enquiries.set(i, enquiry);
                return;
            }
        }
        // If no existing enquiry found, add new one
        enquiries.add(enquiry);
    }
    
    /**
     * Retrieves a list of all enquiries currently stored in the repository.
     *
     * @return A new ArrayList containing all EnquiryModel objects.
     */
    @Override
    public List<EnquiryModel> findAll() {
        return new ArrayList<>(enquiries);
    }
    
    /**
     * Finds an enquiry by its unique Enquiry ID.
     * Iterates through the list to find a match.
     *
     * @param enquiryID The ID of the enquiry to find.
     * @return The found EnquiryModel object, or null if no enquiry matches the ID.
     */
    @Override
    public EnquiryModel findByID(Integer enquiryID) {
        for (EnquiryModel enquiry : enquiries) {
            if (enquiry.getID() == enquiryID) {
                return enquiry;
            }
        }
        return null;
    }
    
    @Override
    public boolean delete(Integer enquiryID) {
        EnquiryModel enquiryToDelete = findByID(enquiryID);
        if (enquiryToDelete!=null) {
            enquiries.remove(enquiryToDelete);
            return true;
        }
        return false;
    }
    
    /**
     * Find an enquiry by its unique Enquiry ID.
     * 
     * @param enquiryID The ID of the enquiry to find.
     * @return The found enquiry, or null if no enquiry has that ID.
     */
    public EnquiryModel findByID(int enquiryID) {
        return this.enquiries.stream()
            .filter(enquiry -> enquiry.getID() == enquiryID)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Save an enquiry to the repository
     * 
     * @param enquiry The enquiry to save
     * @return The saved enquiry
     */
    public EnquiryModel saveEnquiry(EnquiryModel enquiry) {
        // Check if the enquiry already exists
        EnquiryModel existingEnquiry = findByID(enquiry.getID());
        
        if(existingEnquiry!=null) {
            // Remove the existing enquiry
            enquiries.remove(existingEnquiry);
        }
        
        // Add the new or updated enquiry
        enquiries.add(enquiry);
        return enquiry;
    }
    
    /**
     * Find all enquiries by applicant NRIC
     * 
     * @param nric The NRIC of the applicant
     * @return List of enquiries associated with the applicant
     */
    public List<EnquiryModel> findByApplicantNRIC(String nric) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getApplicantNRIC().equals(nric))
                .collect(Collectors.toList());
    }
    
    /**
     * Find all enquiries by project ID
     * 
     * @param projectID The ID of the project
     * @return List of enquiries associated with the project
     */
    public List<EnquiryModel> findByProjectID(int projectID) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getProjectID() == projectID)
                .collect(Collectors.toList());
    }
    
}
