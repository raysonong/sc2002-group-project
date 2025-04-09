package com.sc2002.view;

import java.util.List;
import java.util.Optional;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.EnquiryService;
import com.sc2002.controller.OfficerRegistrationService;
import com.sc2002.controller.ProjectService;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.OfficerRegistrationStatus;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.OfficerRegistrationModel;

public class HDBOfficerView {
    //Service Declaration
    private EnquiryService enquiryService = null;
    private ApplicationService applicationService = null;
    private OfficerRegistrationService officerRegistrationService = null;
    private ProjectService projectService = null;

    private ApplicantView applicantView = new ApplicantView();

    public void HDBOfficerMenu(AppContext appContext) {
        // Initialize services
        this.enquiryService = new EnquiryService(appContext);
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

        switch (userInput) {
            case "1" -> {
                // Option 1: Apply for BTO Project
                // applicantView.applyForProjectMenu(appContext);
            }
            // case "2" -> {
            //     // Option 2: View Application Status
            //     viewApplicationStatusMenu(appContext);
            // }
            // case "3" -> {
            //     // Option 3: Update Flat Details
            //     updateFlatDetailsMenu(appContext);
            // }
            // case "4" -> {
            //     // Option 4: Generate Flat Selection Receipt
            //     generateReceiptMenu(appContext);
            // }
            // case "5" -> {
            //     // Option 5: Submit Enquiry
            //     submitEnquiryMenu(appContext);
            // }
            // case "6" -> {
            //     // Option 6: View My Enquiries
            //     viewMyEnquiriesMenu(appContext);
            // }
            case "7" -> {
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
            case "8" -> {
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
            case "9" -> {
                // Option 3: View and updating bookings
                Optional<OfficerRegistrationModel> officerRegOpt = appContext.getOfficerRegistrationRepo()
                        .findbyUserID(appContext.getCurrentUser().getUserID());
            
                if (officerRegOpt.isEmpty() || officerRegOpt.get().getStatus() != OfficerRegistrationStatus.APPROVED) {
                    System.out.println("You are not an approved officer. Access denied.");
                    break;
                }

                System.out.println("Enter applicant's NRIC:");
                String input_nric = appContext.getScanner().nextLine();
            
                int officerProjectID = officerRegOpt.get().getProjectID();

                BTOApplicationModel[] outputApplication = new BTOApplicationModel[1];
                boolean found = applicationService.viewApplicationByNRIC(officerProjectID, input_nric, outputApplication);

                if (found) {
                    BTOApplicationModel application = outputApplication[0];
                    int applicationID = application.getApplicationID();

                    System.out.println("");
                    System.out.println("Application Details:");
                    System.out.println("----------------------------------------------");
                    System.out.println("Application ID: " + application.getApplicationID());
                    System.out.println("Applicant NRIC / Name: " + application.getApplicantNRIC() + " / " + application.getApplicantName());
                    System.out.println("Flat Type: " + application.getFlatType());
                    System.out.println("Status: " + application.getStatus());
                    System.out.println("Submission Date: " + application.getSubmissionDate());
                    System.out.println("----------------------------------------------");
                    System.out.println("1. Book application");
                    System.out.println("2. Update applicant's flat type");
                    System.out.print("Select an option: ");

                    String subOption = appContext.getScanner().nextLine();

                    switch (subOption) {
                        case "1" -> {
                            // Option 1: Update Booking Status
                            boolean updated = applicationService.updateApplicationStatusToBooked(applicationID);

                            if (updated) {
                                System.out.println("Booking status updated successfully to 'BOOKED'.");
                            } else {
                                System.out.println("Failed to update booking status. Only applications with status 'SUCCESSFUL' can be updated.");
                            }
                        }
                        case "2" -> {
                            // Option 2: Update applicant's flat type
                            System.out.println("Available Flat Types:");
                            for (FlatType flatType : FlatType.values()) {
                                System.out.println("- " + flatType);
                            }
                            
                            System.out.print("Enter new flat type: ");
                            String newFlatTypeInput = appContext.getScanner().nextLine();
                        
                            try {
                                // Convert the input to a FlatType enum
                                FlatType newFlatType = FlatType.valueOf(newFlatTypeInput.toUpperCase());
                            
                                // Call the service to update the flat type
                                boolean updated = applicationService.updateApplicationFlatType(applicationID, newFlatType);
                            
                                if (updated) {
                                    System.out.println("Flat type updated successfully.");
                                } else {
                                    System.out.println("Failed to update flat type. Please check the Application ID.");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid flat type entered. Please try again.");
                            }
                        
                        }
                        default -> System.out.println("Invalid option selected.");
                    }
                } else {
                    System.out.println("No application found or you are not authorized to view this application.");
                    break;
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
    }
}
