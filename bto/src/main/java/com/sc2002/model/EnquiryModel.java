package com.sc2002.model;

import java.util.Date;
/**
 * Represents a question or enquiry submitted by an applicant about a specific BTO project.
 * It holds the enquiry details, submission info, and any response provided by an HDB staff member.
 */
public class EnquiryModel {
    /** Counter to give each new enquiry a unique ID. */
    private static int nextEnquiryID = 0; // Static counter for auto-incrementing IDs
    /** The unique ID for this specific enquiry. */
    private int enquiryID;
    /** The NRIC of the applicant who submitted the enquiry. */
    private String submittedByApplicantNRIC;
    /** The ID of the BTO project this enquiry is about. */
    private int projectID;
    /** The actual text of the applicant's question. */
    private String enquiryText;
    /** When the enquiry was first submitted. */
    private Date submissionDate;
    /** Holds the response from an HDB staff member, if one exists. Null if not yet replied. */
    private EnquiryResponseModel enquiryResponse;

    /**
     * Creates a new enquiry instance.
     * Automatically assigns a unique ID and sets the submission date to now.
     * The response is initially null.
     *
     * @param applicantNRIC The NRIC of the applicant asking the question.
     * @param projectID The ID of the project the question is about.
     * @param enquiryText The applicant's question.
     */
    public EnquiryModel(String applicantNRIC, int projectID, String enquiryText){
        this.enquiryID = nextEnquiryID++; // Assign current ID and increment for next use
        this.submittedByApplicantNRIC=applicantNRIC;
        this.projectID=projectID;
        this.enquiryText=enquiryText;
        this.submissionDate = new Date(); // Initialize submission date
        this.enquiryResponse = null; // Initialize enquiry response
    }

    /**
     * Gets the unique ID of this enquiry.
     * @return The enquiry ID.
     */
    public int getID(){
        return this.enquiryID;
    }

    /**
     * Gets the NRIC of the applicant who submitted the enquiry.
     * @return The applicant's NRIC.
     */
    public String getApplicantNRIC(){
        return this.submittedByApplicantNRIC;
    }

    /**
     * Gets the ID of the project this enquiry relates to.
     * @return The project ID.
     */
    public int getProjectID() {
        return this.projectID;
    }

    /**
     * Checks if the enquiry has been replied to.
     * @return True if there is a response, false otherwise.
     */
    public boolean getStatus(){ // return true if replied, else false
        return enquiryResponse != null;
    }

    /**
     * Gets the text of the applicant's enquiry.
     * @return The enquiry question.
     */
    public String getEnquiryText() {
        return this.enquiryText;
    }

    /**
     * Adds a response to this enquiry.
     * Creates and stores a new response linked to this enquiry.
     *
     * @param officerResponse The text of the response from the HDB staff.
     * @param officerUserID The ID of the HDB staff member who replied.
     */
    public void replyEnquiry(String officerResponse, int officerUserID) {
        // Create a new EnquiryResponse with the provided details
        EnquiryResponseModel response = new EnquiryResponseModel(officerResponse,this.enquiryID,officerUserID);
        // Set the response for this enquiry
        this.enquiryResponse = response;
    }

    /**
     * Allows the applicant to edit their submitted enquiry *before* it has been replied to.
     * Updates the enquiry text and resets the submission date.
     * Any previous response (if somehow added) is cleared.
     *
     * @param newEnquiryText The updated text for the enquiry.
     * @return True, indicating the edit was processed (though success might depend on whether it was already replied to in a real scenario).
     */
    public boolean editEnquiry(String newEnquiryText) {
        // Update the enquiry text with the new text
        this.enquiryText = newEnquiryText;
        // Assuming garbage collection will handle the old response
        this.submissionDate= new Date();
        // Reset the response since the enquiry has been edited
        this.enquiryResponse = null;
        
        return true;
    }

    /**
     * Generates a formatted string containing all details of the enquiry and its response (if available).
     * Useful for displaying the enquiry information to users.
     *
     * @return A string with the enquiry ID, submitter, project ID, dates, text, and response details (or pending status).
     */
    public String getFormattedEnquiry() {
        StringBuilder strToReturn = new StringBuilder();
        strToReturn.append("\nEnquiry ID: ").append(this.enquiryID).append("\n");
        strToReturn.append("Submitted by: ").append(this.submittedByApplicantNRIC).append("\n");
        strToReturn.append("Project ID: ").append(this.projectID).append("\n");
        strToReturn.append("Submission Date: ").append(this.submissionDate).append("\n");
        strToReturn.append("Enquiry: ").append(this.enquiryText).append("\n");
        
        // Check if there's a response
        if (enquiryResponse != null) {
            strToReturn.append("\nResponse:\n");
            strToReturn.append("Staff ID: ").append(this.enquiryResponse.getOfficerUserID()).append("\n");
            strToReturn.append("Response Date: ").append(this.enquiryResponse.getResponseDate()).append("\n");
            strToReturn.append("Response: ").append(this.enquiryResponse.getResponseText());
        } else {
            strToReturn.append("\nStatus: Pending Response");
        }
        
        return strToReturn.toString();
    }
}


