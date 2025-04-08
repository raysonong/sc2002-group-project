package com.sc2002.model;

import java.util.Date;

import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;

public class BTOApplicationModel {
    private static int nextApplicationId = 1; // Static counter for auto-incrementing IDs
    private int applicationId;

    private User applicantUser;
    private BTOProjectModel project;
    private ApplicationStatus status;
    private FlatType applicationFlatType;
    private BookingDetailModel BookedUnit;
    private Date submissionDate;

    public BTOApplicationModel(User applicantUser, BTOProjectModel project, FlatType applicationFlatType) {
        //Cond 1, Singles 35 year old and above, can only apply for 2-Room
        if(applicantUser.getAge()<35 && !applicantUser.getMaritialStatus()){ // Checks for singles less than 35
            throw new RuntimeException("User is Single & age less than 35.");
        } 
        //Cond 2, Married, 21 years old and above, can apply for any flat type
        // No need check 21 lesser since we allow users to register only if they are above 21
        // getMaritalStatus returns true if married.
        if(!applicantUser.getMaritialStatus() && applicationFlatType.equals(FlatType.THREE_ROOM)){ //  check for singles tryna get 3-room
            throw new RuntimeException("User is Single can't apply 3-Room.");
        }
        // free to apply for married individuals
        this.applicationId = nextApplicationId++;
        this.applicantUser=applicantUser;
        this.project=project;
        this.applicationFlatType=applicationFlatType;
        this.status = ApplicationStatus.PENDING;
        this.submissionDate = new Date();

    }

    public long getApplicantUserID() {
        return this.applicantUser.getUserID();
    }

    public int getProjectID() {
        return this.project.getProjectID();
    }

    public ApplicationStatus getStatus() {
        return this.status;
    }

}
