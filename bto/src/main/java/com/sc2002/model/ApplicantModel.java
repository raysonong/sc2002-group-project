package com.sc2002.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sc2002.enums.FlatType; // for FlatType enums
import com.sc2002.enums.UserRole; // for UserRole enums

public class ApplicantModel extends User {
    // contain ID of project applicant applied to
    private int appliedProject; // ProjectID in INT
    
    // Contains either TWO_ROOM or THREE_ROOM (MIGHT NOT NEED)
    private FlatType bookedFlatType; // view com.sc2002.enums.FlatType for more info

    // Contains Project booked(NOT SURE HOW WE GONNA HANDLE BTOPROJECT OBJECT)
    private BTOProjectModel bookedProject;

    public ApplicantModel(String name, String nric, int age, String isMarried, String password){
        super(nric, name, age, isMarried, password,UserRole.APPLICANT);
    }

    @Override
    public List<String> getMenuOptions() {
        List<String> options = new ArrayList<>();
        options.addAll(Arrays.asList(
        "Apply for BTO Project",
        "View Application Status",
        "Update Flat Details",
        "Generate Flat Selection Receipt",
        "Submit Enquiry",
        "View Enquiry"
        ));
        return options;
    }
    public int getAppliedProjectID(){ // may not be needed
        return this.appliedProject;
    }

    public FlatType getFlatType(){ // may not be needed
        return this.bookedFlatType;
    }

}


