package com.sc2002.utilities;


import com.sc2002.enums.FlatType;

public class ProjectFilterCriteria {
    private String location;
    private FlatType flatType;

    public ProjectFilterCriteria(String location, FlatType flatType) {
        this.location = location;
        this.flatType = flatType;
    }

    public String getLocation() { 
        return location; 
    }
    public FlatType getFlatType() { 
        return flatType; 
    }
}