package com.sc2002.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.HDBManagerModel;
import com.sc2002.model.User;
import com.sc2002.enums.UserRole;

public class ProjectManagementService {

    /**
     * Creates a new BTO project with user input.
     *
     * @param newProjectID The unique ID for the new project.
     * @param scanner The Scanner object for user input.
     * @return A new BTOProjectModel object with the specified details.
     */
    public BTOProjectModel createProject(int newProjectID, Scanner scanner,User currentUser) {
        if(currentUser.getUsersRole() != UserRole.HDB_MANAGER) {
            System.out.println("You do not have permission to create a project.");
            return null;
        }
        
        String projectName, neighborhood;
        int twoRoomCount = 0, threeRoomCount = 0, maxOfficer = 0;
        LocalDate openingDate = null, closingDate = null;
        String tempDate;
        boolean isValidDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Validate project name
        do {
            System.out.printf("Enter the Project Name: ");
            projectName = scanner.nextLine().trim();
            if (projectName.isEmpty()) {
                System.out.println("Project Name cannot be empty. Please try again.");
            }
        } while (projectName.isEmpty());

        // Validate neighborhood
        do {
            System.out.printf("Enter the Neighborhood (e.g. Yishun, Boon Lay, etc.): ");
            neighborhood = scanner.nextLine().trim();
            if (neighborhood.isEmpty()) {
                System.out.println("Neighborhood cannot be empty. Please try again.");
            }
        } while (neighborhood.isEmpty());

        // Validate two-room flat count
        do {
            System.out.printf("Enter the numbers of 2-room flats: ");
            if (scanner.hasNextInt()) {
                twoRoomCount = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
                if (twoRoomCount < 0) {
                    System.out.println("The number of 2-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume invalid input
            }
        } while (twoRoomCount < 0);

        // Validate three-room flat count
        do {
            System.out.printf("Enter the numbers of 3-room flats: ");
            if (scanner.hasNextInt()) {
                threeRoomCount = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
                if (threeRoomCount < 0) {
                    System.out.println("The number of 3-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume invalid input
            }
        } while (threeRoomCount < 0);

        // Validate opening date
        while (!isValidDate) {
            System.out.printf("Enter the application opening date in DD-MM-YYYY format (e.g. 31-12-2025): ");
            tempDate = scanner.nextLine();
            try {
                openingDate = LocalDate.parse(tempDate, formatter);
                isValidDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use DD-MM-YYYY (e.g. 31-12-2025)");
            }
        }

        // Validate closing date
        isValidDate = false;
        while (!isValidDate) {
            System.out.printf("Enter the application closing date in DD-MM-YYYY format (e.g. 31-12-2025): ");
            tempDate = scanner.nextLine();
            try {
                closingDate = LocalDate.parse(tempDate, formatter);
                if (!closingDate.isAfter(openingDate)) {
                    System.out.println("Closing date must be later than the opening date. Please try again.");
                } else {
                    isValidDate = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use DD-MM-YYYY (e.g. 31-12-2025)");
            }
        }

        do {
            System.out.printf("Enter the maximum amount of HDB Officer Slots (max 10): ");
            if (scanner.hasNextInt()) {
                maxOfficer = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
                if (maxOfficer < 0) {
                    System.out.println("The number of maximum HDB Officer Slots cannot be negative. Please try again.");
                } else if (maxOfficer > 10) {
                    System.out.println("The number of maximum HDB Officer Slots cannot be more than 10. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume invalid input
            }
        } while (maxOfficer < 0 || maxOfficer > 10);

        return new BTOProjectModel(newProjectID, projectName, neighborhood, twoRoomCount, threeRoomCount, openingDate, closingDate, maxOfficer, currentUser.getUserID());
    }
}