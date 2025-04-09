package com.sc2002.controller;

import java.util.List;

import com.sc2002.enums.FlatType;
import com.sc2002.model.BTOApplicationModel;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.User;
import com.sc2002.repositories.ApplicationRepo;

public class ReportingService {
    private AppContext appContext;

    public ReportingService(AppContext appContext) {
        this.appContext = appContext;
    }
    public void generateProjectReport(BTOProjectModel project,int generateType){
        // 1) List of Applicants and their respective flat booking - flat type, project name , age marital status.
        // 2) There should be filters to generate a list based on various categories,
        //      e.g. report of married applicants' choice of flat type
        User currentUser = this.appContext.getCurrentUser();
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
                        .filter(booking -> booking.getApplicantMaritialStatus())
                        .toList();
                }
                case 3 -> { // Filter by Single
                    projectBookings = projectBookings.stream()
                        .filter(booking -> !booking.getApplicantMaritialStatus())
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
                    booking.getApplicantMaritialStatus() ? "Married" : "Single"
                );
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
