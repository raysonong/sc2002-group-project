package com.sc2002.view;

import java.util.List;
import java.util.Optional;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.EnquiryService;
import com.sc2002.controller.ProjectService;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.EnquiryModel;
import com.sc2002.utilities.Receipt;

public class ApplicantView {
    // declare all the services required by Applicant
    private final EnquiryService enquiryService;
    private final ProjectService projectService = new ProjectService();
    private final ApplicationService applicationService = new ApplicationService();

    public ApplicantView(AppContext appContext) {
        // Pass the EnquiryRepo from appContext to the EnquiryService constructor
        this.enquiryService = new EnquiryService(appContext.getEnquiryRepo());
    }
    
    public void ApplicantMenu(AppContext appContext) {
        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();

        System.out.println("--Applicant Menu--");
        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }
        System.out.println("Option " + (menus.size() + 1) + ": Logout");
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
                // Option 7: Logout
                System.out.println("Logging out...");
                appContext.setCurrentUser(null);
            }
            default -> {
                System.out.println("Please select a valid option!");
            }
        }
    }

    private void applyForProjectMenu(AppContext appContext) {
    // display eligible projects 
    System.out.println("Available Projects: ");
    applicationService.viewAvailableProjectsForApplicant(appContext);


    System.out.print("Enter Project ID to apply for: ");
    int selectedProjectId = appContext.getScanner().nextInt();
    appContext.getScanner().nextLine(); 
    //get project
    BTOProjectModel selectedProject = appContext.getProjectRepo().getProjectByID(selectedProjectId);


    if (selectedProject != null) {
        // apply to the project
        BTOApplicationModel application = applicationService.applyToProject(
            appContext.getProjectRepo(),
            appContext.getScanner(),
            appContext.getCurrentUser()
        );

        // check application
        if (application != null) {
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
       applicationService.viewAvailableProjectsForApplicant(appContext); 

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
            boolean isEdited = enquiryService.editEnquiryResponse(appContext, selectedEnquiry.getId(), newEnquiryText);
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
