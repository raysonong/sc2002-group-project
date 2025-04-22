package com.sc2002.view;

import java.util.List;
import java.util.Optional;

import com.sc2002.config.AppContext;
import com.sc2002.controllers.ApplicationController;
import com.sc2002.controllers.EnquiryController;
import com.sc2002.controllers.ProjectController;
import com.sc2002.controllers.UserController;
import com.sc2002.enums.ApplicationStatus;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.EnquiryModel;
import com.sc2002.utilities.Receipt;

/**
 * Handles the user interface and interactions for users with the Applicant role.
 * Provides menu options for viewing projects, applying for projects, managing applications,
 * and handling enquiries.
 */
public class ApplicantView {

    /** Controller for handling enquiry-related actions. */
    private EnquiryController enquiryController = null;
    /** Controller for handling project viewing actions. */
    private ProjectController projectController = null;
    /** Controller for handling application-related actions. */
    private ApplicationController applicationController = null;
    /** Controller for handling user-related actions like password reset. */
    private UserController userController = null;
    /** View component for displaying project details and filters. */
    private ProjectView projectView = new ProjectView(); // used to print filtered projectView

    /**
     * Default constructor for ApplicantView.
     * Initializes the view components. Controllers are initialized within the ApplicantMenu method using the AppContext.
     */
    public ApplicantView() {
        // Default constructor - controllers are initialized later with AppContext
    }

