package com.sc2002.view;

import java.util.List;
import java.util.Optional;

import com.sc2002.config.AppContext;
import com.sc2002.controllers.ApplicationController;
import com.sc2002.controllers.EnquiryController;
import com.sc2002.controllers.OfficerRegistrationController;
import com.sc2002.controllers.ProjectController;
import com.sc2002.controllers.UserController;
import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.OfficerRegistrationStatus;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.EnquiryModel;
import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.utilities.Receipt;

/**
 * Handles the user interface and interactions for users with the HDB Officer
 * role. Provides menu options for viewing projects, managing personal BTO
 * applications (if applicable), registering for project teams, viewing
 * registration status, managing applicant bookings, generating receipts, and
 * handling enquiries for assigned projects.
 */
public class HDBOfficerView {

    //Service Declaration
    /**
     * Controller for handling enquiry-related actions.
     */
    private EnquiryController enquiryController = null;
    /**
     * Controller for handling application-related actions (both personal and
     * applicant management).
     */
    private ApplicationController applicationController = null;
    /**
     * Controller for handling officer registration for projects.
     */
    private OfficerRegistrationController officerRegistrationController = null;
    /**
     * Controller for handling project viewing actions.
     */
    private ProjectController projectController = null;
    /**
     * Controller for handling user-related actions like password reset.
     */
    private UserController userController = null;
    // Initialize other views
    /**
     * View component for displaying project details and filters.
     */
    private ProjectView projectView = new ProjectView(); // used to print filtered projectView

    /**
     * Default constructor for HDBOfficerView. Initializes view components.
     * Controllers are initialized within the HDBOfficerMenu method using the
     * AppContext.
     */
    public HDBOfficerView() {
        // Default constructor
    }

    /**
     * Displays the main menu for the HDB Officer user and handles user input
     * for navigation.
     *
     * @param appContext The application context containing shared resources and
     * state.
     */
    public void HDBOfficerMenu(AppContext appContext) {
        // Initialize services
        this.enquiryController = new EnquiryController(appContext);
        this.applicationController = new ApplicationController(appContext);
        this.officerRegistrationController = new OfficerRegistrationController(appContext);
        this.projectController = new ProjectController(appContext);
        this.userController = new UserController();

        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();

        System.out.println("\n--HDB Officer Menu--");
        //prints the current Project this user is managing
        projectView.projectManagingMenu(appContext);
        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.print("Please select an option: ");
        userInput = appContext.getScanner().nextLine();

        switch (userInput) {
            case "1" -> {
                // Option 1: View Projects with Filters
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
                // Option 7: Register for Project Team
                registerForProjectMenu(appContext);
            }
            case "8" -> {
                // Option 8: View Registration Status
                viewRegistrationStatusMenu(appContext);
            }
            case "9" -> {
                // Option 9: View and updating bookings
                viewAndUpdateBookings(appContext);
            }
            case "10" -> {
                // Option 10: Generate receipt for application
                generateApplicationReceipt(appContext);
            }
            case "11" -> {
                // Option 11: Manage Enquiries
                manageEnquiriesMenu(appContext);
            }
            case "12" -> {
                // Option 12: Reset Password
                userController.resetPassword(appContext.getCurrentUser(), appContext.getScanner());
            }
            case "13" -> {
                // Option 13: Logout
                System.out.println("Logging out...");
                appContext.setCurrentUser(null); // set the CurrentUser null
            }
            default -> {
                // Invalid option selected
                System.out.println("Please select a valid option!");
            }
        }
    }

    /**
     * Handles the menu flow for an officer applying to a BTO project (as an
     * applicant). Checks eligibility, displays available projects, and
     * processes the application submission.
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
     * Handles the menu flow for an officer viewing the status of their personal
     * BTO application. Allows the officer (as an applicant) to request
     * withdrawal if the application is in a suitable state.
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
        } else {
            System.out.println("You have not applied to any projects.");
        }
    }

    /**
     * Handles the menu flow for an officer generating and displaying a receipt
     * for their personal successful application.
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
     * Handles the menu flow for an officer submitting an enquiry about a
     * specific BTO project (acting as an applicant). Checks if the officer is
     * currently managing a project.
     *
     * @param appContext The application context.
     */
    private void submitEnquiryMenu(AppContext appContext) {
        System.out.println("Eligible Projects for Enquiry: ");
        applicationController.viewAvailableProjectsForApplicant();

        // ask applicant to select project
        System.out.print("Enter Project ID to submit an enquiry: ");
        int selectedProjectID = appContext.getScanner().nextInt();
        appContext.getScanner().nextLine();

        // // check project selected is available for applicant
        List<BTOProjectModel> availableProjects = applicationController.getAvailableProjectsForApplicant();;
        BTOProjectModel selectedProject = null;
        for (BTOProjectModel project : availableProjects) {
            if (project.getProjectID() == selectedProjectID) {
                selectedProject = project;
                break;
            }
        }
        if (selectedProject == null) {
            System.out.println("Invalid Project ID or project is not available for your profile.");
            return;
        }
        System.out.print("Enter your enquiry: ");
        String enquiryText = appContext.getScanner().nextLine();

        // submit using EnquiryController
        String applicantNRIC = ((ApplicantModel) appContext.getCurrentUser()).getNRIC();
        boolean isSubmitted = enquiryController.submitEnquiry(applicantNRIC, selectedProjectID, enquiryText);

        if (!isSubmitted) {
            System.out.println("There was an issue submitting your enquiry. Please try again");
        }
    }

