package com.sc2002.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sc2002.enums.Neighborhood;

import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.EnquiryModel;
import com.sc2002.model.OfficerRegistrationModel;

import com.sc2002.controllers.ProjectController;
import com.sc2002.controllers.ProjectManagementController;
import com.sc2002.controllers.ReportingController;
import com.sc2002.controllers.UserController;
import com.sc2002.controllers.ApplicationController;
import com.sc2002.controllers.EnquiryController;
import com.sc2002.controllers.OfficerRegistrationController;

import com.sc2002.config.AppContext;

public class HDBManagerView {

    // declare all the services required by Manager
    private ProjectController projectController = null;
    private ProjectManagementController projectManagementController = null;
    private EnquiryController enquiryController = null;
    
    private OfficerRegistrationController officerRegistrationController = null;
    private ReportingController reportingController = null;
    private ApplicationController applicationController = null;
    private UserController userController = null;
    // Initialize other views
    private ProjectView projectView = new ProjectView(); // used to print filtered projectView

    public void HDBManagerMenu(AppContext appContext) {
        // Initializing variables & services,
        // we did services 2 ways, 1 where we have repo as attribute.
        // the other where we parse repo as parameters.
        // *Only enquiryController uses repo as attribute. Part of learning process
        String userInput = "";

        List<String> menus = appContext.getCurrentUser().getMenuOptions();
        this.projectManagementController = new ProjectManagementController(appContext);
        this.enquiryController = new EnquiryController(appContext); // Only enquiryRepo, we parse repo as parameter to make it attribute
        this.projectController = new ProjectController(appContext);
        this.officerRegistrationController = new OfficerRegistrationController(appContext);
        this.reportingController = new ReportingController(appContext);
        this.applicationController = new ApplicationController(appContext);
        this.userController = new UserController();


        System.out.println("\n--HDB Manager Menu--");
        // View which project Managing is currently Managing
        projectView.projectManagingMenu(appContext);
        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.print("Please select an option: ");
        userInput = appContext.getScanner().nextLine();

        switch (userInput) {
            case "1" -> {
                // Option 1: View projects with filters
                projectView.viewProjectFilterableMenu(appContext);
            }
            case "2" -> {
                // Option 2: Create a new BTO project
                createProjectMenu(appContext);
            }
            case "3" -> {
                // Option 3: Edit an existing BTO project
                editBTOProjectMenu(appContext);
            }
            case "4" -> {
                // Option 4: Delete an existing BTO project
                deleteBTOProjectMenu(appContext);
            }
            case "5" -> {
                // Option 5: Toggle the visibility of a BTO project
                toggleProjectVisibilityMenu(appContext);
            }
            case "6" -> {
                // Option 6: View all BTO projects
                getAllBTOProjectMenu(appContext);
            }
            case "7" -> {
                // Option 7: View details of a specific BTO project
                getBTOProjectByUserIDMenu(appContext);
            }
            case "8" -> {
                // Option 8: View Enquiries
                getAllEnquiryMenu(appContext);
            }
            case "9" -> {
                // Option 9: Reply Enquiries
                editEnquiryMenu(appContext);
            }
            case "10" -> {
                // Option 10: Approve officer registration
                approveOfficerRegistrationMenu(appContext);
            }
            case "11" -> {
                // Option 11: Reject officer registration
                rejectOfficerRegistrationMenu(appContext);
            }
            case "12" -> {
                // Option 12: Approve an application
                approveBTOApplicationMenu(appContext);
            }
            case "13" -> {
                // Option 13: Reject an application
                rejectBTOApplicationMenu(appContext);
            }
            case "14" -> {
                // Option 14: Approve a withdrawal request
                approveApplicationWithdrawalMenu(appContext);
            }
            case "15" -> {
                // Option 15: Reject a withdrawal request
                rejectApplicationWithdrawalMenu(appContext);
            }
            case "16" -> {
                // Option 16: Generate reports
                generateReportMenu(appContext);
            }
            case "17" -> {
                // Option 17: Reset Password
                userController.resetPassword(appContext.getCurrentUser(), appContext.getScanner());
            }
            case "18" -> {
                // Option 18: Logout
                System.out.println("Logging out...");
                appContext.setCurrentUser(null); // set the CurrentUser null
            }
            default -> {
                // Invalid option selected
                System.out.println("Please select a valid option!");
            }
        }
    }// End of HDBManagerMenu

