package com.sc2002.services;

import java.util.List;

import com.sc2002.config.AppContext;
import com.sc2002.enums.FlatType;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.ApplicationRepo;

/**
 * Service responsible for generating various reports based on application and project data.
 * Typically used by Managers or Officers.
 */
public class ReportingService {
    /** The application context providing access to repositories and current user state. */
    private AppContext appContext;

    /**
     * Constructs a ReportingService with the given application context.
     *
     * @param appContext The application context.
     */
    public ReportingService(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Generates and prints a report listing applicants who have booked a flat for a specific project.
     * The report includes applicant name, flat type, age, and marital status.
     * Allows filtering the report based on marital status or flat type.
     * Performs authorization check (only the project manager can generate the report).
     *
     * @param project The BTOProjectModel for which to generate the report.
     * @param generateType An integer indicating the filter type:
     *                     1: No filter
     *                     2: Filter by Married applicants
     *                     3: Filter by Single applicants
     *                     4: Filter by TWO_ROOM flat type
     *                     5: Filter by THREE_ROOM flat type
     */
    public void generateProjectReport(BTOProjectModel project,int generateType){
        UserModel currentUser = this.appContext.getCurrentUser();
        ApplicationRepo applicationRepo=this.appContext.getApplicationRepo();

        try{
            if(project.getManagerUserID()!=currentUser.getUserID()) throw new RuntimeException("User not authorized.");
            List<BTOApplicationModel> projectBookings = applicationRepo.findBookedByProjectID(project.getProjectID());
            switch(generateType){
                case 1 -> { // No Filter
                    // Do nothing
                }
                case 2 -> { // Filter by Married
                    projectBookings = projectBookings.stream()
                        .filter(booking -> booking.getApplicantMaritalStatus())
                        .toList();
                }
                case 3 -> { // Filter by Single
                    projectBookings = projectBookings.stream()
                        .filter(booking -> !booking.getApplicantMaritalStatus())
                        .toList();
                }
                case 4 -> { // Filter by TWO_ROOM
                    projectBookings = projectBookings.stream()
                        .filter(booking -> booking.getFlatType() == FlatType.TWO_ROOM)
                        .toList();
                }
                case 5 -> { // Filter by THREE_ROOM
                    projectBookings = projectBookings.stream()
                        .filter(booking -> booking.getFlatType() == FlatType.THREE_ROOM)
                        .toList();
                }
                default -> {
                    throw new RuntimeException("Option not implemented.");
                }
            }
            //flat type, project name, age, marital status
            System.out.println("Project Name: " + project.getProjectName());
            System.out.println("--------------------------------------------------");
            System.out.printf("%-20s %-15s %-10s %-15s%n", "Applicant's Name", "Flat Type", "Age", "Marital Status");
            System.out.println("--------------------------------------------------");
            if (projectBookings.isEmpty()) { // check if got any result
                System.out.println("No bookings found for the selected criteria.");
                return;
            }
            for (BTOApplicationModel booking : projectBookings) { // print result for "report"
                System.out.printf("%-20s %-15s %-10d %-15s%n",
                    booking.getApplicantName(),
                    booking.getFlatType(),
                    booking.getApplicantAge(),
                    booking.getApplicantMaritalStatus() ? "Married" : "Single"
                );
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
