package com.sc2002.view;

import java.util.List;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ProjectManagementService;

public class HDBManagerView {
    public void HDBManagerMenu(AppContext appContext) {
        // TODO: Menu for HDB Manager
        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();

        // Service declaration
        ProjectManagementService projectManagementService = new ProjectManagementService();

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
            appContext.getProjectRepo().save(projectManagementService.createProject(appContext.getProjectRepo().getLastProjectID(), appContext.getScanner(), appContext.getCurrentUser()));
            }
            case "2" -> {
            // Option 2: Edit an existing BTO project
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
    }
}
