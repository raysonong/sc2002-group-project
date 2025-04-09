package com.sc2002.view;

import java.util.List;
import java.util.Optional;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.OfficerRegistrationService;
import com.sc2002.controller.ProjectService;
import com.sc2002.enums.OfficerRegistrationStatus;
import com.sc2002.model.OfficerRegistrationModel;

public class HDBOfficerView {
    //Service Declaration
    private ApplicationService applicationService = null;
    private OfficerRegistrationService officerRegistrationService = null;
    private ProjectService projectService = null;

    public void HDBOfficerMenu(AppContext appContext) {
        // Initialize services
        this.applicationService=new ApplicationService(appContext);
        this.officerRegistrationService = new OfficerRegistrationService(appContext);
        this.projectService = new ProjectService(appContext);

        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();


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
                OfficerRegistrationStatus application_status = appContext.getOfficerRegistrationRepo()
                                                        .findbyUserID(appContext.getCurrentUser().getUserID())
                                                        .map(OfficerRegistrationModel::getStatus)
                                                        .orElse(null);

                if (application_status == OfficerRegistrationStatus.APPROVED) {
                    System.out.println("You can't submit a new application as you are registered in an existing project!");
                    break;
                }

                OfficerRegistrationModel registration = officerRegistrationService.registerForProject();
                if (registration != null) {
                    appContext.getOfficerRegistrationRepo().save(registration);
                }
            }
            case "2" -> {
                // Option 2: View Registration Status
                Optional<OfficerRegistrationModel> registrationOpt = appContext.getOfficerRegistrationRepo()
                    .findbyUserID(appContext.getCurrentUser().getUserID());

                if (registrationOpt.isEmpty()) {
                    System.out.println("You have not submitted a registration to join a project yet!");
                }
                else{
                    OfficerRegistrationModel reg = registrationOpt.get();
                    // if (application_status == OfficerRegistrationStatus.APPROVED) {
                    //     int projectID = reg.getProjectID(); // Assumes getProjectID() exists

                    //     projectService.viewProjectByID(projectID); // View project details
                    // }
                    if (reg.getStatus() == OfficerRegistrationStatus.APPROVED) {
                        int projectID = reg.getProjectID();
                        projectService.viewProjectByID(projectID);
                    }
                    System.out.println("Application Status: " + reg.getStatus());
                }
            }
            case "3" -> {
                // Option 3: Update Flat Details

            }
            case "4" -> {
                // Option 4: Generate Flat Selection Receipt
                Optional<OfficerRegistrationModel> registrationOpt = appContext.getOfficerRegistrationRepo()
                .findbyUserID(appContext.getCurrentUser().getUserID());

                if (registrationOpt.isEmpty()) {
                    System.out.println("You have not submitted a registration to join a project yet!");
                    break;
                }

                OfficerRegistrationModel reg = registrationOpt.get();

                // Simulate receipt details (expandable once model includes real flat data)
                System.out.println("=========== Flat Selection Receipt ===========");
                System.out.println("User ID: " + appContext.getCurrentUser().getUserID());
                System.out.println("Application Status: " + reg.getStatus());

                // Mocked flat details (since we can't access actual Flat model)
                System.out.println("Flat Type: 4-Room"); // Example placeholder
                System.out.println("Preferred Floor: 10"); // Example placeholder
                System.out.println("Preferred Region: North"); // Example placeholder

                // Timestamp (optional, for realism)
                System.out.println("Generated on: " + java.time.LocalDateTime.now());

                System.out.println("==============================================");
            }
            case "5" -> {
            }
            case "6" -> {
                // Option 5: Logout
                System.out.println("Logging out...");
                appContext.setCurrentUser(null); // set the CurrentUser null
                }
            default -> {
                // Invalid option selected
                System.out.println("Please select a valid option!");
            }
        }
        System.out.println("Press enter to continue...");
        appContext.getScanner().nextLine();
    }
}