    /**
     * Handles the menu flow for an officer viewing, editing, or deleting their
     * personal enquiries (acting as an applicant). Checks if the officer is
     * currently managing a project.
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
                    System.out.println("You cannot edit a replied enquiry.");
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
                    System.out.println("You cannot delete a replied enquiry.");
                    break;
                }
                boolean isDeleted = enquiryController.deleteEnquiry(selectedEnquiry.getID());
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

    /**
     * Handles the menu flow for an officer registering their interest to join a
     * specific BTO project team.
     *
     * @param appContext The application context.
     */
    private void registerForProjectMenu(AppContext appContext) {
        // Check if the user is already registered for a project
        boolean registration = officerRegistrationController.registerForProject();

        if (!registration) {
            return;
        }

        System.out.println("Your application has been created successfully and is pending approval from the project manager!");
    }

    /**
     * Handles the menu flow for an officer viewing the status of their
     * registration request to join a project team. Displays project details if
     * approved.
     *
     * @param appContext The application context.
     */
    private void viewRegistrationStatusMenu(AppContext appContext) {
        Optional<OfficerRegistrationModel> registrationOpt = appContext.getOfficerRegistrationRepo()
                .findbyUserID(appContext.getCurrentUser().getUserID());

        if (registrationOpt.isEmpty()) {
            System.out.println("You have not submitted a registration to join a project yet!");
            return;
        }

        OfficerRegistrationModel reg = registrationOpt.get();

        if (reg.getStatus() == OfficerRegistrationStatus.APPROVED) {
            int projectID = reg.getProjectID();
            projectController.viewProjectByID(projectID);
        }
        System.out.println("Application Status: " + reg.getStatus());
    }

