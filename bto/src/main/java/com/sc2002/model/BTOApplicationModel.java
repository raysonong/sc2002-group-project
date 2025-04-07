package com.sc2002.model;

import java.util.Date;

import com.sc2002.enums.ApplicationStatus;

public class BTOApplicationModel {
    private static int nextApplicationId = 1; // Static counter for auto-incrementing IDs

    private int applicationId;
    private String applicantNRIC;
    private int applicantUserID;
    private int projectID;
    private ApplicationStatus status;
    private Date submissionDate;

    public BTOApplicationModel(String applicantNRIC, int applicantUserID, int projectID) {
        this.applicationId = nextApplicationId++;
        this.applicantNRIC = applicantNRIC;
        this.applicantUserID = applicantUserID;
        this.projectID = projectID;
        this.status = ApplicationStatus.PENDING;
        this.submissionDate = new Date();
    }

    public long getApplicantUserID() {
        return this.applicantUserID;
    }

    public int getProjectID() {
        return this.projectID;
    }

    public ApplicationStatus getStatus() {
        return this.status;
    }

}
