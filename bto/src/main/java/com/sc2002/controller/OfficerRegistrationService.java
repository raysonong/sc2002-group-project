package com.sc2002.controller;

import java.util.Scanner;

import com.sc2002.enums.UserRole;
import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.model.User;
import com.sc2002.repositories.ProjectRepo;

public class OfficerRegistrationService {
    public OfficerRegistrationModel registerForProject(ProjectRepo projectRepo, Scanner scanner, User currentUser) {
        // Check role
        if(currentUser.getUsersRole() != UserRole.HDB_OFFICER) {
            System.out.println("You do not have permission to apply an application to join a project.");
            return null;
        }

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
                if (projectRepo.findByProjectID(input_projectId)==null) {
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

        System.out.println("Your application has been created successfully and is pending approval from the project manager!");
        return new OfficerRegistrationModel(currentUser.getNRIC(), currentUser.getUserID(), input_projectId);
    }
}
