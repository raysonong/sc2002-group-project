package com.sc2002.model;

import com.sc2002.enums.OfficerRegistrationStatus;

public class OfficerRegistrationModel {
    private static int nextRegistrationId = 1; // Static counter for auto-incrementing IDs
    private final int registrationId;
    private UserModel officer;
    private int projectID;
    private OfficerRegistrationStatus status;

    public OfficerRegistrationModel(UserModel officer, int projectID) {
        this.registrationId = nextRegistrationId++;
        this.officer = officer;
        this.projectID = projectID;
        this.status = OfficerRegistrationStatus.PENDING;
    }

    public int getRegistrationId(){
        return this.registrationId;
    }

    public int getProjectID() {
        return this.projectID;
    }

    public String getOfficerName(){
        return this.officer.getName();
    }

    public int getUserID() {
        return this.officer.getUserID();
    }

    public UserModel getOfficerUser(){
        return this.officer;
    }

    public OfficerRegistrationStatus getStatus() {
        return this.status;
    }

    public void setStatus(OfficerRegistrationStatus status){
        this.status=status;
    }
}