    private void printProjectsManagedByUser(List<BTOProjectModel> managerProjects) {
        System.out.println("-- Projects Managed by You --");
        System.out.println("Project ID\tProject Name");
        System.out.println("-----------------------------------");
        for (BTOProjectModel project : managerProjects) {
            System.out.printf("%d\t\t%s%n", project.getProjectID(), project.getProjectName());
        }
    }

    private void createProjectMenu(AppContext appContext) {
        BTOProjectModel btoProjectModel = this.projectController.viewManagingProject();
        if (btoProjectModel != null) {
            System.out.println("You are currently managing another project!");
            System.out.println("Current Project Name: " + btoProjectModel.getProjectName());
            System.out.println("Closing Date: " + btoProjectModel.getClosingDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
            System.out.println("Today's Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
            System.out.println("------------------------------\nPress enter to continue...");
            appContext.getScanner().nextLine();
            return;
        }
        this.projectManagementController.createProject();
    }

    private void editBTOProjectMenu(AppContext appContext) {
        // check if currently managing any project
        boolean managingAnyActiveProject = projectController.viewManagingProject() != null;

        // 2. Get and display projects managed by this user (assuming manager role)
        List<BTOProjectModel> managedProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        if (managedProjects == null || managedProjects.isEmpty()) {
            System.out.println("You are not currently managing any projects.");
            return;
        }
        printProjectsManagedByUser(managedProjects);

        // 3. Select the project ID to edit
        System.out.print("Please select the ID of the project to edit: ");
        int projectID = appContext.getScanner().nextInt();
        appContext.getScanner().nextLine();

        // 4. Validate the selected project ID
        final int finalProjectID = projectID; // Need final variable for lambda/stream
        Optional<BTOProjectModel> projectToEdit = managedProjects.stream()
                .filter(p -> p.getProjectID() == finalProjectID)
                .findFirst();

        if (projectToEdit.isEmpty()) {
            System.out.println("Error: Invalid Project ID selected or you do not manage this project.");
            return;
        }
        System.out.println("Selected project: " + projectToEdit.get().getProjectName());

        // 5. Define menu options and prompts
        // Using a Map for cleaner association of option code to prompt text
        Map<String, String> editOptions = Map.of(
                "1", "Enter new Project Name: ",
                // "2" handled separately
                "3", "Enter new 2 Room Count: ",
                "4", "Enter new 3 Room Count: ",
                "5", "Enter new Opening Date in DD-MM-YYYY format (e.g. 31-12-2025): ",
                "6", "Enter new Closing Date in DD-MM-YYYY format (e.g. 31-12-2025): ",
                "7", "Enter new Officer Managing Count (0-10): "
        );

        // 6. Display edit menu
        System.out.println("\n-- Select field to edit for Project ID: " + projectID + " --");
        System.out.println("(1) Project Name");
        System.out.println("(2) Neighborhood");
        System.out.println("(3) 2 Room Count");
        System.out.println("(4) 3 Room Count");
        System.out.println("(5) Opening Date");
        System.out.println("(6) Closing Date");
        System.out.println("(7) Update Managing Officer count");
        System.out.print("Please select an option: ");
        String userOption = appContext.getScanner().nextLine().trim();

        String valueToChange = null;
        String prompt = editOptions.get(userOption); // Get prompt from map

        // 7. Handle user choice
        if ("2".equals(userOption)) {
            // Special handling for Neighborhood Enum
            System.out.println("-- Select Neighbourhood --");
            Neighborhood[] neighborhoods = Neighborhood.values();
            for (int i = 0; i < neighborhoods.length; i++) {
                System.out.println((i + 1) + ". " + neighborhoods[i]);
            }
            System.out.print("Enter the number corresponding to the new Neighborhood: ");
            int neighborhoodChoice = appContext.getScanner().nextInt();
            appContext.getScanner().nextLine();
            if (neighborhoodChoice > 0 && neighborhoodChoice <= neighborhoods.length) {
                // Pass the selected enum's name as the value
                valueToChange = neighborhoods[neighborhoodChoice - 1].name(); // Pass enum name
            } else {
                System.out.println("Invalid neighborhood selection.");
                return; // Exit if selection is invalid
            }
        } else if (prompt != null) {
            // Standard handling for other options using the map
            System.out.print(prompt); // Use prompt from map
            valueToChange = appContext.getScanner().nextLine();
        } else {
            System.out.println("Invalid option selected!");
            return; // Exit if option is invalid
        }

        // 8. Call the edit service method (only if a valid option was processed)
        if (!valueToChange.trim().isEmpty()) {
            // Input validation for the valueToChange should ideally happen here
            // or within the projectManagementController.editProject method.
            // For simplicity, calling editProject directly as in the original code.
            projectManagementController.editProject(userOption, valueToChange, projectID, managingAnyActiveProject);
        } else {
            System.out.println("Input cannot be empty");
        }
    }

    private void deleteBTOProjectMenu(AppContext appContext) { // REDO THIS PART< THE LOGIC IS WRONG #TODO
        // print all project
        printProjectsManagedByUser(appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID()));
        // select the project
        System.out.print("Please select the project ID: ");
        int projectID = appContext.getScanner().nextInt();
        appContext.getScanner().nextLine();

        System.out.println("--deleteBTOProjectMenu--\n(yes) To Confirm Deletion: ");
        String userOption = appContext.getScanner().nextLine().toLowerCase();
        switch (userOption) {
            case "yes" -> {
                // Confirm Deletion (REDO THIS PART!!)
                if (projectManagementController.deleteProject(projectID)) {
                    System.out.println("Deletion Successful.");
                }
            }
            default -> {
                System.out.println("Deletion process cancelled.");
            }
        }
    }

    private void toggleProjectVisibilityMenu(AppContext appContext) {

        List<BTOProjectModel> listOfProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        // In case they allow any manager to toggle any project visiblity
        // Map<Integer, String> listOfProjects = appContext.getProjectRepo().getAllProject();
        System.out.println("-- All BTO Projects --");
        System.out.println("Project ID\tProject Name");
        System.out.println("-----------------------------------");
        for (BTOProjectModel projects : listOfProjects) {
            System.out.printf("%d\t\t%s%n", projects.getProjectID(), projects.getProjectName());
        }
        System.out.print("Enter the Project ID to toggle visiblity: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (listOfProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) {
                projectManagementController.toggleProjectVisibility(projectID);
            } else {
                System.out.println("Invalid Project ID. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
        }
    }

    private void getAllBTOProjectMenu(AppContext appContext) {
        Map<Integer, String> listOfProjects = appContext.getProjectRepo().getAllProject();
        System.out.println("-- All BTO Projects --");
        System.out.println("Project ID\tProject Name");
        System.out.println("-----------------------------------");
        for (Map.Entry<Integer, String> entry : listOfProjects.entrySet()) {
            System.out.printf("%d\t\t%s%n", entry.getKey(), entry.getValue());
        }
        System.out.print("Enter the Project ID to view details: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (listOfProjects.containsKey(projectID)) {
                projectController.viewProjectByID(projectID);
            } else {
                System.out.println("Invalid Project ID. Please try again.");
            }
            System.out.print("Press enter to continue...");
            appContext.getScanner().nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
        }
    }

    private void getBTOProjectByUserIDMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to view details: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) {
                projectController.viewProjectByID(projectID);
            } else {
                System.out.println("Invalid Project ID. Please try again.");
            }
            System.out.print("Press enter to continue...");
            appContext.getScanner().nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
        }
    }

    private void getAllEnquiryMenu(AppContext appContext) {
        List<EnquiryModel> enquiries = enquiryController.getAllEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            System.out.println("-- All Enquiries --");
            for (EnquiryModel enquiry : enquiries) {
                System.out.printf("ID: %d, Subject: %s, Status: %s%n",
                        enquiry.getID(), enquiry.getEnquiryText(), enquiry.getStatus());
            }
        }
        System.out.print("Press enter to continue...");
        appContext.getScanner().nextLine();
    }

    private void editEnquiryMenu(AppContext appContext) {
        List<EnquiryModel> enquiries = enquiryController.getAllEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            System.out.println("-- All Enquiries --");
            for (int i = 0; i < enquiries.size(); i++) {
                System.out.printf("Index: %d, Subject: %s, Status: %s%n",
                        i, enquiries.get(i).getEnquiryText(), enquiries.get(i).getStatus());
            }
            System.out.print("Enter the index of the enquiry you wish to view: ");
            try {
                int index = Integer.parseInt(appContext.getScanner().nextLine());
                if (index >= 0 && index < enquiries.size()) {
                    // View the selected enquiry to print, returns if no project found
                    if (!enquiryController.viewEnquiry(index)) {
                        return;
                    };
                    // Gather new response
                    System.out.print("Enter your response to the enquiry: ");
                    String response = appContext.getScanner().nextLine();

                    // Edit the enquiry with the new response
                    enquiryController.editEnquiryResponse(index, response);
                } else {
                    System.out.println("Invalid index. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer for the index.");
            }
        }
        System.out.print("Press enter to continue...");
        appContext.getScanner().nextLine();
    }

    private void approveOfficerRegistrationMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage officer registration: ");
        List<OfficerRegistrationModel> listOfRegistration;
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) {
                // Call a method to handle officer registration for the selected project
                listOfRegistration = appContext.getOfficerRegistrationRepo().findByProjectID(projectIDString);
            } else {
                System.out.println("Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        if (listOfRegistration.isEmpty()) {
            System.out.println("No officer registrations found for this project.");
        } else {
            System.out.println("-- Officer Registrations --");
            for (int i = 0; i < listOfRegistration.size(); i++) {
                OfficerRegistrationModel officer = listOfRegistration.get(i);
                System.out.printf("Index: %d, Officer Name: %s, Status: %s%n", i, officer.getOfficerName(), officer.getStatus());
            }
            System.out.print("Enter the index of the officer registration you wish to approve: ");
            try {
                int index = Integer.parseInt(appContext.getScanner().nextLine());
                if (index <= listOfRegistration.size() && index >= 0) {
                    // approve registration
                    OfficerRegistrationModel registration = listOfRegistration.get(index);
                    if (officerRegistrationController.approveRegistration(registration)) {
                        System.out.println("Officer added.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer for the index.");
            }
        }
        System.out.print("Press enter to continue...");
        appContext.getScanner().nextLine();
    }

    private void rejectOfficerRegistrationMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage officer registration: ");
        List<OfficerRegistrationModel> listOfRegistration;
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) {
                // Call a method to handle officer registration for the selected project
                listOfRegistration = appContext.getOfficerRegistrationRepo().findByProjectID(projectIDString);
            } else {
                System.out.println("Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        if (listOfRegistration.isEmpty()) {
            System.out.println("No officer registrations found for this project.");
        } else {
            System.out.println("-- Officer Registrations --");
            for (int i = 0; i < listOfRegistration.size(); i++) {
                OfficerRegistrationModel officer = listOfRegistration.get(i);
                System.out.printf("Index: %d, Officer Name: %s, Status: %s%n", i, officer.getOfficerName(), officer.getStatus());
            }
            System.out.print("Enter the index of the officer registration you wish to reject: ");
            try {
                int index = Integer.parseInt(appContext.getScanner().nextLine());
                if (index <= listOfRegistration.size() && index >= 0) {
                    // reject registration
                    OfficerRegistrationModel registration = listOfRegistration.get(index);
                    if (officerRegistrationController.rejectRegistration(registration)) {
                        System.out.println("Officer removed successfully.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer for the index.");
            }
        }
        System.out.print("Press enter to continue...");
        appContext.getScanner().nextLine();
    }

    private void approveBTOApplicationMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage Applications(approve): ");
        String projectIDString = appContext.getScanner().nextLine();
        int projectID;
        try {
            projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) { // Check if user input is inside managerProjects
                //do nothing
            } else {
                System.out.println("Error: Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        List<BTOApplicationModel> listOfApplications = appContext.getApplicationRepo().findPendingByProjectID(projectID);
        if (listOfApplications.isEmpty()) {
            System.out.println("No applications found for this project.");
        } else {
            System.out.println("-- Applications --");
            System.out.println("Index\tApplicant Name\tMarital Status\tAge\tRoom Type");
            System.out.println("-----------------------------------------------------------");
            int index = 1;
            for (BTOApplicationModel application : listOfApplications) {
                System.out.printf("%d\t%s\t\t%s\t\t%d\t%s%n",
                        index++,
                        application.getApplicantName(),
                        application.getApplicantMaritalStatus(),
                        application.getApplicantAge(),
                        application.getFlatType());
            }
        }
        System.out.print("Enter the index of the application you wish to approve: ");
        try {
            int appChoice = Integer.parseInt(appContext.getScanner().nextLine());
            if (appChoice >= 1 && appChoice < listOfApplications.size() + 1) {
                applicationController.approveApplicantApplication(listOfApplications.get(appChoice - 1)); // we start with index 1
                System.out.println("Application approved successfully.");
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid integer for the index.");
        }
    }

    private void rejectBTOApplicationMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage Applications(reject): ");
        String projectIDString = appContext.getScanner().nextLine();
        int projectID;
        try {
            projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) { // Check if user input is inside managerProjects
                //do nothing
            } else {
                System.out.println("Error: Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        List<BTOApplicationModel> listOfApplications = appContext.getApplicationRepo().findPendingByProjectID(projectID);
        if (listOfApplications.isEmpty()) {
            System.out.println("No applications found for this project.");
        } else {
            System.out.println("-- Applications --");
            System.out.println("Index\tApplicant Name\tMarital Status\tAge\tRoom Type");
            System.out.println("-----------------------------------------------------------");
            int index = 1;
            for (BTOApplicationModel application : listOfApplications) {
                System.out.printf("%d\t%s\t\t%s\t\t%d\t%s%n",
                        index++,
                        application.getApplicantName(),
                        application.getApplicantMaritalStatus(),
                        application.getApplicantAge(),
                        application.getFlatType());
            }
        }
        System.out.print("Enter the index of the application you wish to reject: ");
        try {
            int appChoice = Integer.parseInt(appContext.getScanner().nextLine());
            if (appChoice >= 1 && appChoice < listOfApplications.size() + 1) {
                applicationController.rejectApplicantApplication(listOfApplications.get(appChoice - 1)); // we start with index 1
                System.out.println("Application rejected successfully.");
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid integer for the index.");
        }
    }

    private void approveApplicationWithdrawalMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage Applications Withdrawal (approve): ");
        String projectIDString = appContext.getScanner().nextLine();
        int projectID;
        try {
            projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) { // Check if user input is inside managerProjects
                //do nothing
            } else {
                System.out.println("Error: Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        List<BTOApplicationModel> listOfApplications = appContext.getApplicationRepo().findPendingWithDrawalByProjectID(projectID);
        if (listOfApplications.isEmpty()) {
            System.out.println("No applications found for this project.");
        } else {
            System.out.println("-- Withdrawal Applications --");
            System.out.println("Index\tApplicant Name\tMarital Status\tAge\tRoom Type");
            System.out.println("-----------------------------------------------------------");
            int index = 1;
            for (BTOApplicationModel application : listOfApplications) {
                System.out.printf("%d\t%s\t\t%s\t\t%d\t%s%n",
                        index++,
                        application.getApplicantName(),
                        application.getApplicantMaritalStatus(),
                        application.getApplicantAge(),
                        application.getFlatType());
            }
        }
        System.out.print("Enter the index of the application you wish to approve withdrawal: ");
        try {
            int appChoice = Integer.parseInt(appContext.getScanner().nextLine());
            if (appChoice >= 1 && appChoice < listOfApplications.size() + 1) {
                applicationController.approveApplicantWithdrawalApplication(listOfApplications.get(appChoice - 1)); // we start with index 1
                System.out.println("BTO Application successfully withdrawn.");
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid integer for the index.");
        }
    }

    private void rejectApplicationWithdrawalMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage Applications Withdrawal (approve): ");
        String projectIDString = appContext.getScanner().nextLine();
        int projectID;
        try {
            projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(project -> project.getProjectID() == projectID)) { // Check if user input is inside managerProjects
                //do nothing
            } else {
                System.out.println("Error: Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        List<BTOApplicationModel> listOfApplications = appContext.getApplicationRepo().findPendingWithDrawalByProjectID(projectID);
        if (listOfApplications.isEmpty()) {
            System.out.println("No applications found for this project.");
        } else {
            System.out.println("-- Withdrawal Applications --");
            System.out.println("Index\tApplicant Name\tMarital Status\tAge\tRoom Type");
            System.out.println("-----------------------------------------------------------");
            int index = 1;
            for (BTOApplicationModel application : listOfApplications) {
                System.out.printf("%d\t%s\t\t%s\t\t%d\t%s%n",
                        index++,
                        application.getApplicantName(),
                        application.getApplicantMaritalStatus(),
                        application.getApplicantAge(),
                        application.getFlatType());
            }
        }
        System.out.print("Enter the index of the application you wish to reject withdrawal: ");
        try {
            int appChoice = Integer.parseInt(appContext.getScanner().nextLine());
            if (appChoice >= 1 && appChoice < listOfApplications.size() + 1) {
                applicationController.rejectApplicantWithdrawalApplication(listOfApplications.get(appChoice - 1)); // we start with index 1
                System.out.println("BTO Application to withdraw rejected successfully.");
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid integer for the index.");
        }
    }

    private void generateReportMenu(AppContext appContext) {
        List<BTOProjectModel> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to print report for: ");
        String projectIDString = appContext.getScanner().nextLine();
        BTOProjectModel project;
        try {
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.stream().anyMatch(projects -> projects.getProjectID() == projectID)) { // Check if user input is inside managerProjects
                project = appContext.getProjectRepo().findByID(projectID);
                if (project == null) {
                    System.out.println("Error: Project not found.");
                    return;
                }
            } else {
                System.out.println("Error: Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        // By right there should be filters
        // generate report by ONLY MARRIED, ONLY SINGLE, TWO_ROOM only and THREE_ROOM only
        System.out.println("-- Generate Report --");
        System.out.println("(1) All Applications");
        System.out.println("(2) Married Applicants Only");
        System.out.println("(3) Single Applicants Only");
        System.out.println("(4) By 2-Room ");
        System.out.println("(5) By 3-Room");
        System.out.print("Please select a report type (1-5): ");
        int generateType;
        try {
            generateType = Integer.parseInt(appContext.getScanner().nextLine());
            if (generateType < 1 || generateType > 5) {
                System.out.println("Invalid option.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the report type.");
            return;
        }
        reportingController.generateProjectReport(project, generateType);

    }
}
