package com.sc2002.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.HDBManagerModel;

public class ProjectManagementService {

    /**
     * Creates a new BTO project with user input.
     *
     * @param newProjectID The unique ID for the new project.
     * @param scanner The Scanner object for user input.
     * @return A new BTOProjectModel object with the specified details.
     */
    public BTOProjectModel createProject(AppContext appContext) {
        if (!appContext.getAuthService().isManager(appContext.getCurrentUser())) {
            System.out.println("You do not have permission to create a project.");
            return null;
        }

        String projectName, neighborhood;
        int twoRoomCount = 0,twoRoomPrice = 0,threeRoomCount = 0,threeRoomPrice = 0, maxOfficer = 0;
        LocalDate openingDate = null, closingDate = null;
        String tempDate;
        boolean isValidDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Validate project name
        do {
            System.out.printf("Enter the Project Name: ");
            projectName = appContext.getScanner().nextLine().trim();
            if (projectName.isEmpty()) {
                System.out.println("Project Name cannot be empty. Please try again.");
            }
        } while (projectName.isEmpty());

        // Validate neighborhood
        do {
            System.out.printf("Enter the Neighborhood (e.g. Yishun, Boon Lay, etc.): ");
            neighborhood = appContext.getScanner().nextLine().trim();
            if (neighborhood.isEmpty()) {
                System.out.println("Neighborhood cannot be empty. Please try again.");
            }
        } while (neighborhood.isEmpty());

        // Validate two-room flat count
        do {
            System.out.printf("Enter the numbers of 2-room flats: ");
            if (appContext.getScanner().hasNextInt()) {
                twoRoomCount = appContext.getScanner().nextInt();
                appContext.getScanner().nextLine(); // Consume the leftover newline
                if (twoRoomCount < 0) {
                    System.out.println("The number of 2-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (twoRoomCount < 0);
        // Validate two-room flat price
        do {
            System.out.printf("Enter the price of 2-room flats: ");
            if (appContext.getScanner().hasNextInt()) {
            twoRoomPrice = appContext.getScanner().nextInt();
            appContext.getScanner().nextLine(); // Consume the leftover newline
            if (twoRoomPrice < 0) {
                System.out.println("The price of 2-room flats cannot be negative. Please try again.");
            }
            } else {
            System.out.println("Invalid input. Please enter a valid integer.");
            appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (twoRoomPrice < 0);
        // Validate three-room flat count
        do {
            System.out.printf("Enter the numbers of 3-room flats: ");
            if (appContext.getScanner().hasNextInt()) {
                threeRoomCount = appContext.getScanner().nextInt();
                appContext.getScanner().nextLine(); // Consume the leftover newline
                if (threeRoomCount < 0) {
                    System.out.println("The number of 3-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (threeRoomCount < 0);
        // Validate three-room flat price
        do {
            System.out.printf("Enter the price of 3-room flats: ");
            if (appContext.getScanner().hasNextInt()) {
                threeRoomPrice = appContext.getScanner().nextInt();
                appContext.getScanner().nextLine(); // Consume the leftover newline
                if (threeRoomPrice < 0) {
                    System.out.println("The price of 3-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (threeRoomPrice < 0);
        // Validate opening date
        while (!isValidDate) {
            System.out.printf("Enter the application opening date in DD-MM-YYYY format (e.g. 31-12-2025): ");
            tempDate = appContext.getScanner().nextLine();
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
            tempDate = appContext.getScanner().nextLine();
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

        // Validate max Officer
        do {
            System.out.printf("Enter the maximum amount of HDB Officer Slots (max 10): ");
            if (appContext.getScanner().hasNextInt()) {
                maxOfficer = appContext.getScanner().nextInt();
                appContext.getScanner().nextLine(); // Consume the leftover newline
                if (maxOfficer < 0) {
                    System.out.println("The number of maximum HDB Officer Slots cannot be negative. Please try again.");
                } else if (maxOfficer > 10) {
                    System.out.println("The number of maximum HDB Officer Slots cannot be more than 10. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (maxOfficer < 0 || maxOfficer > 10);

        return new BTOProjectModel(projectName, neighborhood, twoRoomCount,twoRoomPrice, threeRoomCount,threeRoomPrice, openingDate, closingDate, maxOfficer, appContext.getCurrentUser().getUserID());
    }

    public void editProject(AppContext appContext, String userOption, String valueToChange) {
        try {
            if (appContext.getAuthService().isManager(appContext.getCurrentUser())) {
                int projectID = ((HDBManagerModel) appContext.getCurrentUser()).getProjectID();
                BTOProjectModel project = appContext.getProjectRepo().getProjectByID(projectID);
                if (project == null) {
                    throw new RuntimeException("Current User has no project under it.");
                }
                switch (userOption) {
                    case "1" ->
                        project.setProjectName(valueToChange);
                    case "2" ->
                        project.setNeighborhood(valueToChange);
                    case "3" -> {
                        try {
                            int twoRoomCount = Integer.parseInt(valueToChange);
                            if (twoRoomCount < 0) {
                                throw new IllegalArgumentException("2 Room Count cannot be negative.");
                            }
                            project.setTwoRoomCount(twoRoomCount);
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid input for 2 Room Count. Please enter a valid integer.");
                        }
                    }
                    case "4" -> {
                        try {
                            int threeRoomCount = Integer.parseInt(valueToChange);
                            if (threeRoomCount < 0) {
                                throw new IllegalArgumentException("3 Room Count cannot be negative.");
                            }
                            project.setThreeRoomCount(threeRoomCount);
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid input for 3 Room Count. Please enter a valid integer.");
                        }
                    }
                    case "5" -> {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            LocalDate openingDate = LocalDate.parse(valueToChange, formatter);
                            project.setOpeningDate(openingDate);
                        } catch (DateTimeParseException e) {
                            throw new RuntimeException("Invalid date format for Opening Date. Please use DD-MM-YYYY.");
                        }
                    }
                    case "6" -> {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            LocalDate closingDate = LocalDate.parse(valueToChange, formatter);
                            if (!closingDate.isAfter(project.getOpeningDate())) {
                                throw new IllegalArgumentException("Closing Date must be later than the Opening Date.");
                            }
                            project.setClosingDate(closingDate);
                        } catch (DateTimeParseException e) {
                            throw new RuntimeException("Invalid date format for Closing Date. Please use DD-MM-YYYY.");
                        }
                    }
                    default ->
                        throw new RuntimeException("Invalid option selected!");
                }
            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public boolean deleteProject(AppContext appContext) {
        try {
            if (appContext.getAuthService().isManager(appContext.getCurrentUser())) {
                HDBManagerModel currentUser = (HDBManagerModel) appContext.getCurrentUser();
                BTOProjectModel project = appContext.getProjectRepo().getProjectByID(currentUser.getProjectID());
                if (project.getManagerUserID() == currentUser.getUserID()) {
                    currentUser.deleteProjectID(); // if deleting currently managing project

                                }if (appContext.getProjectRepo().deleteByProjectID(currentUser.getProjectID())) {
                    return true; 
                }else {
                    throw new RuntimeException("Failed to delete project.");
                }
            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return false;
    }

    public void toggleProjectVisibility(AppContext appContext, Integer projectID) {
        try {
            if (appContext.getAuthService().isManager(appContext.getCurrentUser())) {
                BTOProjectModel project = appContext.getProjectRepo().getProjectByID(projectID);
                if (project == null) {
                    throw new RuntimeException("Project with the given ID does not exist.");
                }
                if(project.getManagerUserID()!=appContext.getCurrentUser().getUserID()) throw new RuntimeException("User does not own this project.");
                boolean currentVisibility = project.isVisible();
                System.out.println("Current visibility: " + (currentVisibility ? "Visible" : "Hidden"));
                project.setVisible(!currentVisibility);
                System.out.println("Visibility toggled to: " + (project.isVisible() ? "Visible" : "Hidden"));
            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
