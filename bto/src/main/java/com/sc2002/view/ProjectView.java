package com.sc2002.view;

import java.util.List;

import com.sc2002.enums.FlatType;
import com.sc2002.enums.Neighborhood;

import com.sc2002.model.BTOProjectModel;

import com.sc2002.config.AppContext;

import com.sc2002.controllers.ProjectController;

/**
 * Provides views related to BTO projects, primarily focusing on displaying
 * projects with filtering capabilities and showing the project currently managed by a user.
 */
public class ProjectView {

    /**
     * Default constructor for ProjectView.
     * Initializes controllers used for project viewing and filtering.
     */
    public ProjectView() {
        // Default constructor
    }

    /**
     * Displays the main menu for viewing projects, allowing users to change filters
     * or view projects based on current filters. Managers have an additional option
     * to view only the projects they manage.
     *
     * @param appContext The application context containing shared resources and state.
     */
    public void viewProjectFilterableMenu(AppContext appContext) {
        System.out.println("BTO Project View Options:");
        System.out.println("1. Change filters");
        System.out.println("2. View projects (filtered)");
        // Only show option 3 if the user is a manager
        if (appContext.getAuthController().isManager(appContext.getCurrentUser())) {
            System.out.println("3. View Your Projects (filtered)");
        }
        System.out.print("Enter your choice: ");

        String input = appContext.getScanner().nextLine();

        switch (input) {
            case "1":
                setFilterMenu(appContext);
                break;
            case "2":
                printProjectMenu(appContext);
                break;
            case "3":
                if (appContext.getAuthController().isManager(appContext.getCurrentUser())) {
                    printPersonalProjectMenu(appContext);
                }
                // if not manager, return without doing anything
                break;
            default:
                // Return without doing anything
                break;
        }
    }

    /**
     * Displays information about the BTO project currently being managed by the logged-in
     * HDB Officer or Manager.
     *
     * @param appContext The application context.
     */
    public void projectManagingMenu(AppContext appContext) { // handling printing of which project officer is currently handling
        ProjectController projectController = new ProjectController(appContext);
        BTOProjectModel project = projectController.viewManagingProject();
        if (project != null) {
            System.out.printf("%s INFO: Currently Managing \"%s\" Project ID: %d\n", appContext.getCurrentUser().getUsersRole(), project.getProjectName(), project.getProjectID());
        } else {
            System.out.println("INFO: Currently not managing Project");
        }
    }

    /**
     * Prints a list of all BTO projects that match the filters currently set
     * in the user's profile within the AppContext.
     *
     * @param appContext The application context.
     */
    private void printProjectMenu(AppContext appContext) {
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

    /**
     * Prints a list of BTO projects managed by the current user (Manager)
     * that match the filters currently set in the user's profile within the AppContext.
     *
     * @param appContext The application context.
     */
    private void printPersonalProjectMenu(AppContext appContext){
        List<BTOProjectModel> listOfProjects = appContext.getProjectRepo().findPersonalByFilter(appContext);
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

    /**
     * Handles the menu flow for setting or resetting the Neighborhood and Flat Type filters
     * used for viewing projects. Updates the filter settings in the user's profile.
     *
     * @param appContext The application context.
     */
    private void setFilterMenu(AppContext appContext) {
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
                System.out.println("Flat type filter set to: " + flatTypes[choice - 1]);
            } else {
                System.out.println("Invalid choice. Flat type filter unchanged.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Flat type filter unchanged.");
        }
        printProjectMenu(appContext); // Print before leaving
    }
}
