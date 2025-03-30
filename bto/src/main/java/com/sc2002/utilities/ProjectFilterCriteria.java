package com.sc2002.utilities;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.sc2002.entities.BTOProjectModel; 
import com.sc2002.enums.FlatType;

public class ProjectFilterCriteria {
    private String location;
    private FlatType flatType;

    public ProjectFilterCriteria(String location, FlatType flatType) {
        this.location = location;
        this.flatType = flatType;
    }

    // Getters
    public String getLocation() { 
        return location; 
    }
    public FlatType getFlatType() { 
        return flatType; 
    }
}