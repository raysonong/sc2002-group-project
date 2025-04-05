package com.sc2002.view;

import java.util.List;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.EnquiryService;
import com.sc2002.controller.ProjectManagementService;

public class HDBManagerView {
    // declare all the services required by Manager
    private final ProjectManagementService projectManagementService=new ProjectManagementService();
    private final EnquiryService enquiryService = new EnquiryService();
    
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
            }
            case "4" -> {
            // Option 4: Toggle the visibility of a BTO project
            }
            case "5" -> {
            // Option 5: View all BTO projects
            }
            case "6" -> {
            // Option 6: View details of a specific BTO project
            }
            case "7" -> {
            // Option 7: Approve officer registration
            }
            case "8" -> {
            // Option 8: Reject officer registration
            }
            case "9" -> {
            // Option 9: Approve an application
            }
            case "10" -> {
            // Option 10: Reject an application
            }
            case "11" -> {
            // Option 11: Approve a withdrawal request
            }
            case "12" -> {
            // Option 12: Reject a withdrawal request
            }
            case "13" -> {
            // Option 13: Generate reports
            }
            case "14" -> {
            // Option 14: Logout
            System.out.println("Logging out...");
            appContext.setCurrentUser(null); // set the CurrentUser null
            }
            default -> {
            // Invalid option selected
            System.out.println("Please select a valid option!");
            }
        }
    }// End of HDBManagerMenu

    public void editBTOProjectMenu(AppContext appContext) { 
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

    public void deleteBTOProjectMenu(AppContext appContext) {
        System.out.println("--deleteBTOProjectMenu--\n(1) Confirm Deletion\n(2) Cancel\nPlease select an option: ");
        String userOption = appContext.getScanner().nextLine();
        switch (userOption) {
            case "1" -> {
                // Confirm Deletion
            }
            case "2" -> {
                // Cancel
            }
            default -> {
                System.out.println("Invalid option selected!");
            }
        }
    }
}
