package com.sc2002.entities;

import java.util.Date;

public class Enquiry {
    private int enquiryId;
    private String submittedByApplicantNRIC;
    private int projectId;
    private String enquiryText;
    private Date submissionDate;
    private EnquiryResponse enquiryResponse;

    public Enquiry(String applicantNRIC, int applicantProjectId, String applicantEnquiryText){
        // get next enquiry id from enquiry repo
        // 
        submittedByApplicantNRIC=applicantNRIC;
        projectId=applicantProjectId;
        enquiryText=applicantEnquiryText;
        enquiryResponse = new EnquiryResponse(enquiryId);
    }

    public int getId(){
        return enquiryId;
    }
    public String getApplicantNRIC(){
        return submittedByApplicantNRIC;
    }

	public int getProjectId() {
		return projectId;
    }
}


