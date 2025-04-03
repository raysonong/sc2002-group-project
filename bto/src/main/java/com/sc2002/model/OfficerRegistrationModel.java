package com.sc2002.model;

import com.sc2002.enums.OfficerRegistrationStatus;

public class OfficerRegistrationModel {
    private static int nextRegistrationId = 1; // Static counter for auto-incrementing IDs

    private int registrationId;
    private String officerNRIC;
    private int officerUserID;
    private int projectID;
    private OfficerRegistrationStatus status;

    public OfficerRegistrationModel(String officerNRIC, int officerUserID, int projectID) {
        this.registrationId = nextRegistrationId++;
        this.officerNRIC = officerNRIC;
        this.officerUserID = officerUserID;
        this.projectID = projectID;
        this.status = OfficerRegistrationStatus.PENDING;
    }

    public int getProjectID() {
        return this.projectID;
    }

    public int getUserID() {
        return this.officerUserID;
    }

    public OfficerRegistrationStatus getStatus() {
        return this.status;
    }
}
