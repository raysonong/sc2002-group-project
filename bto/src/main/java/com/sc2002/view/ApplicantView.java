package com.sc2002.view;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.EnquiryService;
import com.sc2002.controller.ProjectService;
import com.sc2002.enums.FlatType;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.EnquiryModel;
import com.sc2002.model.BTOProjectModel; 
import com.sc2002.model.ApplicantModel;   
import com.sc2002.repositories.ApplicationRepo; 
import com.sc2002.utilities.Receipt;

public class ApplicantView {
    // declare all the services required by Applicant
    private final EnquiryService enquiryService = new EnquiryService();
    private final ProjectService projectService = new ProjectService();
    private final ApplicationService applicationService = new ApplicationService();
    
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
        throw new RuntimeException("Not implemented");
    }

    private void submitEnquiryMenu(AppContext appContext) {
        throw new RuntimeException("Not implemented");
    }

    private void viewMyEnquiriesMenu(AppContext appContext) {
        throw new RuntimeException("Not implemented");
    }
}
