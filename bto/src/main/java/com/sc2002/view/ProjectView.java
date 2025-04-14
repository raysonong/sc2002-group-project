package com.sc2002.view;

import java.util.List;
import java.util.Map;

import com.sc2002.controller.AppContext;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.Neighborhood;

// Primary to view Projects with filterable options
public class ProjectView {
    public void viewProjectFilterableMenu(AppContext appContext){
        System.out.println("BTO Project View Options:");
        System.out.println("1. Change filters");
        System.out.println("2. View projects");
        System.out.print("Enter your choice: ");

        String input = appContext.getScanner().nextLine();

        switch (input) {
            case "1":
                setFilterMenu(appContext);
                break;
            case "2":
                printProjectMenu(appContext);
                break;
            default:
                // Return without doing anything
                break;
        }
    }
    private void printProjectMenu(AppContext appContext){
        List<BTOProjectModel> listOfProjects = appContext.getProjectRepo().findByFilter(appContext);
        System.out.println("\n-- All BTO Projects --");
        System.out.printf("%-10s %-20s %-15s %-15s %-15s%n", 
            "Project ID", "Project Name", "2-Room Count", "3-Room Count", "Location");
        System.out.println("---------------------------------------------------------------------------------");
        for (BTOProjectModel project : listOfProjects) {
            System.out.printf("%-10d %-20s %-15d %-15d %-15s%n", 
            project.getProjectID(), 
            project.getProjectName(),
            project.getTwoRoomCount(),
            project.getThreeRoomCount(),
            project.getNeighborhood());
        }
        System.out.println("\nPress Enter to continue...");
        appContext.getScanner().nextLine();
    }

    private void setFilterMenu(AppContext appContext){
        // Prompt user to select Neighborhood filter
        System.out.println("-- Select Neighbourhood Filter --");
        System.out.println("0. Reset Neighbourhood filter");

        // Display all Neighborhood from the enum
        Neighborhood[] neighborhoods = Neighborhood.values();
        for (int i = 0; i < neighborhoods.length; i++) {
            System.out.println((i + 1) + ". " + neighborhoods[i]);
        }

        System.out.print("Enter your choice: ");
        String neighborhoodChoice = appContext.getScanner().nextLine();

        try {
            int choice = Integer.parseInt(neighborhoodChoice);
            if (choice == 0) {
                // Reset location filter
                appContext.getCurrentUser().getProjectViewFilter().setNeighborhood(null);
                System.out.println("Neighborhood filter reset.");
            } else if (choice >= 1 && choice <= neighborhoods.length) {
                // Set location filter to selected enum value
                appContext.getCurrentUser().getProjectViewFilter().setNeighborhood(neighborhoods[choice - 1]);
                System.out.println("Neighborhood filter set to: " + neighborhoods[choice - 1]);
            } else {
                System.out.println("Invalid choice. Neighborhood filter unchanged.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Neighborhood filter unchanged.");
        }

        // Now we prompt for flat type filter
        System.out.println("\n-- Select Flat Type Filter --");
        System.out.println("0. Reset flat type filter");
        // Display all Room type from the enum
        FlatType[] flatTypes = FlatType.values();
        for (int i = 0; i < flatTypes.length; i++) {
            System.out.println((i + 1) + ". " + flatTypes[i]);
        }

        System.out.print("Enter your choice: ");
        String flatTypeChoice = appContext.getScanner().nextLine();

        try {
            int choice = Integer.parseInt(flatTypeChoice);
            if (choice == 0) {
                // Reset flat type filter
                appContext.getCurrentUser().getProjectViewFilter().setFlatType(null);
                System.out.println("Flat type filter reset.");
            } else if (choice >= 1 && choice <= flatTypes.length) {
                appContext.getCurrentUser().getProjectViewFilter().setFlatType(flatTypes[choice - 1]);
                System.out.println("Flat type filter set to: " + flatTypes[choice-1]);
            } else {
                System.out.println("Invalid choice. Flat type filter unchanged.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Flat type filter unchanged.");
        }
        printProjectMenu(appContext); // Print before leaving
    }
}
