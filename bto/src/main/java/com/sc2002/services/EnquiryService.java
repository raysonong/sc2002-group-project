package com.sc2002.services;


import java.util.ArrayList;
import java.util.List;

import com.sc2002.config.AppContext;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.EnquiryModel;

public class EnquiryService {
    // submitEnquiry for Applicant and Officer(Who have a project they BTOed)
    // didnt see any other submitEnquiry methods so i put here
    private AppContext appContext;

    public EnquiryService(AppContext appContext) {
        this.appContext = appContext;
    }

    public boolean submitEnquiry(String applicantNRIC, int projectId, String enquiryText) {
        // create a new enquiry
        if (applicantNRIC == null || enquiryText == null || enquiryText.isBlank()) {
            return false;
        }

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
                List<EnquiryModel> toReturn=new ArrayList<>();
                List<BTOProjectModel> projects=this.appContext.getProjectRepo().getProjectsByOfficer(appContext.getCurrentUser()); // [projectID,projectname]
                for (BTOProjectModel project : projects) {
                    int projectId = project.getProjectID();
                    List<EnquiryModel> enquiries=this.appContext.getEnquiryRepo().findByProjectId(projectId);
                    toReturn.addAll(enquiries);
                } // NOT TESTED
                return toReturn;

            }else throw new RuntimeException("User is not authorized to perform this action.");
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public boolean viewEnquiry(int enquiryID) {
        try{
            EnquiryModel enquiry = this.appContext.getEnquiryRepo().findByID(enquiryID);

            if(this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())){
                // for managers, can reply all
                if (enquiry!=null) {
                    System.out.println(enquiry.getFormattedEnquiry()); // we print it in the service
                    return true;
                } else {
                    throw new RuntimeException("Project not found.");
                }
            }else if (this.appContext.getAuthService().isOfficer(this.appContext.getCurrentUser())){
                // for officer only managing projects can reply
                // @ Rayson, this views the exact enquiry,
                // idea for flow of enquiry editing
                // 1) print menu using getAllEnquiry
                // 2) viewEnquiry(index) to view the exact enquiry
                // 3) editEnquiry(index) to edit that exact enquiry
                if (enquiry!=null) {
                    if (appContext.getProjectRepo().findByID(enquiry.getID()).isManagingOfficer(appContext.getCurrentUser())) {
                        System.out.println(enquiry.getFormattedEnquiry()); // we print it in the service
                        return true;
                    }
                    else { // might need to check if officer is applicant, will see when we debugging @rayson
                        throw new RuntimeException("User is not an officer for project.");
                    }
                }
                else {
                    throw new RuntimeException("Project not found.");
                }
                
                // Check if officer can viewEnquiry
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
            EnquiryModel enquiry = this.appContext.getEnquiryRepo().findByID(enquiryID);
            if(this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())){
                if(appContext.getProjectRepo().findByID(enquiry.getProjectID()).getManagerUserID()!=appContext.getCurrentUser().getUserID()){
                    throw new RuntimeException("You are not Project Manager of this enquiry's project.");
                }
                if (enquiry!=null) {
                    enquiry.replyEnquiry(response, this.appContext.getCurrentUser().getUserID());
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
                if (enquiry!=null) {
                    if(appContext.getProjectRepo().findByID(enquiry.getProjectID()).isManagingOfficer(appContext.getCurrentUser())){
                        enquiry.replyEnquiry(response, this.appContext.getCurrentUser().getUserID());
                        return true;
                    }else{
                        throw new RuntimeException("User is not an officer for project.");
                    }
                } else {
                    throw new RuntimeException("Project not found.");
                }
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
        return appContext.getEnquiryRepo().delete(enquiryId);
    }
}
