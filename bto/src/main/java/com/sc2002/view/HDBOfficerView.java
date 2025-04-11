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
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.EnquiryModel;
import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.utilities.Receipt;

public class HDBOfficerView {
    //Service Declaration
    private EnquiryService enquiryService = null;
    private ApplicationService applicationService = null;
    private OfficerRegistrationService officerRegistrationService = null;
    private ProjectService projectService = null;

    public void HDBOfficerMenu(AppContext appContext) {
        // Initialize services
        this.enquiryService = new EnquiryService(appContext);
        this.applicationService=new ApplicationService(appContext);
        this.officerRegistrationService = new OfficerRegistrationService(appContext);
        this.projectService = new ProjectService(appContext);

        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();

        System.out.println("\n--HDB Officer Menu--");

        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.print("Please select an option: ");
        userInput = appContext.getScanner().nextLine();

        switch (userInput) {
            case "1" -> {
                // Option 1: Apply for BTO Project
                applyForProjectMenu(appContext);
            }
            case "2" -> {
                // Option 2: View Application Status
                viewApplicationStatusMenu(appContext);
            }
            case "3" -> {
                // Option 3: Update Flat Details
                updateFlatDetailsMenu(appContext);
            }
            case "4" -> {
                // Option 4: Generate Flat Selection Receipt
                generateReceiptMenu(appContext);
            }
            case "5" -> {
                // Option 5: Submit Enquiry
                submitEnquiryMenu(appContext);
            }
            case "6" -> {
                // Option 6: View My Enquiries
                viewMyEnquiriesMenu(appContext);
            }
            case "7" -> {
                // Option 7: Register for Project Team
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
                // Option 8: View Registration Status
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
                // Option 9: View and updating bookings
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
                Optional<OfficerRegistrationModel> officerRegOpt = appContext.getOfficerRegistrationRepo()
                .findbyUserID(appContext.getCurrentUser().getUserID());

                if (officerRegOpt.isEmpty() || officerRegOpt.get().getStatus() != OfficerRegistrationStatus.APPROVED) {
                    System.out.println("You are not an approved officer. Access denied.");
                    break;
                }

                System.out.println("Enter applicant's NRIC:");
                String inputNric = appContext.getScanner().nextLine();

                int officerProjectID = officerRegOpt.get().getProjectID();

                BTOApplicationModel[] outputApplication = new BTOApplicationModel[1];
                boolean found = applicationService.viewApplicationByNRIC(officerProjectID, inputNric, outputApplication);

                if (found) {
                    BTOApplicationModel application = outputApplication[0];

                    // Create a Receipt object
                    Receipt receipt = new Receipt(application);

                    // Print the receipt
                    receipt.printReceipt();
                } else {
                    System.out.println("No application found for the provided NRIC.");
                }
            }
            case "11" -> {
                // Retrieve all enquiries
                List<EnquiryModel> enquiries = enquiryService.getAllEnquiries();
            
                if (enquiries.isEmpty()) {
                    System.out.println("No enquiries found.");
                    return;
                }
            
                // Display all enquiries
                System.out.println("Enquiries:");
                for (int i = 0; i < enquiries.size(); i++) {
                    EnquiryModel enquiry = enquiries.get(i);
                    System.out.println((i + 1) + ". Enquiry ID: " + enquiry.getId() +
                                    ", Applicant NRIC: " + enquiry.getApplicantNRIC() +
                                    ", Response: " + enquiry.getEnquiryText() +
                                    ", Status: " + (enquiry.getStatus() ? "Replied" : "Pending"));
                }
            
                // Prompt officer to select an enquiry to reply to
                System.out.print("Enter the number of the enquiry you want to reply to: ");
                String input = appContext.getScanner().nextLine();
            
                try {
                    int enquiryIndex = Integer.parseInt(input) - 1;
            
                    if (enquiryIndex < 0 || enquiryIndex >= enquiries.size()) {
                        System.out.println("Invalid selection. Please try again.");
                        return;
                    }
            
                    EnquiryModel selectedEnquiry = enquiries.get(enquiryIndex);
            
                    // Display the selected enquiry details
                    System.out.println("Selected Enquiry:");
                    System.out.println("Enquiry ID: " + selectedEnquiry.getId());
                    System.out.println("Applicant NRIC: " + selectedEnquiry.getApplicantNRIC());
                    System.out.println("Message: " + selectedEnquiry.getEnquiryText());
            
                    // Check if the enquiry has already been replied to
                    if (selectedEnquiry.getStatus()) {
                        System.out.println("This enquiry has already been replied to.");
                        return;
                    }
            
                    // Prompt officer to enter a reply
                    System.out.print("Enter your reply: ");
                    String reply = appContext.getScanner().nextLine();
            
                    // Reply to the enquiry
                    selectedEnquiry.replyEnquiry(reply, appContext.getCurrentUser().getUserID());
            
                    System.out.println("Reply sent successfully.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
            case "12" -> {
                // Option 11: Logout
                System.out.println("Logging out...");
                appContext.setCurrentUser(null); // set the CurrentUser null
            }
            default -> {
                // Invalid option selected
                System.out.println("Please select a valid option!");
            }
        }
    }

    private void applyForProjectMenu(AppContext appContext) {
    // display eligible projects 
    System.out.println("Available Projects: ");
    applicationService.viewAvailableProjectsForApplicant(); // CAUSING ERROR IMPLEMENT FIRST

    System.out.print("Enter Project ID to apply for: ");
    int selectedProjectId = appContext.getScanner().nextInt();
    appContext.getScanner().nextLine(); 
    //get project
    BTOProjectModel selectedProject = appContext.getProjectRepo().getProjectByID(selectedProjectId);


    if (selectedProject != null) {
        // apply to the project
        Boolean application = applicationService.applyToProject(
            appContext.getProjectRepo(),
            appContext.getScanner(),
            appContext.getCurrentUser()
        );

        // check application
        if (application) {
            System.out.println("You have successfully applied for project ID: " + selectedProjectId);
        } else {
            System.out.println("Application failed. Please try again.");
        }
    } else {
        System.out.println("Invalid Project ID. Please try again.");
    }
}

    private void viewApplicationStatusMenu(AppContext appContext) {
      ApplicantModel applicant = (ApplicantModel) appContext.getCurrentUser();

    // retrieve the application 
      Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findbyUserID(applicant.getUserID());

    //check if the application exists
      if (applicationOpt.isPresent()) {
        BTOApplicationModel application = applicationOpt.get();

        System.out.println("Your application status is: " + application.getStatus());
    } else {
        System.out.println("You have not applied to any projects yet.");
    }

}

    private void updateFlatDetailsMenu(AppContext appContext) {
        throw new RuntimeException("Not implemented");
    }

    private void generateReceiptMenu(AppContext appContext) {
        ApplicantModel applicant = (ApplicantModel) appContext.getCurrentUser();
    Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findbyUserID(applicant.getUserID());
    
    if (applicationOpt.isPresent()) {
        BTOApplicationModel application = applicationOpt.get();
        // Create a receipt for the application
        Receipt receipt = new Receipt(application);
        // print the receipt
        receipt.printReceipt();
    } else {
        System.out.println("You have not applied to any projects");
    }
}

    private void submitEnquiryMenu(AppContext appContext) {
    // Display eligible projects for the applicant
       System.out.println("Eligible Projects for Enquiry: ");
       applicationService.viewAvailableProjectsForApplicant();

    // ask applicant to select project
      System.out.print("Enter Project ID to submit an enquiry: ");
      int selectedProjectId = appContext.getScanner().nextInt();
      appContext.getScanner().nextLine(); 

    // check project exists
      BTOProjectModel selectedProject = appContext.getProjectRepo().getProjectByID(selectedProjectId);
      if (selectedProject == null) {
         System.out.println("Invalid Project ID. Please try again.");
         return;
     }

   
      System.out.print("Enter your enquiry: ");
      String enquiryText = appContext.getScanner().nextLine();

    // submit using EnquiryService
      String applicantNRIC = ((ApplicantModel) appContext.getCurrentUser()).getNRIC();
      boolean isSubmitted = enquiryService.submitEnquiry(applicantNRIC, selectedProjectId, enquiryText);

      if (isSubmitted) {
        System.out.println("Your enquiry has been submitted successfully");
      } else {
        System.out.println("There was an issue submitting your enquiry. Please try again");
      }
}


   private void viewMyEnquiriesMenu(AppContext appContext) {
    String applicantNRIC = ((ApplicantModel) appContext.getCurrentUser()).getNRIC();

    //get enquiries of applicant 
    List<EnquiryModel> applicantEnquiries = appContext.getEnquiryRepo().findByApplicantNRIC(applicantNRIC);

    //check if no enquiries
    if (applicantEnquiries.isEmpty()) {
        System.out.println("You have not submitted any enquiries.");
        return;
    }

    //disply enquiries
    System.out.println("Your Enquiries:");
    for (int i = 0; i < applicantEnquiries.size(); i++) {
        EnquiryModel enquiry = applicantEnquiries.get(i);
        System.out.println((i + 1) + ". " + "Project ID: " + enquiry.getProjectId() + " | Enquiry: " + enquiry.getEnquiryText());
    }

    //ask user to select enquiry
    System.out.println("Enter the number of the enquiry you want to view/edit/delete or enter 0 to return to the menu: ");
    int selectedEnquiryIndex = appContext.getScanner().nextInt();
    appContext.getScanner().nextLine();

    if (selectedEnquiryIndex == 0) {
        System.out.println("Returning to the menu...");
        return;
    }

    if (selectedEnquiryIndex < 1 || selectedEnquiryIndex > applicantEnquiries.size()) {
        System.out.println("Invalid selection. Returning to the menu...");
        return;
    }

    EnquiryModel selectedEnquiry = applicantEnquiries.get(selectedEnquiryIndex - 1);

    //ask user for action
    System.out.println("You selected Enquiry: " + selectedEnquiry.getEnquiryText());
    System.out.println("What would you like to do?");
    System.out.println("1. View Enquiry");
    System.out.println("2. Edit Enquiry");
    System.out.println("3. Delete Enquiry");
    System.out.print("Enter your choice: ");
    String actionChoice = appContext.getScanner().nextLine();

    switch (actionChoice) {
        case "1":
            //view
            System.out.println("Enquiry Details:");
            System.out.println(selectedEnquiry.getFormattedEnquiry());
            break;
        case "2":
            //edit
            System.out.print("Enter the new enquiry text: ");
            String newEnquiryText = appContext.getScanner().nextLine();
            boolean isEdited = enquiryService.editEnquiryResponse(selectedEnquiry.getId(), newEnquiryText);
            if (isEdited) {
                System.out.println("Your enquiry has been updated.");
            } else {
                System.out.println("There was an issue updating your enquiry.");
            }
            break;
        case "3":
           //delete
            boolean isDeleted = enquiryService.deleteEnquiry(selectedEnquiry.getId());
            if (isDeleted) {
                System.out.println("Your enquiry has been deleted.");
            } else {
                System.out.println("There was an issue deleting your enquiry.");
            }
            break;
        default:
            System.out.println("Invalid choice. Returning to the menu...");
            break;
        }
    }
}
