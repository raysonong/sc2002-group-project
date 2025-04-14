package com.sc2002.controller;

import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.UserModel;
import com.sc2002.model.ApplicantModel;

public class ProjectService {
    private AppContext appContext;

    public ProjectService(AppContext appContext) {
        this.appContext = appContext;
    }

    public void viewProjectByID(int projectID){
        try{
            if (this.appContext.getAuthService().isApplicant(this.appContext.getCurrentUser())){
                // viewProject for applicant, cannot view if not visible
                BTOProjectModel project = this.appContext.getProjectRepo().findByID(projectID);
                if(project!=null){
                    ApplicantModel currentUser = (ApplicantModel)this.appContext.getCurrentUser();
                    if(project.isVisible()) project.printAll();
                    else if(currentUser.getAppliedProjectID()==projectID){ // is safe since projectID in BTOProjectModel is final int
                        project.printAll();
                    }else{
                        throw new RuntimeException("No Project Found.");
                    }
                }else throw new RuntimeException("No Project Found.");
            }
            else if (this.appContext.getAuthService().isOfficer(this.appContext.getCurrentUser()) || this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())){
                // viewProject for Officer/Manager, view regardless of visilibty
                BTOProjectModel project = this.appContext.getProjectRepo().findByID(projectID);
                if(project!=null) project.printAll();
                else throw new RuntimeException("No Project Found.");
            }else{
                throw new RuntimeException("UserRole undefined.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return;
        }
    }

    public BTOProjectModel viewManagingProject(){ // Returns the project user is managing
        UserModel currentUser = appContext.getCurrentUser();
        try{
            if(appContext.getAuthService().isManager(currentUser)){
                return appContext.getProjectRepo().getProjectsByManagerID(currentUser.getUserID()).stream().findFirst().orElse(null);

    
            }else if(appContext.getAuthService().isOfficer(currentUser)){
                return appContext.getProjectRepo().getProjectsByOfficer(currentUser).stream().findFirst().orElse(null);
            }else{
                throw new RuntimeException("User does not manage Projects.");
            }
        }catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }
}
