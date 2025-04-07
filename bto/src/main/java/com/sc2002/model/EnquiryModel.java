package com.sc2002.model;

import java.util.Date;
public class EnquiryModel {
    private static int nextEnquiryId = 0; // Static counter for auto-incrementing IDs
    private int enquiryID;
    private String submittedByApplicantNRIC;
    private int projectID;
    private String enquiryText;
    private Date submissionDate;
    private EnquiryResponseModel enquiryResponse;

    public EnquiryModel(String applicantNRIC, int projectID, String enquiryText){
        this.enquiryID = nextEnquiryId++; // Assign current ID and increment for next use
        this.submittedByApplicantNRIC=applicantNRIC;
        this.projectID=projectID;
        this.enquiryText=enquiryText;
        this.submissionDate = new Date(); // Initialize submission date
        this.enquiryResponse = null; // Initialize enquiry response
    }

    public int getId(){
        return this.enquiryID;
    }
    public String getApplicantNRIC(){
        return this.submittedByApplicantNRIC;
    }

    public int getProjectId() {
        return this.projectID;
    }

    public boolean getStatus(){ // return true if replied, else false
        return enquiryResponse != null;
    }

    public String getEnquiryText() {
        return this.enquiryText;
    }

    public void replyEnquiry(String officerResponse, int officerUserID) {
        // Create a new EnquiryResponse with the provided details
        EnquiryResponseModel response = new EnquiryResponseModel(officerResponse,this.enquiryID,officerUserID);
        // Set the response for this enquiry
        this.enquiryResponse = response;
    }

    public void editEnquiry(String newEnquiryText) {
        // Update the enquiry text with the new text
        this.enquiryText = newEnquiryText;
        // Assuming garbage collection will handle the old response
        // Reset the response since the enquiry has been edited
        this.enquiryResponse = null;
    }

    
    public String getFormattedEnquiry() {
        StringBuilder strToReturn = new StringBuilder();
        strToReturn.append("Enquiry ID: ").append(this.enquiryID).append("\n");
        strToReturn.append("Submitted by: ").append(this.submittedByApplicantNRIC).append("\n");
        strToReturn.append("Project ID: ").append(this.projectID).append("\n");
        strToReturn.append("Submission Date: ").append(this.submissionDate).append("\n");
        strToReturn.append("Enquiry: ").append(this.enquiryText).append("\n");
        
        // Check if there's a response
        if (enquiryResponse != null) {
            strToReturn.append("\nResponse:\n");
            strToReturn.append("Staff ID: ").append(this.enquiryResponse.getOfficerUserId()).append("\n");
            strToReturn.append("Response Date: ").append(this.enquiryResponse.getResponseDate()).append("\n");
            strToReturn.append("Response: ").append(this.enquiryResponse.getResponseText());
        } else {
            strToReturn.append("\nStatus: Pending Response");
        }
        
        return strToReturn.toString();
    }
}


