package com.sc2002.controller;

import java.util.List;
import java.util.Scanner;

import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.utilities.Receipt;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.UserRole;
import com.sc2002.model.User;

public class ApplicationService {
    //
    // PS: ruba pls refer to my code, think i have accidentally did ur part :,) - rayson
    //
   public void viewAvailableProjectsForApplicant(AppContext appContext) {
        ApplicantModel applicant = (ApplicantModel) appContext.getCurrentUser();

        // get the list of available projects from the project repository
        List<BTOProjectModel> allProjects = appContext.getProjectRepo().getAllProjects();
        
        for (BTOProjectModel project : allProjects) {
            if (isProjectVisibleForApplicant(applicant, project)) {
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

        // criteria for married applicants 21 years old and above can apply for both 2-Room or 3-Room
        if (applicant.getMaritalStatus().equals("MARRIED") && applicant.getAge() >= 21) {
            if (project.getFlatType() != FlatType.TWO_ROOM && project.getFlatType() != FlatType.THREE_ROOM) {
                return false;
            }
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

        System.out.println("Your application has been created and submitted successfully!");
        return new BTOApplicationModel(currentUser.getNRIC(), currentUser.getUserID(), input_projectId);
    }

    
    public ApplicationStatus viewApplicationStatus(BTOApplication application){
        return application.getStatus();
    }

    public void updateApplicationStatus(BTOApplication application, ApplicationStatus status){
        application.setStatus(status);    
    }

    public void approveApplication(BTOApplication application){
        updateApplicationStatus(application, ApplicationStatus.SUCCESSFUL);
    }

    public void rejectApplication(BTOApplication application){
        updateApplicationStatus(application, ApplicationStatus.UNSUCCESSFUL);
    }
    public Receipt generateReceipt(BTOApplicationModel application) {
        return new Receipt(application);
    }
}
