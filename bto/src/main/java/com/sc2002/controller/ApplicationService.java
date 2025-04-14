package com.sc2002.controller;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.User;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.utilities.Receipt;

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
                        return false; // dont let him see it if he applied but withdrawn/unsuccessful and not visible
                    }else{
                        return true; // let him see it if he applied and is not visible
                    }
                }
            }
            return false;
        }

        // criteria for single 35 years old and above can only apply for 2-Room flats, hence we only allow viewing if
        // tworoom count > 0,
        if (!currentUser.getMaritalStatus() && currentUser.getAge() >= 35) {
            if (project.getTwoRoomCount() <=0) {
                return false;
            }
            return true;
        }
        // For married individuals check age
        if(currentUser.getMaritalStatus()&&currentUser.getAge()>21){ // this will remove married under 21, technically not needed since we only allow 21 above to register
            if (project.getTwoRoomCount() <= 0 && project.getThreeRoomCount() <=0) {
                return false; // if no more rooms to book
            }
            // married and room avail, return true
            return true;
        }

        return false;
    }

    public Boolean applyToProject(ProjectRepo projectRepo, Scanner scanner, User currentUser) {
        // Check role
        if(currentUser.getUsersRole() != UserRole.APPLICANT && currentUser.getUsersRole() != UserRole.HDB_OFFICER){
            System.out.println("You do not have permission to apply an application to join a project.");
            return false;
        }
        ApplicantModel applicant = (ApplicantModel) currentUser;
        int input_projectId = 0;

        while (true) { 
            System.out.printf("Enter Project ID (Input -1 to return back to menu): ");
            if (scanner.hasNextInt()) {
                List<BTOProjectModel> managedProjects = appContext.getProjectRepo().getProjectsByOfficerID(appContext.getCurrentUser());
                input_projectId = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                // Return back to menu
                if (input_projectId == -1) {
                    return false;
                }

                // Condition that officer cannot apply a BTO project that he is managing
                if (!managedProjects.isEmpty()) {
                    if (managedProjects.get(0).getProjectID() == input_projectId) {
                        System.out.println("You cannot apply to your own project.");
                        return false;
                    }
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
            System.out.printf("Enter Flat Type (2-Room or 3-Room) (Input -1 to return back to menu): ");
            userInput2 = scanner.nextLine().trim().toLowerCase();
            
            // Validate input
            if (userInput2.equals("2-room")){
                // no need check married people since user can only register if >=21
                if(!currentUser.getMaritalStatus() && currentUser.getAge()<35){ // if is single and younger then 35
                    System.out.println(currentUser.getMaritalStatus());
                    System.out.println("User is single, can't apply for any projects.");
                    return false;
                }
                if(projectRepo.getProjectByID(input_projectId).getTwoRoomCount()<=0){
                    System.out.println("No more 2-room.");
                    continue;
                }else{ // if there is 2-room avail then allow him to book.
                    inputFlatType = FlatType.TWO_ROOM;
                    break;
                }
            }else if(userInput2.equals("3-room")) {
                if(!currentUser.getMaritalStatus()){ // if is single 
                    System.out.println("User can only apply 2-room.");
                    continue; // reloop him
                }
                if(projectRepo.getProjectByID(input_projectId).getThreeRoomCount()<=0){
                    System.out.println("No more 3-room.");
                    continue;
                }else{ // if there is 3-room avail then allow him to book.
                    inputFlatType = FlatType.THREE_ROOM;
                    break;
                }
            } else if(userInput2.equals("-1")){
                return false;
            }else {
            System.out.println("Invalid flat type. Please enter a valid flat type (\"2-Room\" or \"3-Room\" or \"-1\" to exit).");
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

    public boolean approveApplicantWithdrawalApplication(BTOApplicationModel application){
        try {
            User currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().getProjectByID(application.getProjectID());
            if (currentUser.getUserID() != project.getManagerUserID()) { // If manager of project
                if (!project.isManagingOfficer(currentUser)) {
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            // Handle adding back the room to total count
            if (application.getFlatType() == FlatType.TWO_ROOM) {
                project.setTwoRoomCount(project.getTwoRoomCount() + 1);
            } else if (application.getFlatType() == FlatType.THREE_ROOM) {
                project.setThreeRoomCount(project.getThreeRoomCount() + 1);
            } else {
                throw new RuntimeException("RoomType not implemented.");
            }

            // Change statuses and variables to withdrawn
            application.setStatus(ApplicationStatus.WITHDRAWN);
            application.setWithdrawalRequested(false);
            application.clearBookedUnit();
            return true;
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean rejectApplicantWithdrawalApplication(BTOApplicationModel application){
        try {
            User currentUser = appContext.getCurrentUser();
            BTOProjectModel project = appContext.getProjectRepo().getProjectByID(application.getProjectID());
            if (currentUser.getUserID() != project.getManagerUserID()) { // If manager of project
                if (!project.isManagingOfficer(currentUser)) {
                    throw new RuntimeException("User is not authorized to perform this action.");
                }
            }
            // Change status to withdrawn
            application.setWithdrawalRequested(false);
            return true;
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public boolean updateApplicationStatusToBooked(int applicationID) {
        // Fetch the application by its ID
        Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findByApplicationID(applicationID);
    
        if (applicationOpt.isPresent()) {
            BTOApplicationModel application = applicationOpt.get();
    
            // Check if the current status is 'SUCCESSFUL'
            if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
                // Update the status to 'BOOKED'
                application.setStatus(ApplicationStatus.BOOKED);
                appContext.getApplicationRepo().save(application);
                return true;
            }
        }
        return false; // Return false if the application is not found or the status is not 'SUCCESSFUL'
    }

    public boolean updateApplicationFlatType(int applicationID, FlatType newFlatType, List<FlatType> availableFlatTypes) {
        // Fetch the application by its ID
        Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findByApplicationID(applicationID);

        // Validate if the flat type is available
        if (!availableFlatTypes.contains(newFlatType)) {
            System.out.println("The selected flat type is not available. Please try again.");
            return false;
        }
    
        if (applicationOpt.isPresent()) {
            BTOApplicationModel application = applicationOpt.get();
            BTOProjectModel project = appContext.getProjectRepo().getProjectByID(application.getProjectID());

            if (newFlatType == FlatType.TWO_ROOM) {
                System.out.println("old 2 room count: " + project.getTwoRoomCount());
                System.out.println("old 3 room count: " + project.getThreeRoomCount());
                project.setTwoRoomCount(project.getTwoRoomCount() - 1);
                project.setThreeRoomCount(project.getThreeRoomCount() + 1);
                System.out.println("\nNew 2 room count: " + project.getTwoRoomCount());
                System.out.println("New 3 room count: " + project.getThreeRoomCount());
            }
            else if (newFlatType == FlatType.THREE_ROOM) {
                System.out.println("old 2 room count: " + project.getTwoRoomCount());
                System.out.println("old 3 room count: " + project.getThreeRoomCount());
                project.setTwoRoomCount(project.getTwoRoomCount() + 1);
                project.setThreeRoomCount(project.getThreeRoomCount() - 1);
                System.out.println("\nNew 2 room count: " + project.getTwoRoomCount());
                System.out.println("New 3 room count: " + project.getThreeRoomCount());
            }

            // Update the flat type
            application.setFlatType(newFlatType);
            appContext.getApplicationRepo().save(application);
            return true;
        }

        // If the application is not found, print an error message
        System.out.println("Failed to update flat type. Please check the Application ID.");
        return false;
    }

    public boolean viewApplicationByNRIC(int projectID, String nric, BTOApplicationModel[] outputApplication) {
        Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findByNRIC(nric);
    
        if (applicationOpt.isEmpty()) {
            return false; // No application found for the provided NRIC
        }
    
        BTOApplicationModel application = applicationOpt.get();
        if (application.getProjectID() != projectID) {
            return false; // Officer is not authorized to view applications for this project
        }
    
        // Store the application in the output parameter
        outputApplication[0] = application;
        return true; // Application found and authorized
    }
}
