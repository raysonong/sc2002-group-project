package com.sc2002.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.sc2002.enums.Neighborhood;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.HDBManagerModel;

public class ProjectManagementService {

    private AppContext appContext;

    public ProjectManagementService(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Creates a new BTO project with user input.
     *
     * @param newProjectID The unique ID for the new project.
     * @param scanner The Scanner object for user input.
     * @return A new BTOProjectModel object with the specified details.
     */
    public BTOProjectModel createProject() {
        // Check if have the right permission
        if (!this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())) {
            System.out.println("You do not have permission to create a project.");
            return null;
        }

        // check if currently are managing other project
        for (BTOProjectModel btoProjectModel : appContext.getProjectRepo().findAll()) {
            if (btoProjectModel.getManagerUserID() == appContext.getCurrentUser().getUserID()) {
                if (btoProjectModel.getClosingDate().isAfter(LocalDate.now())) {
                    System.out.println("You are currently managing another project!");
                    System.out.println("Current Project Name: " + btoProjectModel.getProjectName());
                    System.out.println("Closing Date: " + btoProjectModel.getClosingDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
                    System.out.println("Today's Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
                    System.out.println("------------------------------\nPress enter to continue...");
                    appContext.getScanner().nextLine();
                    return null;
                }
            }
        }

        String projectName, neighborhoodChoice;
        Neighborhood neighborhoodEnum=null;
        int twoRoomCount = 0, twoRoomPrice = 0, threeRoomCount = 0, threeRoomPrice = 0, maxOfficer = 0;
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
            System.out.println("-- Select Neighborhood --");
            Neighborhood[] neighborhoods = Neighborhood.values();
            for (int i = 0; i < neighborhoods.length; i++) {
                System.out.println((i + 1) + ". " + neighborhoods[i]);
            }
            System.out.printf("Select Neighborhood: ");
            neighborhoodChoice = this.appContext.getScanner().nextLine().trim();
            try {
                int choice = Integer.parseInt(neighborhoodChoice);
                if (choice >= 1 && choice <= neighborhoods.length) {
                    // Set location filter to selected enum value
                    neighborhoodEnum=neighborhoods[choice - 1];
                    System.out.println("Neighborhood choice: " + neighborhoods[choice - 1]);
                } else {
                    System.out.println("Invalid Neighborhood choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        } while (neighborhoodEnum==null);

        // Validate two-room flat count
        do {
            System.out.printf("Enter the numbers of 2-room flats: ");
            if (this.appContext.getScanner().hasNextInt()) {
                twoRoomCount = this.appContext.getScanner().nextInt();
                appContext.getScanner().nextLine(); // Consume the leftover newline
                if (twoRoomCount < 0) {
                    System.out.println("The number of 2-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                this.appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (twoRoomCount < 0);
        // Validate two-room flat price
        do {
            System.out.printf("Enter the price of 2-room flats: ");
            if (this.appContext.getScanner().hasNextInt()) {
                twoRoomPrice = this.appContext.getScanner().nextInt();
                this.appContext.getScanner().nextLine(); // Consume the leftover newline
                if (twoRoomPrice < 0) {
                    System.out.println("The price of 2-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                this.appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (twoRoomPrice < 0);
        // Validate three-room flat count
        do {
            System.out.printf("Enter the numbers of 3-room flats: ");
            if (this.appContext.getScanner().hasNextInt()) {
                threeRoomCount = this.appContext.getScanner().nextInt();
                this.appContext.getScanner().nextLine(); // Consume the leftover newline
                if (threeRoomCount < 0) {
                    System.out.println("The number of 3-room flats cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                this.appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (threeRoomCount < 0);
        // Validate three-room flat price
        do {
            System.out.printf("Enter the price of 3-room flats: ");
            if (this.appContext.getScanner().hasNextInt()) {
                threeRoomPrice = this.appContext.getScanner().nextInt();
                this.appContext.getScanner().nextLine(); // Consume the leftover newline
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
            tempDate = this.appContext.getScanner().nextLine();
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
            tempDate = this.appContext.getScanner().nextLine();
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
            if (this.appContext.getScanner().hasNextInt()) {
                maxOfficer = this.appContext.getScanner().nextInt();
                this.appContext.getScanner().nextLine(); // Consume the leftover newline
                if (maxOfficer < 0) {
                    System.out.println("The number of maximum HDB Officer Slots cannot be negative. Please try again.");
                } else if (maxOfficer > 10) {
                    System.out.println("The number of maximum HDB Officer Slots cannot be more than 10. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                this.appContext.getScanner().nextLine(); // Consume invalid input
            }
        } while (maxOfficer < 0 || maxOfficer > 10);
        
        return new BTOProjectModel(projectName, neighborhoodEnum, twoRoomCount, twoRoomPrice, threeRoomCount, threeRoomPrice, openingDate, closingDate, maxOfficer, this.appContext.getCurrentUser().getUserID());
    }

    public void editProject(String userOption, String valueToChange) {
        try {
            if (this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())) {
                int projectID = ((HDBManagerModel) this.appContext.getCurrentUser()).getProjectID();
                BTOProjectModel project = this.appContext.getProjectRepo().findByID(projectID);
                if (project == null) {
                    throw new RuntimeException("Current User has no project under it.");
                }
                switch (userOption) {
                    case "1" ->
                        project.setProjectName(valueToChange);
                    case "2" ->{
                        // Display all Neighborhood from the enum
                        try{
                            int choice = Integer.parseInt(valueToChange);
                            Neighborhood[] neighborhoods = Neighborhood.values();
                            project.setNeighborhood(neighborhoods[choice-1]);
                            System.out.printf("Neighborhood changed to: %s\n",neighborhoods[choice-1]);
                        }catch(NumberFormatException e){
                            throw new IllegalArgumentException("Invalid number.");
                        }
                    }case "3" -> {
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

    public boolean deleteProject() {
        try {
            if (this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())) {
                HDBManagerModel currentUser = (HDBManagerModel) this.appContext.getCurrentUser();
                BTOProjectModel project = this.appContext.getProjectRepo().findByID(currentUser.getProjectID());
                if (project.getManagerUserID() == currentUser.getUserID()) {
                    currentUser.deleteProjectID(); // if deleting currently managing project

                }
                if (this.appContext.getProjectRepo().delete(currentUser.getProjectID())) {
                    return true;
                } else {
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

    public void toggleProjectVisibility(Integer projectID) {
        try {
            if (this.appContext.getAuthService().isManager(this.appContext.getCurrentUser())) {
                BTOProjectModel project = this.appContext.getProjectRepo().findByID(projectID);
                if (project == null) {
                    throw new RuntimeException("Project with the given ID does not exist.");
                }
                if (project.getManagerUserID() != this.appContext.getCurrentUser().getUserID()) {
                    throw new RuntimeException("User does not own this project.");
                }
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
