package com.sc2002.controller;

import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.ApplicantModel;

public class ProjectService {
    public void viewProjectByID(AppContext appContext,int projectID){
        try{
            if (appContext.getAuthService().isApplicant(appContext.getCurrentUser())){
                // viewProject for applicant, cannot view if not visible
                BTOProjectModel project = appContext.getProjectRepo().findByProjectID(projectID);
                if(project!=null){
                    ApplicantModel currentUser = (ApplicantModel)appContext.getCurrentUser();
                    if(project.isVisibility()) project.printAll();
                    else if(currentUser.getAppliedProjectID()==projectID){ // is safe since projectID in BTOProjectModel is final int
                        project.printAll();
                    }else{
                        throw new RuntimeException("No Project Found.");
                    }
                }else throw new RuntimeException("No Project Found.");
            }
            else if (appContext.getAuthService().isOfficer(appContext.getCurrentUser()) || appContext.getAuthService().isManager(appContext.getCurrentUser())){
                // viewProject for Officer/Manager, view regardless of visilibty
                BTOProjectModel project = appContext.getProjectRepo().findByProjectID(projectID);
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
}
