package com.sc2002.services;

import java.util.Scanner;

import com.sc2002.config.AppContext;
import com.sc2002.enums.OfficerRegistrationStatus;
import com.sc2002.enums.UserRole;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.OfficerRegistrationRepo;
import com.sc2002.repositories.ProjectRepo;

public class OfficerRegistrationService {
    private AppContext appContext;

    public OfficerRegistrationService(AppContext appContext) {
        this.appContext = appContext;
    }
    public boolean registerForProject() {
        // Initialize appContext required
        ProjectRepo projectRepo = this.appContext.getProjectRepo();
        ApplicationRepo applicationRepo = this.appContext.getApplicationRepo();
        OfficerRegistrationRepo officerRegistrationRepo = this.appContext.getOfficerRegistrationRepo();

        Scanner scanner = this.appContext.getScanner();
        UserModel currentUser = this.appContext.getCurrentUser();
        // Check role
        if(currentUser.getUsersRole() != UserRole.HDB_OFFICER) {
            System.out.println("You do not have permission to apply an application to join a project.");
            return false;
        }

        int input_projectId = 0;

        while (true) { 
            System.out.printf("Enter Project ID (Input -1 to return back to menu): ");
            if (scanner.hasNextInt()) {
                input_projectId = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                // Return back to menu
                if (input_projectId == -1) {
                    return false;
                }

                // Validate input
                if (projectRepo.findByID(input_projectId)==null) {
                    System.out.println("This project ID does not exist. Please enter a valid ID.");
                    return false;
                }

                // Check if the officer has already applied as an applicant
                if (applicationRepo.findByUserAndProject(currentUser.getUserID(), input_projectId) != null) {
                    System.out.println("You cannot register for a project you have already applied to as an applicant.");
                    return false;
                }

                break;
            } else {
                System.out.println("Invalid input. Please enter a valid ID.");
                scanner.nextLine(); // Consume invalid input
            }
        }

        // save in repository
        officerRegistrationRepo.save(new OfficerRegistrationModel(currentUser, input_projectId));

        return true;
    }

    public boolean approveRegistration(OfficerRegistrationModel registration){
        try{
            //Checking if isManager and is Project's manager
            BTOProjectModel project = this.appContext.getProjectRepo().findByID(registration.getProjectID());
            if(this.appContext.getAuthService().isManager(this.appContext.getCurrentUser()) && project.getManagerUserID()==this.appContext.getCurrentUser().getUserID()){
                // Add to project 
                if(project.addManagingOfficerUser(registration.getOfficerUser())){
                    registration.setStatus(OfficerRegistrationStatus.APPROVED);
                    return true;
                }else{
                    throw new RuntimeException("Failed to add Officer to project. (limit hit)");
                }
            }else{
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        }catch(RuntimeException e){
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
    public boolean rejectRegistration(OfficerRegistrationModel registration){
        try{
            //Checking if isManager and is Project's manager
            BTOProjectModel project = this.appContext.getProjectRepo().findByID(registration.getProjectID());
            if(this.appContext.getAuthService().isManager(this.appContext.getCurrentUser()) && project.getManagerUserID()==this.appContext.getCurrentUser().getUserID()){
                // Add to project 
                if(project.removeManagingOfficerUser(registration.getOfficerUser())){
                    registration.setStatus(OfficerRegistrationStatus.REJECTED);
                    return true;
                }else{
                    throw new RuntimeException("Failed to remove Officer from project.");
                }
            }else{
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        }catch(RuntimeException e){
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
}
