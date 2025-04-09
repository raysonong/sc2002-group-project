package com.sc2002.view;

import java.util.List;
import java.util.Optional;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.OfficerRegistrationService;
import com.sc2002.controller.ProjectService;
import com.sc2002.enums.OfficerRegistrationStatus;
import com.sc2002.model.BTOApplicationModel;
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

                    if (reg.getStatus() == OfficerRegistrationStatus.APPROVED) {
                        int projectID = reg.getProjectID();
                        projectService.viewProjectByID(projectID);
                    }
                    System.out.println("Application Status: " + reg.getStatus());
                }
            }
            case "3" -> {
                // Option 3: View and updating bookings
                Optional<OfficerRegistrationModel> officerRegOpt = appContext.getOfficerRegistrationRepo()
                        .findbyUserID(appContext.getCurrentUser().getUserID());
            
                if (officerRegOpt.isEmpty() || officerRegOpt.get().getStatus() != OfficerRegistrationStatus.APPROVED) {
                    System.out.println("You are not an approved officer. Access denied.");
                    break;
                }
            
                int officerProjectID = officerRegOpt.get().getProjectID();

                System.out.println("1. View all applications");
                System.out.println("2. Book application");
                System.out.println("3. Update applicant's flat type");
                System.out.print("Select an option: ");

                String subOption = appContext.getScanner().nextLine();

                switch (subOption) {
                    case "1" -> {
                        // Fetch all applications for the officer's project with status "SUCCESSFUL"
                        List<BTOApplicationModel> applications = appContext.getApplicationRepo()
                                .findByProjectID(officerProjectID);
                    
                        if (applications.isEmpty()) {
                            System.out.println("No applications found for your project.");
                        } else {
                            System.out.println("Applications:");
                            System.out.println("----------------------------------------------");
                            for (BTOApplicationModel application : applications) {
                                System.out.println("Application ID: " + application.getApplicationID());
                                System.out.println("Applicant NRIC / Name: " + application.getApplicantNRIC() + " / " + application.getApplicantName());
                                System.out.println("Flat Type: " + application.getFlatType());
                                System.out.println("Status: " + application.getStatus());
                                System.out.println("Submission Date: " + application.getSubmissionDate());
                                System.out.println("----------------------------------------------");
                            }
                        }
                    }
                    case "2" -> {
                                // Option 2: Update Booking Status
                                // System.out.print("Enter Application ID to update status: ");
                                // String applicationID = appContext.getScanner().nextLine();
                    
                                // // boolean updated = applicationService.updateBookingStatus(applicationID, ApplicationStatus.BOOKED);
                                // if (updated) {
                                //     System.out.println("Booking status updated successfully.");
                                // } else {
                                //     System.out.println("Failed to update booking status. Please check the Application ID.");
                                // }
                            }
                            case "3" -> {
                                // Option 3: Cancel Booking
                                // System.out.print("Enter Application ID to cancel booking: ");
                                // String applicationID = appContext.getScanner().nextLine();
                    
                                // boolean canceled = applicationService.cancelBooking(applicationID);
                                // if (canceled) {
                                //     System.out.println("Booking canceled successfully.");
                                // } else {
                                //     System.out.println("Failed to cancel booking. Please check the Application ID.");
                                // }
                            }
                            default -> System.out.println("Invalid option selected.");
                        }
            }
            case "10" -> {
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
