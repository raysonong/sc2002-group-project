package com.sc2002.controller;

import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.Enquiry;
import com.sc2002.enums.UserRole;
import java.util.List;

public class EnquiryService {
    // submitEnquiry for Applicant and Officer(Who have a project they BTOed)

    // viewAllEnquiry for Managers 
    public List<Enquiry> getAllEnquiries(AppContext appContext) {
        // Check if the current user is authenticated and has the correct role
        try{
            if (appContext.getCurrentUser() != null && appContext.getAuthService().isManager(appContext.getCurrentUser())) {
                return appContext.getEnquiryRepo().findAll();
            }
            throw new RuntimeException("User is not authorized to perform this action.");
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    // viewManagingEnquiry for HDBOfficer

    // viewEnquiryByUserID for HDBOfficer and Applicant (make sure to do your checks)
}
