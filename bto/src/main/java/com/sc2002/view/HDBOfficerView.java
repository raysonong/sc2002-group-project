package com.sc2002.view;

import java.util.List;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.OfficerRegistrationService;
import com.sc2002.model.OfficerRegistrationModel;

public class HDBOfficerView {
    public void HDBOfficerMenu(AppContext appContext) {
        // TODO: Menu for HDB Officer
        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();

        // Service declaration
        ApplicationService applicationService = new ApplicationService();
        OfficerRegistrationService officerRegistrationService = new OfficerRegistrationService();

        System.out.println("HDB Officer Menu:");

        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.print("Please select an option: ");
        userInput = appContext.getScanner().nextLine();

        switch (userInput) { // violates s-SRP for (SOLID), could be implemented better later-on
            case "1" -> {
                // Option 1: Register for Project Team
                OfficerRegistrationModel registration = officerRegistrationService.registerForProject(appContext.getProjectRepo(), appContext.getScanner(), appContext.getCurrentUser());
                if (registration != null) {
                    appContext.getOfficerRegistrationRepo().save(registration);
                }
            }
            case "2" -> {
                // Option 2: View Registration Status
                if (appContext.getOfficerRegistrationRepo().findbyUserID(appContext.getCurrentUser().getUserID()).isEmpty()) {
                    System.out.println("You have not submitted a registration to join a project yet!");
                    break;
                }

                System.out.println(appContext.getOfficerRegistrationRepo().findbyUserID(appContext.getCurrentUser().getUserID()).get().getStatus());
            }
            case "3" -> {
                // Option 3: Update Flat Details
                
            }
            case "4" -> {
                // Option 4: Generate Flat Selection Receipt
                
            }
            case "5" -> {
                // Option 5: Logout
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
