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

    private boolean isProjectVisibleForApplicant(User currentUser, BTOProjectModel project) {
        if (!project.isVisible()) {
            List<BTOApplicationModel> listOfUsersApplication=appContext.getApplicationRepo().findApplicationByApplicantID(currentUser.getUserID());
            for (BTOApplicationModel application : listOfUsersApplication) {
                if (application.getProjectID() == project.getProjectID()) { // If the user applied for the project before
                    if (application.getStatus() == ApplicationStatus.WITHDRAWN || application.getStatus() == ApplicationStatus.UNSUCCESSFUL) { // If the user was already rejected or unsuccessful
                        return false; // dont let him see it
                    }
                    // if its in progress then just continue
                }else{ // If user did not apply for project before & project is not visible
                    return false;
                }
            }
            
        }

        // criteria for single 35 years old and above can only apply for 2-Room flats, hence we only allow viewing if
        // tworoom count > 0,
        if (!currentUser.getMaritalStatus() && currentUser.getAge() >= 35) {
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

    public Boolean applyToProject(ProjectRepo projectRepo, Scanner scanner, User currentUser) {
        // Check role
        if(currentUser.getUsersRole() != UserRole.APPLICANT) {
            System.out.println("You do not have permission to apply an application to join a project.");
            return false;
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
                    return false;
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
                if(!currentUser.getMaritalStatus() && currentUser.getAge()<35){ // if is single and younger then 35
                    System.out.println(currentUser.getMaritalStatus());
                    System.out.println("WTF?");
                    return false;
                }
                inputFlatType = FlatType.TWO_ROOM;
                break;

            }else if(userInput2.equals("3-room")) {
                if(!currentUser.getMaritalStatus()){ // if is single 
                    System.out.println("User can only apply 2-room.");
                    continue; // reloop him
                }
                inputFlatType = FlatType.THREE_ROOM;
                break;
            } else {
            System.out.println("Invalid flat type. Please enter a valid flat type (2-Room or 3-Room).");
            }
        }
        BTOProjectModel selectedProject = appContext.getProjectRepo().getProjectByID(input_projectId);
        this.appContext.getApplicationRepo().save(new BTOApplicationModel(currentUser, selectedProject, inputFlatType));
        return true;
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
    public Boolean approveApplicantApplication(BTOApplicationModel application){
        try{ // for both Managers and Officer @Rayson
            // Do initial authentication checking to see if currentUser can actually change values
            User currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().getProjectByID(application.getProjectID());
            if(currentUser.getUserID()!=project.getManagerUserID()){// If manager of project
                if(!project.isManagingOfficer(currentUser)){
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            //Able  to  approve  or  reject  Applicant’s  BTO  application  –  approval  is  
            // limited to the supply of the flats (number of units for the respective flat 
            // types)
            // we will immediately deduct from total count once the user's booking is approved.
            if(application.getFlatType()==FlatType.TWO_ROOM){
                if(project.getTwoRoomCount()==0) throw new RuntimeException("Not enough rooms.");// check at least 1
                application.setStatus(ApplicationStatus.SUCCESSFUL);
                project.setTwoRoomCount(project.getTwoRoomCount() - 1);
            }else if(application.getFlatType()==FlatType.THREE_ROOM){
                if(project.getThreeRoomCount()==0) throw new RuntimeException("Not enough rooms.");// check at least 1
                application.setStatus(ApplicationStatus.SUCCESSFUL);
                project.setThreeRoomCount(project.getThreeRoomCount() - 1);
            }else{
                throw new RuntimeException("RoomType not implemented.");
            }
            return true;
        }catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
    public Boolean rejectApplicantApplication(BTOApplicationModel application){
        try{ // for both Managers and Officer @Rayson
            // Do initial authentication checking to see if currentUser can actually change values
            User currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().getProjectByID(application.getProjectID());
            if(currentUser.getUserID()!=project.getManagerUserID()){// If manager of project
                if(!project.isManagingOfficer(currentUser)){
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            // Only need do initial checks, afterwards just change status to unsucessful
            application.setStatus(ApplicationStatus.UNSUCCESSFUL);
            return true;
        }catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
}
