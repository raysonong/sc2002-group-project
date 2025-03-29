package com.sc2002.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sc2002.enums.FlatType; // for FlatType enums
import com.sc2002.enums.UserRole; // for UserRole enums

public class HDBOfficerModel extends User {
    private int projectID;
    private boolean registrationStatus;

    // Contains either TWO_ROOM or THREE_ROOM (MIGHT NOT NEED)
    private FlatType bookedFlatType; // view com.sc2002.enums.FlatType for more info

    // Contains Project booked(NOT SURE HOW WE GONNA HANDLE BTOPROJECT OBJECT)
    private BTOProjectModel bookedProject;

    public HDBOfficerModel(String nric, String name, int age, String isMarried, String password) {
        super(nric, name, age, isMarried, password, UserRole.HDB_OFFICER);
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

    @Override
    public List<String> getMenuOptions() {
        List<String> options = new ArrayList<>();
        //options.addAll(super.getMenuOptions()); // Inherit Applicant's options
        options.addAll(Arrays.asList("Register for Project Team", "View Registration Status", "Update Flat Details",
                "Generate Flat Selection Receipt"));
        return options;
    }
}
