package com.sc2002.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sc2002.config.AppContext;
import com.sc2002.enums.ApplicationStatus;
import com.sc2002.enums.FlatType;
import com.sc2002.interfaces.RepoInterface;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.ProjectViewFilterModel;
import com.sc2002.model.UserModel;

/**
 * Manages the storage and retrieval of BTO project data.
 * Implements the RepoInterface for standard repository operations and adds project-specific finders.
 */
public class ProjectRepo implements RepoInterface<BTOProjectModel, Integer> {

    /** In-memory list to store all BTO project instances. */
    private List<BTOProjectModel> projects = new ArrayList<>();

    /**
     * Default constructor for ProjectRepo. Initializes the project list.
     */
    public ProjectRepo() {
        // Default constructor
    }

    @Override
    public void save(BTOProjectModel project) {
        projects.add(project);
    }

    @Override
    public boolean delete(Integer projectID) {
        return projects.removeIf(project -> project.getProjectID() == projectID);
    }

    @Override
    public BTOProjectModel findByID(Integer projectID) {
        for (BTOProjectModel project : projects) {
            if (project.getProjectID() == projectID) {
                return project;
            }
        }
        return null;
    }

    @Override
    public List<BTOProjectModel> findAll() {
        return new ArrayList<>(projects);
    }

    /**
     * Retrieves all projects as a map of Project ID to Project Name.
     *
     * @return A Map where keys are project IDs and values are project names.
     */
    public Map<Integer, String> getAllProject() {
        Map<Integer, String> toReturn = new HashMap<>();
        for (BTOProjectModel btoProjectModel : projects) {
            toReturn.put(btoProjectModel.getProjectID(), btoProjectModel.getProjectName());
        }
        return toReturn;
    }

