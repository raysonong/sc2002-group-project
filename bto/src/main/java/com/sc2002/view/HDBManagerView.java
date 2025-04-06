package com.sc2002.view;

import java.util.List;
import java.util.Map;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.EnquiryService;
import com.sc2002.controller.ProjectManagementService;
import com.sc2002.controller.ProjectService;
import com.sc2002.model.Enquiry;

public class HDBManagerView {

    // declare all the services required by Manager
    private final ProjectManagementService projectManagementService = new ProjectManagementService();
    private final EnquiryService enquiryService = new EnquiryService();
    private final ProjectService projectService = new ProjectService();

    public void HDBManagerMenu(AppContext appContext) {
        // TODO: Menu for HDB Manager
        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();

        System.out.println("--HDB Manager Menu--");
        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.print("Please select an option: ");
        userInput = appContext.getScanner().nextLine();

        switch (userInput) { // violates s-SRP for (SOLID), could be implemented better later-on
            case "1" -> {
                // Option 1: Create a new BTO project
                appContext.getProjectRepo().save(projectManagementService.createProject(appContext));
            }
            case "2" -> {
                // Option 2: Edit an existing BTO project
                editBTOProjectMenu(appContext);
            }
            case "3" -> {
                // Option 3: Delete an existing BTO project
                deleteBTOProjectMenu(appContext);
            }
            case "4" -> {
                // Option 4: Toggle the visibility of a BTO project
                toggleProjectVisibilityMenu(appContext);
            }
            case "5" -> {
                // Option 5: View all BTO projects
                getAllBTOProjectMenu(appContext);
            }
            case "6" -> {
                // Option 6: View details of a specific BTO project
                getBTOProjectByUserIDMenu(appContext);
            }
            case "7" -> {
                // Option 7: View Enquiries
                getAllEnquiryMenu(appContext);
            }
            case "8" -> {
                // Option 8: Reply Enquiries

            }
            case "9" -> {
                // Option 9: Approve officer registration

            }
            case "10" -> {
                // Option 10: Reject officer registration

            }
            case "11" -> {
                // Option 11: Approve an application

            }
            case "12" -> {
                // Option 12: Reject an application

            }
            case "13" -> {
                // Option 13: Approve a withdrawal request

            }
            case "14" -> {
                // Option 14: Reject a withdrawal request

            }
            case "15" -> {
                // Option 15: Generate reports

            }
            case "16" -> {
                // Option 16: Logout
                System.out.println("Logging out...");
                appContext.setCurrentUser(null); // set the CurrentUser null
            }
            default -> {
                // Invalid option selected
                System.out.println("Please select a valid option!");
            }
        }
    }// End of HDBManagerMenu

    private void editBTOProjectMenu(AppContext appContext) {
        System.out.println("--editBTOProjectMenu--\n(1) Project Name\n(2) Neighborhood\n(3) 2 Room Count\n(4) 3 Room Count\n(5) Opening Date\n(6) Closing Date\nPlease select an option: ");
        String userOption = appContext.getScanner().nextLine();
        switch (userOption) {
            case "1" -> {
                // Project Name
                System.out.println("Enter new Project Name: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(appContext, userOption, valueToChange);
            }
            case "2" -> {
                // Neighborhood
                System.out.println("Enter new Neighborhood Name: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(appContext, userOption, valueToChange);
            }
            case "3" -> {
                // 2 Room Count
                System.out.println("Enter new 2 Room Count: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(appContext, userOption, valueToChange);
            }
            case "4" -> {
                // 3 Room Count
                System.out.println("Enter new 3 Room Count: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(appContext, userOption, valueToChange);
            }
            case "5" -> {
                // Opening Date
                System.out.println("Enter new Opening Date in DD-MM-YYYY format (e.g. 31-12-2025): ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(appContext, userOption, valueToChange);
            }
            case "6" -> {
                // Closing Date
                System.out.println("Enter new Closing Date in DD-MM-YYYY format (e.g. 31-12-2025): ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(appContext, userOption, valueToChange);
            }
            default -> {
                System.out.println("Invalid option selected!");
            }
        }
    }

    private void deleteBTOProjectMenu(AppContext appContext) {
        System.out.println("--deleteBTOProjectMenu--\n(yes) To Confirm Deletion: ");
        String userOption = appContext.getScanner().nextLine().toLowerCase();
        switch (userOption) {
            case "yes" -> {
                // Confirm Deletion
                if (projectManagementService.deleteProject(appContext)) {
                    System.out.println("Deletion Successful.");
                }
            }
            default -> {
                System.out.println("Deletion process cancelled.");
            }
        }
    }

    private void toggleProjectVisibilityMenu(AppContext appContext) {
        Map<Integer, String> listOfProjects = appContext.getProjectRepo().getAllProject();
        System.out.println("-- All BTO Projects --");
        System.out.println("Index\tProject ID\tProject Name");
        System.out.println("-----------------------------------");
        int index = 1;
        for (Map.Entry<Integer, String> entry : listOfProjects.entrySet()) {
            System.out.printf("%d\t%d\t\t%s%n", index++, entry.getKey(), entry.getValue());
        }
        System.out.print("Enter the Project ID to toggle visiblity: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (listOfProjects.containsKey(projectID)) {
                projectManagementService.toggleProjectVisibility(appContext, projectID);
            } else {
                System.out.println("Invalid Project ID. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
        }
        projectManagementService.toggleProjectVisibility(appContext, null);
    }

    private void getAllBTOProjectMenu(AppContext appContext) {
        Map<Integer, String> listOfProjects = appContext.getProjectRepo().getAllProject();
        System.out.println("-- All BTO Projects --");
        System.out.println("Index\tProject ID\tProject Name");
        System.out.println("-----------------------------------");
        int index = 1;
        for (Map.Entry<Integer, String> entry : listOfProjects.entrySet()) {
            System.out.printf("%d\t%d\t\t%s%n", index++, entry.getKey(), entry.getValue());
        }
        System.out.print("Enter the Project ID to view details: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (listOfProjects.containsKey(projectID)) {
                projectService.viewProjectByID(appContext, projectID);
            } else {
                System.out.println("Invalid Project ID. Please try again.");
            }
            System.out.print("Press enter to continue...");
            appContext.getScanner().nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
        }
    }

    private void getBTOProjectByUserIDMenu(AppContext appContext) {
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        System.out.println("-- Projects Managed by You --");
        System.out.println("Index\tProject ID\tProject Name");
        System.out.println("-----------------------------------");
        int index = 1;
        for (Map.Entry<Integer, String> entry : managerProjects.entrySet()) {
            System.out.printf("%d\t%d\t\t%s%n", index++, entry.getKey(), entry.getValue());
        }
        System.out.print("Enter the Project ID to view details: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) {
                projectService.viewProjectByID(appContext, projectID);
            } else {
                System.out.println("Invalid Project ID. Please try again.");
            }
            System.out.print("Press enter to continue...");
            appContext.getScanner().nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
        }
    }

    private void getAllEnquiryMenu(AppContext appContext) {
        List<Enquiry> enquiries = enquiryService.getAllEnquiries(appContext);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            System.out.println("-- All Enquiries --");
            for (Enquiry enquiry : enquiries) {
                System.out.printf("ID: %d, Subject: %s, Status: %s%n",
                        enquiry.getId(), enquiry.getEnquiryText(), enquiry.getStatus());
            }
        }
        System.out.print("Press enter to continue...");
        appContext.getScanner().nextLine();
    }

}
