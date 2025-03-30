package com.sc2002.model;

import java.util.Date;

import com.sc2002.enums.*;

public class BTOApplication {
    private int applicationId;
    private String applicantNRIC;
    private int applicantUserID;
    private int projectID;
    private ApplicationStatus status;
    private Date submissionDate;

    public long getApplicantUserID() {
        return this.applicantUserID;
    }

    public int getProjectID() {
        return this.projectID;
    }

}