    /**
     * Finds all projects managed by a specific Manager User ID.
     *
     * @param managerUserID The User ID of the manager.
     * @return A list of BTO projects managed by the specified manager.
     */
    public List<BTOProjectModel> getProjectsByManagerID(int managerUserID) {
        List<BTOProjectModel> toReturn = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getManagerUserID() == managerUserID) {
                toReturn.add(projects.get(i));
            }
        }
        return toReturn;
    }

    /**
     * Finds all projects managed by a specific HDB Officer.
     *
     * @param currentUser The UserModel of the officer.
     * @return A list of BTO projects managed by the specified officer.
     */
    public List<BTOProjectModel> getProjectsByOfficer(UserModel currentUser) {
        List<BTOProjectModel> toReturn = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).isManagingOfficer(currentUser)) {
                toReturn.add(projects.get(i));
            }
        }
        return toReturn;
    }

    /**
     * Finds projects based on the filter criteria set in the current user's ProjectViewFilterModel.
     * Filters by neighborhood, flat type availability, and applicant eligibility (age, marital status).
     *
     * @param appContext The application context containing the current user and their filters.
     * @return A list of BTO projects matching the filter criteria.
     */
    public List<BTOProjectModel> findByFilter(AppContext appContext) { // we parse user since we need check all the age and stuff as well
        // Get user
        UserModel user = appContext.getCurrentUser();
        // We filter base on user's filter criterion first
        ProjectViewFilterModel filter = user.getProjectViewFilter();
        List<BTOProjectModel> filteredProjects = projects.stream()
                .filter(project
                        -> (filter.getNeighborhood() == null || project.getNeighborhood() == filter.getNeighborhood())
                && (filter.getFlatType() == null
                || // if no filter set
                ((filter.getFlatType() == FlatType.TWO_ROOM && project.getTwoRoomCount() > 0)
                || // if filter is two-room and there is roomcount
                (filter.getFlatType() == FlatType.THREE_ROOM && project.getThreeRoomCount() > 0)) // if filter is three-room and there is roomcount
                )
                )
                .collect(Collectors.toList());

        if (appContext.getAuthController().isApplicant(user) || appContext.getAuthController().isOfficer(user)) {
            List<BTOProjectModel> secondFilteredProjects = new ArrayList<>();
            for (BTOProjectModel project : filteredProjects) { // we further filter the filteredProjects
                boolean isVisible = project.isVisible();
                boolean hasTwoRoom = project.getTwoRoomCount() > 0;
                boolean hasThreeRoom = project.getThreeRoomCount() > 0;

                if (isVisible && (hasTwoRoom || hasThreeRoom)) { // either two room or three room have, and is visible
                    if (user.getMaritalStatus() && user.getAge() >= 21) { // married and above or equal 21
                        secondFilteredProjects.add(project); // just add
                    } else if (!user.getMaritalStatus() && user.getAge() >= 35 && hasTwoRoom) { // if single and more than 35 and there is tworoom
                        secondFilteredProjects.add(project);// then we add
                    }
                } else if (project.isManagingOfficer(user)) { // if managing officer, can view no matter what
                    secondFilteredProjects.add(project);
                } else { // if not visible or no more rooms etc
                    boolean hasApplied = appContext.getApplicationRepo().hasUserAppliedForProject(user.getUserID(), project.getProjectID());
                    if (hasApplied) {
                        ApplicationStatus applicationStatus = appContext.getApplicationRepo().getApplicationStatus(user.getUserID(), project.getProjectID());
                        if (applicationStatus == ApplicationStatus.PENDING
                                || // We dont let him see it if withdrawn
                                applicationStatus == ApplicationStatus.SUCCESSFUL
                                || applicationStatus == ApplicationStatus.BOOKED) {
                            secondFilteredProjects.add(project);
                        }
                    }
                }
            }
            // Sort secondFilteredProjects by project name alphabetically (case-insensitive)
            secondFilteredProjects.sort((p1, p2) -> p1.getProjectName().compareToIgnoreCase(p2.getProjectName()));
            return secondFilteredProjects;
        } else if (appContext.getAuthController().isManager(user)) {
            // No need to do buisness logic since manager
            // Sort filteredProjects by project name alphabetically (case-insensitive)
            filteredProjects.sort((p1, p2) -> p1.getProjectName().compareToIgnoreCase(p2.getProjectName()));
            return filteredProjects;
        } else {
            return null;
        }
    }

    /**
     * Finds projects relevant to the current user (managed projects for officers/managers)
     * based on the filter criteria set in their ProjectViewFilterModel.
     * Filters by neighborhood and flat type availability.
     *
     * @param appContext The application context containing the current user and their filters.
     * @return A list of relevant BTO projects matching the filter criteria.
     */
    public List<BTOProjectModel> findPersonalByFilter(AppContext appContext){
        try{
            if(appContext.getAuthController().isManager(appContext.getCurrentUser())){
                List<BTOProjectModel> filteredProjects=findByFilter(appContext); // Get projects Filtered by Manager's preference
                // Further filter the list to projects where manager ID lines up
                return filteredProjects.stream()
                    .filter(project -> project.getManagerUserID() == appContext.getCurrentUser().getUserID())
                    .collect(Collectors.toList());
            }else{
                throw new RuntimeException("User is not authorized to perform this action.");
            }
        }catch(RuntimeException e){
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    /**
     * This function returns the project ID of the last project in a list of
     * projects.
     *
     * @return The method `getLastProjectID` returns the project ID of the last
     * project in the list of projects.
     */
    public int getLastProjectID() {
        return this.projects.getLast().getProjectID();
    }

    /**
     * Checks if there is a date conflict between the specified opening and closing dates
     * and the existing projects managed by the current user.
     *
     * @param newOpeningDate The proposed opening date for the new project.
     * @param newClosingDate The proposed closing date for the new project.
     * @param appContext The application context containing the current user and other services.
     * @return {@code true} if there is a date conflict with an existing project managed by the current user,
     *         {@code false} otherwise.
     */
    public boolean hasDateConflict(LocalDate newOpeningDate,LocalDate newClosingDate,AppContext appContext) {
        for (BTOProjectModel project : projects) {
            if (newOpeningDate.isBefore(project.getClosingDate()) &&
                newClosingDate.isAfter(project.getOpeningDate()) &&
                project.getManagerUserID() == appContext.getCurrentUser().getUserID()) {
                return true;
            }
        }
        return false;
    }
}