    /**
     * Displays the main menu for the Applicant user and handles user input for navigation.
     *
     * @param appContext The application context containing shared resources and state.
     */
    public void ApplicantMenu(AppContext appContext) {

        // Initialize Services with context
        enquiryController = new EnquiryController(appContext);
        projectController = new ProjectController(appContext);
        applicationController = new ApplicationController(appContext);
        userController = new UserController();
        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();

        System.out.println("\n--Applicant Menu--");
        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.println("Option " + (menus.size() + 1) + ": Reset Password");
        System.out.println("Option " + (menus.size() + 2) + ": Logout");
        System.out.print("Please select an option: ");
        userInput = appContext.getScanner().nextLine();
        try {
            switch (userInput) {
                case "1" -> {
                    projectView.viewProjectFilterableMenu(appContext);
                }
                case "2" -> {
                    // Option 2: Apply for BTO Project
                    applyForProjectMenu(appContext);
                }
                case "3" -> {
                    // Option 3: View Application Status
                    viewApplicationStatusMenu(appContext);
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
                    // Option 7: Reset Password
                    userController.resetPassword(appContext.getCurrentUser(), appContext.getScanner());
                }
                case "8" -> {
                    // Option 8: Logout
                    System.out.println("Logging out...");
                    appContext.setCurrentUser(null);
                }
                default -> {
                    System.out.println("Please select a valid option!");
                }
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }

    /**
     * Handles the menu flow for applying to a BTO project.
     * Checks eligibility, displays available projects, and processes the application submission.
     *
     * @param appContext The application context.
     */
    private void applyForProjectMenu(AppContext appContext) {
        // check if applicant is currently applying a project
        if (!appContext.getApplicationRepo().canApplyForProject(appContext.getCurrentUser().getUserID())) {
            System.out.println("You can't applied for more than 1 project!");
            System.out.println("Press enter to continue...");
            appContext.getScanner().nextLine();
            return;
        }

        // display eligible projects 
        System.out.println("Available Projects: ");
        applicationController.viewAvailableProjectsForApplicant();

        Boolean application = applicationController.applyToProject(
                appContext.getProjectRepo(),
                appContext.getScanner(),
                appContext.getCurrentUser()
        );

        if (application) {
            System.out.println("Successfully applied to the project.");
        } else {
            System.out.println("Application was not submitted.");
        }

    }

    /**
     * Handles the menu flow for viewing the status of the applicant's BTO application.
     * Allows the applicant to request withdrawal if the application is in a suitable state.
     *
     * @param appContext The application context.
     */
    private void viewApplicationStatusMenu(AppContext appContext) {
        ApplicantModel applicant = (ApplicantModel) appContext.getCurrentUser();

        // retrieve the application 
        Optional<BTOApplicationModel> applicationOpt = appContext.getApplicationRepo().findbyUserID(applicant.getUserID());

        //check if the application exists
        if (applicationOpt.isPresent()) {
            BTOApplicationModel application = applicationOpt.get();

            System.out.println("Your application status is: " + application.getStatus());

            if (application.getStatus() == ApplicationStatus.SUCCESSFUL || application.getStatus() == ApplicationStatus.BOOKED
                    || application.getStatus() == ApplicationStatus.PENDING) {
                String withdrawChoice;
                do {
                    System.out.println("Would you like to withdraw your application? (yes/no)");
                    withdrawChoice = appContext.getScanner().nextLine().trim().toLowerCase();

                    if (withdrawChoice.equals("yes")) {
                        application.setWithdrawalRequested(true);
                        System.out.println("Your application withdrawal has been requested.");
                    } else if (withdrawChoice.equals("no")) {
                        System.out.println("You chose not to withdraw your application.");
                    } else {
                        System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    }
                } while (!withdrawChoice.equals("yes") && !withdrawChoice.equals("no"));
            }
        }
        else{
            System.out.println("You have not applied to any projects.");
        }
    }

    /**
     * Handles the menu flow for generating and displaying a receipt for a successful application
     * (typically after flat selection, although current implementation checks only for application existence).
     *
     * @param appContext The application context.
     */
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

    /**
     * Handles the menu flow for submitting an enquiry about a specific BTO project.
     *
     * @param appContext The application context.
     */
    private void submitEnquiryMenu(AppContext appContext) {
        // Display eligible projects for the applicant
        System.out.println("Eligible Projects for Enquiry: ");
        applicationController.viewAvailableProjectsForApplicant();

        // ask applicant to select project
        System.out.print("Enter Project ID to submit an enquiry: ");
        int selectedProjectID = appContext.getScanner().nextInt();
        appContext.getScanner().nextLine();


        // //check project selected is available for applicant
        // List<BTOProjectModel> availableProjects = applicationController.getAvailableProjectsForApplicant();

        // BTOProjectModel selectedProject = null;
        // for (BTOProjectModel project : availableProjects) {
        //     if (project.getProjectID() == selectedProjectID) {
        //         selectedProject = project;
        //         break;
        //     }
        // }
        
        // if (selectedProject == null) {
        //     System.out.println("Invalid Project ID or project is not available for your profile.");
        //     return;
        // }

        System.out.print("Enter your enquiry: ");
        String enquiryText = appContext.getScanner().nextLine();

        // submit using EnquiryService
        String applicantNRIC = ((ApplicantModel) appContext.getCurrentUser()).getNRIC();
        boolean isSubmitted = enquiryController.submitEnquiry(applicantNRIC, selectedProjectID, enquiryText);

        if (!isSubmitted) {
            System.out.println("There was an issue submitting your enquiry. Please try again");
        }
    }

    /**
     * Handles the menu flow for viewing, editing, or deleting enquiries submitted by the applicant.
     *
     * @param appContext The application context.
     */
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
            System.out.println((i + 1) + ". " + "Enquiry ID: " + enquiry.getID() + " | Enquiry: " + enquiry.getEnquiryText());
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
                if (selectedEnquiry.getStatus()) {
                    System.out.println("This enquiry has already been replied to and cannot be edited.");
                    break;
                }
                System.out.print("Enter the new enquiry text: ");
                String newEnquiryText = appContext.getScanner().nextLine();
                selectedEnquiry.editEnquiry(newEnquiryText);
                System.out.println("Your enquiry has been updated.");

                break;
            case "3":
                //delete
                if (selectedEnquiry.getStatus()) {
                    System.out.println("This enquiry has already been replied to and cannot be deleted.");
                    break;
                }
                boolean isDeleted = enquiryController.deleteEnquiry(selectedEnquiry.getID());
                if (isDeleted) {
                    System.out.println("Your enquiry has been deleted.");
                } 
                else {
                    System.out.println("There was an issue deleting your enquiry.");
                }
                break;
            default:
                System.out.println("Invalid choice. Returning to the menu...");
                break;
        }
    }

}
