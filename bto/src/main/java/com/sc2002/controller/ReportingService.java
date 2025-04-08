package com.sc2002.controller;

import com.sc2002.model.BTOProjectModel;
import com.sc2002.repositories.ApplicationRepo;

public class ReportingService {
    public void generateProjectReport(BTOProjectModel project, ApplicationRepo applicationRepo){
        // 1) List of Applicants and their respective flat booking - flat type, project name , age marital status.
        // 2) There should be filters to generate a list based on various categories,
        //      e.g. report of married applicants' choice of flat type
        try{
            throw new RuntimeException("Not implemented.");
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
