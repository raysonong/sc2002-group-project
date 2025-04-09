package com.sc2002.view;

import java.util.List;
import java.util.Map;

import com.sc2002.controller.AppContext;
import com.sc2002.controller.ApplicationService;
import com.sc2002.controller.EnquiryService;
import com.sc2002.controller.OfficerRegistrationService;
import com.sc2002.controller.ProjectManagementService;
import com.sc2002.controller.ProjectService;
import com.sc2002.controller.ReportingService;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.EnquiryModel;
import com.sc2002.model.OfficerRegistrationModel;

public class HDBManagerView {

    // declare all the services required by Manager
    private ProjectManagementService projectManagementService=null; 
    private EnquiryService enquiryService=null; 
    private ProjectService projectService=null; 
    private OfficerRegistrationService officerRegistrationService=null; 
    private ReportingService reportingService=null; 
    private ApplicationService applicationService=null;
    public void HDBManagerMenu(AppContext appContext) {
        // TODO: Menu for HDB Manager
        // Initializing variables & services,
        // we did services 2 ways, 1 where we have repo as attribute.
        // the other where we parse repo as parameters.
        // *Only enquiryService uses repo as attribute. Part of learning process
        String userInput = "";
        List<String> menus = appContext.getCurrentUser().getMenuOptions();
        this.projectManagementService = new ProjectManagementService(appContext);
        this.enquiryService=new EnquiryService(appContext); // Only enquiryRepo, we parse repo as parameter to make it attribute
        this.projectService = new ProjectService(appContext);
        this.officerRegistrationService = new OfficerRegistrationService(appContext);
        this.reportingService = new ReportingService(appContext);
        this.applicationService = new ApplicationService(appContext);
        System.out.println("--HDB Manager Menu--");
        // Loop variable `i` is used to generate menu numbers starting from 1
        for (int i = 0; i < menus.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + menus.get(i));
        }

        System.out.print("Please select an option: ");
        userInput = appContext.getScanner().nextLine();

