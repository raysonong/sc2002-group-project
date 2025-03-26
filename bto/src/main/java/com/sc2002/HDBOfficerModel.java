package com.sc2002;

public class HDBOfficerModel extends User {
    private int projectID;
    private boolean registrationStatus;

    public HDBOfficerModel(String nric, String name, int age, String isMarried, String password) {
        super(nric, name, age, isMarried, password);
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getProjectID() {
        return this.projectID;
    }

    public boolean getRegistrationStatus() {
        return this.registrationStatus;
    }
}
