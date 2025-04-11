package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sc2002.model.EnquiryModel;
// do double check & change if necessary (delete this comment before submission)
public class EnquiryRepo {
    private List<EnquiryModel> enquiries;
    
    /**
     * Constructor initializes an empty list of enquiries
     */
    public EnquiryRepo() {
        this.enquiries = new ArrayList<>();
    }
    
    /**
     * Find an enquiry by its ID
     * 
     * @param id The ID of the enquiry to find
     * @return Optional containing the enquiry if found, empty otherwise
     */
    public EnquiryModel findById(int enquiryID) {
        return this.enquiries.stream()
            .filter(enquiry -> enquiry.getId() == enquiryID)
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
        EnquiryModel existingEnquiry = findById(enquiry.getId());
        
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
     * @param projectId The ID of the project
     * @return List of enquiries associated with the project
     */
    public List<EnquiryModel> findByProjectId(int projectId) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getProjectId() == projectId)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all enquiries in the repository
     * 
     * @return List of all enquiries
     */
    public List<EnquiryModel> findAll() {
        return new ArrayList<>(enquiries);
    }

    public boolean deleteById(int enquiryId) {
        EnquiryModel enquiryToDelete = findById(enquiryId);
        if (enquiryToDelete!=null) {
            enquiries.remove(enquiryToDelete);
            return true;
        }
        return false;
    }


}
