package com.sc2002.controller;

import java.util.List;
import java.util.Scanner;

import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.utilities.Receipt;
import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.UserRole;
import com.sc2002.model.User;

public class ApplicationService {
    //
    // PS: ruba pls refer to my code, think i have accidentally did ur part :,) - rayson
    //
   private AppContext appContext;
   public ApplicationService(AppContext appContext){
       this.appContext=appContext;
   }
   public void viewAvailableProjectsForApplicant() {
        User currentUser = this.appContext.getCurrentUser();

        // get the list of available projects from the project repository
        List<BTOProjectModel> allProjects = this.appContext.getProjectRepo().getAllProjects();
        
        for (BTOProjectModel project : allProjects) {
            if (isProjectVisibleForApplicant(currentUser, project)) {
                project.printAll();
            }
        }
    }

    private boolean isProjectVisibleForApplicant(ApplicantModel applicant, BTOProjectModel project) {
        if (!project.isVisible()) {
            return false;
        }

        // criteria for single 35 years old and above can only apply for 2-Room flats
        if (applicant.getMaritalStatus().equals("SINGLE") && applicant.getAge() >= 35) {
            if (project.getFlatType() != FlatType.TWO_ROOM) {
                return false;
            }
        }

        // criteria for single 35 years old and above can only apply for 2-Room flats, hence we only allow viewing if
        // tworoom count > 0,
        if (!currentUser.getMaritialStatus() && currentUser.getAge() >= 35) {
            if (project.getTwoRoomCount() <=0) {
                return false;
            }
        }
        // For married individualds just check age
        if(currentUser.getAge()<21){ // this will remove married under 21, technically not needed since we only allow 21 above to register
            return false;
        }

        return true;
    }

    public BTOApplicationModel applyToProject(ProjectRepo projectRepo, Scanner scanner, User currentUser) {
        // Check role
        if(currentUser.getUsersRole() != UserRole.APPLICANT) {
            System.out.println("You do not have permission to apply an application to join a project.");
            return null;
        }
        ApplicantModel applicant = (ApplicantModel) currentUser;
        int input_projectId = 0;

        while (true) { 
            System.out.printf("Enter Project ID (Input -1 to return back to menu): ");
            if (scanner.hasNextInt()) {
                input_projectId = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                // Return back to menu
                if (input_projectId == -1) {
                    return null;
                }

                // Validate input
                if (projectRepo.getProjectByID(input_projectId)==null) {
                    System.out.println("This project ID does not exist. Please enter a valid ID.");
                }
                else {
                    break;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid ID.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        String userInput2;
        FlatType inputFlatType=null;
        while (true) { 
            System.out.printf("Enter Flat Type (2-Room or 3-Room): ");
            userInput2 = scanner.nextLine().trim().toLowerCase();
            
            // Validate input
            if (userInput2.equals("2-room")){
                // no need check married people since user can only register if >=21
                if(!currentUser.getMaritialStatus() && currentUser.getAge()<35){ // if is single and younger then 35
                    System.out.println("User is too young.");
                    return null;
                }
                inputFlatType = FlatType.TWO_ROOM;

            }else if(userInput2.equals("3-room")) {
                if(!currentUser.getMaritialStatus()){ // if is single 
                    System.out.println("User can only apply 2-room.");
                    break; // reloop him
                }
                inputFlatType = FlatType.THREE_ROOM;
            } else {
            System.out.println("Invalid flat type. Please enter a valid flat type (2-Room or 3-Room).");
            }
        }

        System.out.println("Your application has been created and submitted successfully!");
        BTOProjectModel selectedProject = appContext.getProjectRepo().getProjectByID(input_projectId);
        return new BTOApplicationModel(currentUser, selectedProject, flatType);
    }

    
    public ApplicationStatus viewApplicationStatus(BTOApplicationModel application){
        return application.getStatus();
    }

    public void updateApplicationStatus(BTOApplicationModel application, ApplicationStatus status){
        application.setStatus(status);    
    }

    public void approveApplication(BTOApplicationModel application){
        updateApplicationStatus(application, ApplicationStatus.SUCCESSFUL);
    }

    public void rejectApplication(BTOApplicationModel application){
        updateApplicationStatus(application, ApplicationStatus.UNSUCCESSFUL);
    }
    public Receipt generateReceipt(BTOApplicationModel application) {
        return new Receipt(application);
    }
}