    /**
     * Handles the menu flow for an officer to view and update bookings for
     * applicants within the project they are managing. Allows booking
     * confirmation and flat type updates.
     *
     * @param appContext The application context.
     */
    private void viewAndUpdateBookings(AppContext appContext) {
        // Retrieve the projects managed by the officer
        List<BTOProjectModel> managedProjects = appContext.getProjectRepo().getProjectsByOfficer(appContext.getCurrentUser());

        if (managedProjects.isEmpty()) {
            System.out.println("You must be an approving officer for a project to view or update bookings.");
            return;
        }

        // Assuming an officer can manage only one project at a time
        BTOProjectModel managedProject = managedProjects.get(0);

        System.out.println("Managing Project: " + managedProject.getProjectName());

        List<BTOApplicationModel> applicants = appContext.getApplicationRepo().findByProjectID(managedProject.getProjectID());

        for (int i = 0; i < applicants.size(); i++) {
            if (applicants.get(i).getStatus() == ApplicationStatus.SUCCESSFUL || applicants.get(i).getStatus() == ApplicationStatus.BOOKED) {
                System.out.println((i + 1) + ". " + applicants.get(i).getApplicantNRIC());
            } else {
                applicants.remove(i);
            }
        }

        if (applicants.isEmpty()) {
            System.out.println("There are no applicants!");
            return;
        }

        System.out.print("Enter applicant's NRIC: ");
        String inputNric = appContext.getScanner().nextLine();

        // Check if the applicant's application exists for the managed project
        BTOApplicationModel[] outputApplication = new BTOApplicationModel[1];
        boolean found = applicationController.viewApplicationByNRIC(managedProject.getProjectID(), inputNric, outputApplication);

        if (found) {
            BTOApplicationModel application = outputApplication[0];
            int applicationID = application.getApplicationID();

            // Display application details
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
            System.out.println("3. Exit to Menu");
            System.out.print("Select an option: ");

            String subOption = appContext.getScanner().nextLine();

            switch (subOption) {
                case "1" -> {
                    // Option 1: Update Booking Status
                    boolean updated = applicationController.updateApplicationStatusToBooked(applicationID);

                    if (updated) {
                        System.out.println("Booking status updated successfully to 'BOOKED'.");
                    } else {
                        System.out.println("Failed to update booking status. Only applications with status 'SUCCESSFUL' can be updated.");
                    }
                }
                case "2" -> {
                    // Option 2: Update applicant's flat type
                    if (application.getStatus() != ApplicationStatus.SUCCESSFUL) {
                        System.out.println("This application requires approval to updates its flat type, please check again later.");
                        return;
                    }

                    System.out.println("Available Flat Types:");
                    List<FlatType> availableFlatTypes = managedProject.getAvailableFlatTypes();
                    for (FlatType flatType : availableFlatTypes) {
                        System.out.println("- " + flatType);
                    }

                    System.out.print("Enter new flat type: ");
                    String newFlatTypeInput = appContext.getScanner().nextLine();

                    try {
                        // Convert the input to a FlatType enum
                        FlatType newFlatType = FlatType.valueOf(newFlatTypeInput.toUpperCase());

                        // Check if officers tries to reset the same flat type
                        if (application.getFlatType() == newFlatType) {
                            System.out.println("Flat type is already set to " + newFlatType + ". No changes made.");
                            return;
                        }
                        // Call the service to update the flat type
                        boolean updated = applicationController.updateApplicationFlatType(applicationID, newFlatType, availableFlatTypes);

                        if (updated) {
                            System.out.println("Flat type updated successfully.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid flat type entered. Please try again.");
                    }

                }
                case "3" -> {
                    System.out.println("Exiting to menu...");
                }
                default ->
                    System.out.println("Invalid option selected.");
            }
        } else {
            System.out.println("No application found or you are not authorized to view this application.");
        }
    }

    /**
     * Handles the menu flow for an officer to generate a receipt for a specific
     * applicant's application within the project they are managing.
     *
     * @param appContext The application context.
     */
    private void generateApplicationReceipt(AppContext appContext) {
        // Retrieve the projects managed by the officer
        List<BTOProjectModel> managedProjects = appContext.getProjectRepo().getProjectsByOfficer(appContext.getCurrentUser());

        if (managedProjects.isEmpty()) {
            System.out.println("You must be an approving officer for a project to generate a receipt.");
            return;
        }

        System.out.println("Enter applicant's NRIC:");
        String inputNric = appContext.getScanner().nextLine();

        int officerProjectID = managedProjects.get(0).getProjectID();

        BTOApplicationModel[] outputApplication = new BTOApplicationModel[1];
        boolean found = applicationController.viewApplicationByNRIC(officerProjectID, inputNric, outputApplication);

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

    /**
     * Handles the menu flow for an officer to view and reply to enquiries
     * related to the project they are managing.
     *
     * @param appContext The application context.
     */
    private void manageEnquiriesMenu(AppContext appContext) {
        // Retrieve the projects managed by the officer
        List<BTOProjectModel> managedProjects = appContext.getProjectRepo().getProjectsByOfficer(appContext.getCurrentUser());

        if (managedProjects.isEmpty()) {
            System.out.println("You must be an approving officer for a project to manage enquiries.");
            return;
        }

        // Retrieve all enquiries
        List<EnquiryModel> enquiries = enquiryController.getAllEnquiries();

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        // Display all enquiries
        System.out.println("Enquiries:");
        for (int i = 0; i < enquiries.size(); i++) {
            EnquiryModel enquiry = enquiries.get(i);
            System.out.println((i + 1) + ". Applicant NRIC: " + enquiry.getApplicantNRIC()
                    + ", Response: " + enquiry.getEnquiryText()
                    + ", Status: " + (enquiry.getStatus() ? "Replied" : "Pending"));
        }

        // Prompt officer to select an enquiry to reply to
        System.out.print("Enter the row number you want to view: ");
        String input = appContext.getScanner().nextLine();

        try {
            int enquiryIndex = Integer.parseInt(input) - 1;

            if (enquiryIndex < 0 || enquiryIndex >= enquiries.size()) {
                System.out.println("Invalid selection. Please try again.");
                return;
            }

            // Display the selected enquiry details
            enquiryController.viewEnquiry(enquiryIndex);
            System.out.println("What would you like to do?");
            System.out.println("1. Create/Edit Reply");
            System.out.println("2. Exit to Menu");
            System.out.print("Enter your choice: ");

            input = appContext.getScanner().nextLine();
            switch (input) {
                case "1" -> {
                    String reply;
                    EnquiryModel selectedEnquiry = enquiries.get(enquiryIndex);

                    // Check if the enquiry has already been replied to
                    if (selectedEnquiry.getStatus()) {
                        System.out.println("Edit your reply:");
                        reply = appContext.getScanner().nextLine();

                        enquiryController.editEnquiryResponse(enquiryIndex, reply);
                        System.out.println("Reply edited successfully.");
                        return;
                    }

                    // Prompt officer to enter a reply
                    System.out.print("Enter your reply: ");
                    reply = appContext.getScanner().nextLine();

                    // Reply to the enquiry
                    selectedEnquiry.replyEnquiry(reply, appContext.getCurrentUser().getUserID());
                    System.out.println("Reply sent successfully.");
                    return;
                }
                case "2" -> {
                    System.out.println("Exiting to menu...");
                    return;
                }
                default ->
                    System.out.println("Invalid option selected.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

}