        switch (userInput) { 
            case "1" -> {
                // Option 1: Create a new BTO project
                appContext.getProjectRepo().save(projectManagementService.createProject());
            }
            case "2" -> {
                // Option 2: Edit an existing BTO project
                editBTOProjectMenu(appContext);
            }
            case "3" -> {
                // Option 3: Delete an existing BTO project
                deleteBTOProjectMenu(appContext);
            }
            case "4" -> {
                // Option 4: Toggle the visibility of a BTO project
                toggleProjectVisibilityMenu(appContext);
            }
            case "5" -> {
                // Option 5: View all BTO projects
                getAllBTOProjectMenu(appContext);
            }
            case "6" -> {
                // Option 6: View details of a specific BTO project
                getBTOProjectByUserIDMenu(appContext);
            }
            case "7" -> {
                // Option 7: View Enquiries
                getAllEnquiryMenu(appContext);
            }
            case "8" -> {
                // Option 8: Reply Enquiries
                editEnquiryMenu(appContext);
            }
            case "9" -> {
                // Option 9: Approve officer registration
                approveOfficerRegistrationMenu(appContext);
            }
            case "10" -> {
                // Option 10: Reject officer registration
                rejectOfficerRegistrationMenu(appContext);
            }
            case "11" -> {
                // Option 11: Approve an application
                approveBTOApplicationMenu(appContext);
            }
            case "12" -> {
                // Option 12: Reject an application
                rejectBTOApplicationMenu(appContext);
            }
            case "13" -> {
                // Option 13: Approve a withdrawal request
                approveApplicationWithdrawalMenu(appContext);
            }
            case "14" -> {
                // Option 14: Reject a withdrawal request
                rejectApplicationWithdrawalMenu(appContext);
            }
            case "15" -> {
                // Option 15: Generate reports
                generateReportMenu(appContext);
            }
            case "16" -> {
                // Option 16: Logout
                System.out.println("Logging out...");
                appContext.setCurrentUser(null); // set the CurrentUser null
            }
            default -> {
                // Invalid option selected
                System.out.println("Please select a valid option!");
            }
        }
    }// End of HDBManagerMenu

    private void printProjectsManagedByUser(Map<Integer, String> managerProjects) {
        System.out.println("-- Projects Managed by You --");
        System.out.println("Project ID\tProject Name");
        System.out.println("-----------------------------------");
        for (Map.Entry<Integer, String> entry : managerProjects.entrySet()) {
            System.out.printf("%d\t\t%s%n", entry.getKey(), entry.getValue());
        }
    }
    
    private void editBTOProjectMenu(AppContext appContext) {
        System.out.println("--editBTOProjectMenu--\n(1) Project Name\n(2) Neighborhood\n(3) 2 Room Count\n(4) 3 Room Count\n(5) Opening Date\n(6) Closing Date\nPlease select an option: ");
        String userOption = appContext.getScanner().nextLine();
        switch (userOption) {
            case "1" -> {
                // Project Name
                System.out.println("Enter new Project Name: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(userOption, valueToChange);
            }
            case "2" -> {
                // Neighborhood
                System.out.println("Enter new Neighborhood Name: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(userOption, valueToChange);
            }
            case "3" -> {
                // 2 Room Count
                System.out.println("Enter new 2 Room Count: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(userOption, valueToChange);
            }
            case "4" -> {
                // 3 Room Count
                System.out.println("Enter new 3 Room Count: ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(userOption, valueToChange);
            }
            case "5" -> {
                // Opening Date
                System.out.println("Enter new Opening Date in DD-MM-YYYY format (e.g. 31-12-2025): ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(userOption, valueToChange);
            }
            case "6" -> {
                // Closing Date
                System.out.println("Enter new Closing Date in DD-MM-YYYY format (e.g. 31-12-2025): ");
                String valueToChange = appContext.getScanner().nextLine();
                projectManagementService.editProject(userOption, valueToChange);
            }
            default -> {
                System.out.println("Invalid option selected!");
            }
        }
    }

    private void deleteBTOProjectMenu(AppContext appContext) { // REDO THIS PART< THE LOGIC IS WRONG #TODO
        System.out.println("--deleteBTOProjectMenu--\n(yes) To Confirm Deletion: ");
        String userOption = appContext.getScanner().nextLine().toLowerCase();
        switch (userOption) {
            case "yes" -> {
                // Confirm Deletion (REDO THIS PART!!)
                if (projectManagementService.deleteProject()) {
                    System.out.println("Deletion Successful.");
                }
            }default -> {
                System.out.println("Deletion process cancelled.");
            }
        }
    }

    private void toggleProjectVisibilityMenu(AppContext appContext) {
        
        Map<Integer, String> listOfProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        // In case they allow any manager to toggle any project visiblity
        // Map<Integer, String> listOfProjects = appContext.getProjectRepo().getAllProject();
        System.out.println("-- All BTO Projects --");
        System.out.println("Project ID\tProject Name");
        System.out.println("-----------------------------------");
        int index = 1;
        for (Map.Entry<Integer, String> entry : listOfProjects.entrySet()) {
            System.out.printf("%d\t\t%s%n", entry.getKey(), entry.getValue());
        }
        System.out.print("Enter the Project ID to toggle visiblity: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (listOfProjects.containsKey(projectID)) {
                projectManagementService.toggleProjectVisibility( projectID);
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
                projectService.viewProjectByID( projectID);
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
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to view details: ");
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) {
                projectService.viewProjectByID(projectID);
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
        List<EnquiryModel> enquiries = enquiryService.getAllEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            System.out.println("-- All Enquiries --");
            for (EnquiryModel enquiry : enquiries) {
                System.out.printf("ID: %d, Subject: %s, Status: %s%n",
                        enquiry.getId(), enquiry.getEnquiryText(), enquiry.getStatus());
            }
        }
        System.out.print("Press enter to continue...");
        appContext.getScanner().nextLine();
    }

    private void editEnquiryMenu(AppContext appContext){
        List<EnquiryModel> enquiries = enquiryService.getAllEnquiries();
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
                    if(!enquiryService.viewEnquiry(index)){
                        return;
                    };
                    // Gather new response
                    System.out.print("Enter your response to the enquiry: ");
                    String response = appContext.getScanner().nextLine();

                    // Edit the enquiry with the new response
                    enquiryService.editEnquiryResponse(index, response);
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

    private void approveOfficerRegistrationMenu(AppContext appContext){
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage officer registration: ");
        List<OfficerRegistrationModel> listOfRegistration;
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) {
                // Call a method to handle officer registration for the selected project
                listOfRegistration=appContext.getOfficerRegistrationRepo().findByProjectID(projectIDString);
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
                if(index<=listOfRegistration.size() && index>=0){
                    // approve registration
                    OfficerRegistrationModel registration=listOfRegistration.get(index);
                    if(officerRegistrationService.approveRegistration(registration)){
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


    private void rejectOfficerRegistrationMenu(AppContext appContext){
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage officer registration: ");
        List<OfficerRegistrationModel> listOfRegistration;
        try {
            String projectIDString = appContext.getScanner().nextLine();
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) {
                // Call a method to handle officer registration for the selected project
                listOfRegistration=appContext.getOfficerRegistrationRepo().findByProjectID(projectIDString);
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
                if(index<=listOfRegistration.size() && index>=0){
                    // reject registration
                    OfficerRegistrationModel registration=listOfRegistration.get(index);
                    if(officerRegistrationService.rejectRegistration(registration)){
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

    private void approveBTOApplicationMenu(AppContext appContext){
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage Applications(approve): ");
        String projectIDString = appContext.getScanner().nextLine();
        BTOProjectModel project;
        int projectID;
        try {
            projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) { // Check if user input is inside managerProjects
                //do nothing
            } else {
                System.out.println("Error: Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        List<BTOApplicationModel> listOfApplications=appContext.getApplicationRepo().findPendingByProjectID(projectID);
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
            if (appChoice >= 1 && appChoice < listOfApplications.size()+1) {
                applicationService.approveApplicantApplication(listOfApplications.get(appChoice-1)); // we start with index 1
                System.out.println("Application approved successfully.");
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid integer for the index.");
        }
    }
    private void rejectBTOApplicationMenu(AppContext appContext){
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage Applications(reject): ");
        String projectIDString = appContext.getScanner().nextLine();
        BTOProjectModel project;
        int projectID;
        try {
            projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) { // Check if user input is inside managerProjects
                //do nothing
            } else {
                System.out.println("Error: Invalid Project ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer for the Project ID.");
            return;
        }
        List<BTOApplicationModel> listOfApplications=appContext.getApplicationRepo().findPendingByProjectID(projectID);
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
            if (appChoice >= 1 && appChoice < listOfApplications.size()+1) {
                applicationService.rejectApplicantApplication(listOfApplications.get(appChoice-1)); // we start with index 1
                System.out.println("Application rejected successfully.");
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid integer for the index.");
        }
    }
    private void approveApplicationWithdrawalMenu(AppContext appContext){
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage withdrawal(approve): ");
        String projectIDString = appContext.getScanner().nextLine();
        BTOProjectModel project;
        try {
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) { // Check if user input is inside managerProjects
                project = appContext.getProjectRepo().getProjectByID(projectID);
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
        throw new RuntimeException("Not implemented."); // code the withdrwal here (using applicatonService)
    }
    private void rejectApplicationWithdrawalMenu(AppContext appContext){
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to manage withdrawal(reject): ");
        String projectIDString = appContext.getScanner().nextLine();
        BTOProjectModel project;
        try {
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) { // Check if user input is inside managerProjects
                project = appContext.getProjectRepo().getProjectByID(projectID);
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
        throw new RuntimeException("Not implemented."); // code the withdrwal here (using applicatonService)
    }
    private void generateReportMenu(AppContext appContext){
        Map<Integer, String> managerProjects = appContext.getProjectRepo().getProjectsByManagerID(appContext.getCurrentUser().getUserID());
        printProjectsManagedByUser(managerProjects);
        System.out.print("Enter the Project ID to print report for: ");
        String projectIDString = appContext.getScanner().nextLine();
        BTOProjectModel project;
        try {
            int projectID = Integer.parseInt(projectIDString); // Convert to Integer
            if (managerProjects.containsKey(projectID)) { // Check if user input is inside managerProjects
                project = appContext.getProjectRepo().getProjectByID(projectID);
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
        reportingService.generateProjectReport(project,generateType);
        
    }

}
