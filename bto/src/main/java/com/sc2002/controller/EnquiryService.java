package com.sc2002.controller;


import java.util.List;
import java.util.Optional;

import com.sc2002.model.EnquiryModel;
import com.sc2002.repositories.EnquiryRepo;

public class EnquiryService {
    // submitEnquiry for Applicant and Officer(Who have a project they BTOed)
    // didnt see any other submitEnquiry methods so i put here
    private AppContext appContext;

    public EnquiryService(AppContext appContext) {
        this.appContext = appContext;
    }

    public boolean submitEnquiry(String applicantNRIC, int projectId, String enquiryText) {
    // create a new enquiry
    EnquiryModel newEnquiry = new EnquiryModel(applicantNRIC, projectId, enquiryText);

    // save the new enquiry
    appContext.getEnquiryRepo().saveEnquiry(newEnquiry);
    System.out.println("Your enquiry has been submitted successfully!");
    return true;
}

    // viewAllEnquiry for Managers & Officer (Mostly for printing menu related tasks)
    public List<EnquiryModel> getAllEnquiries() {
        // Check if the current user is authenticated and has the correct role
        try{
            if (this.appContext.getCurrentUser() != null && this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())) {
                return this.appContext.getEnquiryRepo().findAll();
            }else if (this.appContext.getCurrentUser() != null && this.appContext.getAuthService().isOfficer(this.appContext.getCurrentUser())) {
                // @rayson, was thinking you put your getAllEnquiries here, (this is for printing your menus)
                // Then sort to only return those which officer by right can view (you can code the sorting either in repo or here)
                throw new RuntimeException("Not implemented");

            }else throw new RuntimeException("User is not authorized to perform this action.");
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public boolean viewEnquiry(int enquiryID){
        try{
            if(this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())){
                Optional<EnquiryModel> enquiry = this.appContext.getEnquiryRepo().findById(enquiryID);
                if (enquiry.isPresent()) {
                    enquiry.get().getFormattedEnquiry();
                    return true;
                } else {
                    throw new RuntimeException("Project not found.");
                }
            }else if (this.appContext.getAuthService().isOfficer(this.appContext.getCurrentUser())){
                // @ Rayson, this views the exact enquiry,
                // idea for flow of enquiry editing
                // 1) print menu using getAllEnquiry
                // 2) viewEnquiry(index) to view the exact enquiry
                // 3) editEnquiry(index) to edit that exact enquiry
                throw new RuntimeException("Not implemented");
            }else{
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        }catch(RuntimeException e){
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean editEnquiryResponse( int enquiryID, String response){
        try{
            if(this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())){
                // Manager can reply any so no need to do extra checking
                Optional<EnquiryModel> enquiry = this.appContext.getEnquiryRepo().findById(enquiryID);
                if (enquiry.isPresent()) {
                    enquiry.get().replyEnquiry(response, this.appContext.getCurrentUser().getUserID());
                    return true;
                } else {
                    throw new RuntimeException("Project not found.");
                }
            }else if (this.appContext.getAuthService().isOfficer(this.appContext.getCurrentUser())){
                // @ Rayson, this views the exact enquiry,
                // idea for flow of enquiry editing
                // 1) print menu using getAllEnquiry
                // 2) viewEnquiry(index) to view the exact enquiry
                // 3) editEnquiry(index) to edit that exact enquiry
                throw new RuntimeException("Not implemented");
            }else{
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        }catch(RuntimeException e){
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
    // viewEnquiryByUserID for HDBOfficer and Applicant (make sure to do your checks)

    //delete enquiry
    public boolean deleteEnquiry(int enquiryId) {
        return appContext.getEnquiryRepo().deleteById(enquiryId);
    }
}
