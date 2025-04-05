package com.sc2002.view;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.EnquiryService;
import com.sc2002.controller.ProjectService;

public class ApplicantView {
    // declare all the services required by Manager
    private final EnquiryService enquiryService= new EnquiryService();
    private final ProjectService projectService= new ProjectService();
    
    public void ApplicantMenu(AppContext appContext) {
        // TODO: Menu for Applicant
        System.out.println("Applicant Menu:");
    }
}
