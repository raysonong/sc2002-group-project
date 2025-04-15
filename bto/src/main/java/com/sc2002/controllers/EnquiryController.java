package com.sc2002.controllers;

import java.util.List;

import com.sc2002.config.AppContext;
import com.sc2002.model.EnquiryModel;
import com.sc2002.services.EnquiryService;

/**
 * Controller for handling enquiry-related operations.
 * Acts as an intermediary between the view and the EnquiryService.
 */
public class EnquiryController {
    
    private EnquiryService enquiryService;
    
    /**
     * Constructor for EnquiryController.
     * 
     * @param appContext The application context
     */
    public EnquiryController(AppContext appContext) {
        this.enquiryService = new EnquiryService(appContext);
    }
    
    /**
     * Submits a new enquiry.
     * 
     * @param applicantNRIC The NRIC of the applicant
     * @param projectId The ID of the project
     * @param enquiryText The enquiry text
     * @return true if successful, false otherwise
     */
    public boolean submitEnquiry(String applicantNRIC, int projectId, String enquiryText) {
        return enquiryService.submitEnquiry(applicantNRIC, projectId, enquiryText);
    }
    
    /**
     * Gets all enquiries that the current user has access to.
     * 
     * @return List of enquiries
     */
    public List<EnquiryModel> getAllEnquiries() {
        return enquiryService.getAllEnquiries();
    }
    
    /**
     * Views an enquiry by its ID.
     * 
     * @param enquiryID The ID of the enquiry
     * @return true if successful, false otherwise
     */
    public boolean viewEnquiry(int enquiryID) {
        return enquiryService.viewEnquiry(enquiryID);
    }
    
    /**
     * Edits the response of an enquiry.
     * 
     * @param enquiryID The ID of the enquiry
     * @param response The response text
     * @return true if successful, false otherwise
     */
    public boolean editEnquiryResponse(int enquiryID, String response) {
        return enquiryService.editEnquiryResponse(enquiryID, response);
    }
    
    /**
     * Deletes an enquiry.
     * 
     * @param enquiryId The ID of the enquiry to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteEnquiry(int enquiryId) {
        return enquiryService.deleteEnquiry(enquiryId);
    }
}
