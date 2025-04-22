package com.sc2002.services;

import java.util.ArrayList;
import java.util.List;

import com.sc2002.config.AppContext;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.EnquiryModel;

/**
 * Provides services for managing enquiries related to BTO projects. Handles
 * submission, retrieval, viewing, responding to, and deleting enquiries,
 * including authorization checks based on user roles (Applicant, Officer,
 * Manager).
 */
public class EnquiryService {

    /**
     * The application context providing access to repositories and current user
     * state.
     */
    private AppContext appContext;

    /**
     * Constructs an EnquiryService with the given application context.
     *
     * @param appContext The application context.
     */
    public EnquiryService(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Submits a new enquiry for a specific project by an applicant. Validates
     * input and saves the enquiry using the repository.
     *
     * @param applicantNRIC The NRIC of the applicant submitting the enquiry.
     * @param projectID The ID of the project the enquiry is about.
     * @param enquiryText The text content of the enquiry.
     * @return True if the enquiry was submitted successfully, false otherwise
     * (e.g., invalid input).
     */
    public boolean submitEnquiry(String applicantNRIC, int projectID, String enquiryText) {
        // create a new enquiry
        if (applicantNRIC == null || enquiryText == null || enquiryText.isBlank()) {
            return false;
        }

        EnquiryModel newEnquiry = new EnquiryModel(applicantNRIC, projectID, enquiryText);
        // save the new enquiry
        appContext.getEnquiryRepo().saveEnquiry(newEnquiry);
        System.out.println("Your enquiry has been submitted successfully!");
        return true;
    }

    /**
     * Retrieves all enquiries accessible to the current user. Managers can see
     * all enquiries. Officers can see enquiries for projects they manage.
     * Includes authorization checks.
     *
     * @return A list of EnquiryModel objects accessible to the current user, or
     * null if unauthorized or an error occurs.
     */
    public List<EnquiryModel> getAllEnquiries() {
        // Check if the current user is authenticated and has the correct role
        try {
            if (this.appContext.getCurrentUser() != null && this.appContext.getAuthController().isManager(this.appContext.getCurrentUser())) {
                return this.appContext.getEnquiryRepo().findAll();
            } else if (this.appContext.getCurrentUser() != null && this.appContext.getAuthController().isOfficer(this.appContext.getCurrentUser())) {
                // @rayson, was thinking you put your getAllEnquiries here, (this is for printing your menus)
                // Then sort to only return those which officer by right can view (you can code the sorting either in repo or here)
                List<EnquiryModel> toReturn = new ArrayList<>();
                List<BTOProjectModel> projects = this.appContext.getProjectRepo().getProjectsByOfficer(appContext.getCurrentUser()); // [projectID,projectname]
                for (BTOProjectModel project : projects) {
                    int projectID = project.getProjectID();
                    List<EnquiryModel> enquiries = this.appContext.getEnquiryRepo().findByProjectID(projectID);
                    toReturn.addAll(enquiries);
                } // NOT TESTED
                return toReturn;

            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    /**
     * Displays the details of a specific enquiry, including its response if
     * available. Performs authorization checks: Managers can view any enquiry,
     * Officers can view enquiries for their managed projects. Prints the
     * formatted enquiry details to the console.
     *
     * @param enquiryID The ID of the enquiry to view.
     * @return True if the enquiry was found and the user is authorized to view
     * it, false otherwise.
     */
    public boolean viewEnquiry(int enquiryID) {
        try {
            EnquiryModel enquiry = this.appContext.getEnquiryRepo().findByID(enquiryID);

            if (this.appContext.getAuthController().isManager(this.appContext.getCurrentUser())) {
                // for managers, can reply all
                if (enquiry != null) {
                    System.out.println(enquiry.getFormattedEnquiry()); // we print it in the service
                    return true;
                } else {
                    throw new RuntimeException("Project not found.");
                }
            } else if (this.appContext.getAuthController().isOfficer(this.appContext.getCurrentUser())) {
                // for officer only managing projects can reply
                // @ Rayson, this views the exact enquiry,
                // idea for flow of enquiry editing
                // 1) print menu using getAllEnquiry
                // 2) viewEnquiry(index) to view the exact enquiry
                // 3) editEnquiry(index) to edit that exact enquiry
                if (enquiry != null) {
                    if (appContext.getProjectRepo().findByID(enquiry.getProjectID()).isManagingOfficer(appContext.getCurrentUser())) {
                        System.out.println(enquiry.getFormattedEnquiry()); // we print it in the service
                        return true;
                    } else { // might need to check if officer is applicant, will see when we debugging @rayson
                        throw new RuntimeException("User is not an officer for project.");
                    }
                } else {
                    throw new RuntimeException("Project not found.");
                }

                // Check if officer can viewEnquiry
            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds or updates the response to a specific enquiry. Performs
     * authorization checks: Managers can respond to enquiries for projects they
     * manage, Officers can respond to enquiries for projects they manage.
     *
     * @param enquiryID The ID of the enquiry to respond to.
     * @param response The text content of the response.
     * @return True if the response was successfully added/updated, false
     * otherwise (e.g., enquiry not found, unauthorized).
     */
    public boolean editEnquiryResponse(int enquiryID, String response) {
        try {
            EnquiryModel enquiry = this.appContext.getEnquiryRepo().findByID(enquiryID);
            if (this.appContext.getAuthController().isManager(this.appContext.getCurrentUser())) {
                if (appContext.getProjectRepo().findByID(enquiry.getProjectID()).getManagerUserID() != appContext.getCurrentUser().getUserID()) {
                    throw new RuntimeException("You are not Project Manager of this enquiry's project.");
                }
                if (enquiry != null) {
                    enquiry.replyEnquiry(response, this.appContext.getCurrentUser().getUserID());
                    return true;
                } else {
                    throw new RuntimeException("Project not found.");
                }
            } else if (this.appContext.getAuthController().isOfficer(this.appContext.getCurrentUser())) {
                // @ Rayson, this views the exact enquiry,
                // idea for flow of enquiry editing
                // 1) print menu using getAllEnquiry
                // 2) viewEnquiry(index) to view the exact enquiry
                // 3) editEnquiry(index) to edit that exact enquiry
                if (enquiry != null) {
                    if (appContext.getProjectRepo().findByID(enquiry.getProjectID()).isManagingOfficer(appContext.getCurrentUser())) {
                        enquiry.replyEnquiry(response, this.appContext.getCurrentUser().getUserID());
                        return true;
                    } else {
                        throw new RuntimeException("User is not an officer for project.");
                    }
                } else {
                    throw new RuntimeException("Project not found.");
                }
            } else {
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an enquiry from the repository based on its ID.
     *
     * @param enquiryID The ID of the enquiry to delete.
     * @return True if the enquiry was found and deleted, false otherwise.
     */
    public boolean deleteEnquiry(int enquiryID) {
        return appContext.getEnquiryRepo().delete(enquiryID);
    }
}
