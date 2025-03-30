package com.sc2002.model;

import java.util.Date;
public class Enquiry {
    private static int nextEnquiryId = 0; // Static counter for auto-incrementing IDs
    private int enquiryId;
    private String submittedByApplicantNRIC;
    private int projectId;
    private String enquiryText;
    private Date submissionDate;
    private EnquiryResponse enquiryResponse;

    public Enquiry(String applicantNRIC, int projectId, String enquiryText){
        this.enquiryId = nextEnquiryId++; // Assign current ID and increment for next use
        this.submittedByApplicantNRIC=applicantNRIC;
        this.projectId=projectId;
        this.enquiryText=enquiryText;
        this.submissionDate = new Date(); // Initialize submission date
        this.enquiryResponse = null; // Initialize enquiry response
    }

    public int getId(){
        return this.enquiryId;
    }
    public String getApplicantNRIC(){
        return this.submittedByApplicantNRIC;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public void replyEnquiry(String officerResponse, int officerUserID) {
        // Create a new EnquiryResponse with the provided details
        EnquiryResponse response = new EnquiryResponse(officerResponse,this.enquiryId,officerUserID);
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

    
    public String getEnquiry() {
        StringBuilder strToReturn = new StringBuilder();
        strToReturn.append("Enquiry ID: ").append(this.enquiryId).append("\n");
        strToReturn.append("Submitted by: ").append(this.submittedByApplicantNRIC).append("\n");
        strToReturn.append("Project ID: ").append(this.projectId).append("\n");
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


