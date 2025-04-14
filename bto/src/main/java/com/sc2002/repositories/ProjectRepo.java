package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sc2002.controller.AppContext;
import com.sc2002.enums.FlatType;
import com.sc2002.enums.UserRole;
import com.sc2002.enums.ApplicationStatus;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.ProjectViewFilterModel;
import com.sc2002.model.User;


public class ProjectRepo {

    private List<BTOProjectModel> projects = new ArrayList<>();

    public BTOProjectModel getProjectByID(int projectID) {
        for (BTOProjectModel project : projects) {
            if (project.getProjectID() == projectID) {
                return project;
            }
        }
        return null;
    }
    
    public boolean deleteByProjectID(int projectID) {
        return projects.removeIf(project -> project.getProjectID() == projectID);
    }

    public Map<Integer, String> getAllProject() {
        Map<Integer, String> toReturn = new HashMap<>();
        for (int i = 0; i < projects.size(); i++) {
            toReturn.put(i, projects.get(i).getProjectName());
        }
        return toReturn;
    }

    public Map<Integer, String> getProjectsByManagerID(int managerUserID) {
        Map<Integer, String> toReturn = new HashMap<>();
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getManagerUserID() == managerUserID) {
                toReturn.put(i, projects.get(i).getProjectName());
            }
        }
        return toReturn;
    }
    public List<BTOProjectModel> getProjectsByOfficerID(User currentUser) {
        List<BTOProjectModel> toReturn = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).isManagingOfficer(currentUser)) {
                toReturn.add(projects.get(i));
            }
        }
        return toReturn;
    }
    public List<BTOProjectModel> getAllProjects() {
        return new ArrayList<>(projects);
    }

    public void save(BTOProjectModel project) {
        projects.add(project);
    }

    public List<BTOProjectModel> findByFilter(AppContext appContext) { // we parse user since we need check all the age and stuff as well
        // Get user
        User user=appContext.getCurrentUser();
        // We filter base on user's filter criterion first
        ProjectViewFilterModel filter = user.getProjectViewFilter();
        List<BTOProjectModel> filteredProjects = projects.stream()
            .filter(project -> 
            (filter.getNeighborhood() == null || project.getNeighborhood() == filter.getNeighborhood()) &&
            (filter.getFlatType() == null || // if no filter set
                ((filter.getFlatType() == FlatType.TWO_ROOM && project.getTwoRoomCount() > 0) || // if filter is two-room and there is roomcount
                 (filter.getFlatType() == FlatType.THREE_ROOM && project.getThreeRoomCount() > 0)) // if filter is three-room and there is roomcount
            )
            )
            .collect(Collectors.toList());
            
        if(appContext.getAuthService().isApplicant(user) || appContext.getAuthService().isOfficer(user)){
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
                } else if(project.isManagingOfficer(user)){ // if managing officer, can view no matter what
                    secondFilteredProjects.add(project);
                }else { // if not visible or no more rooms etc
                    boolean hasApplied = appContext.getApplicationRepo().hasUserAppliedForProject(user.getUserID(), project.getProjectID());
                    if (hasApplied) {
                        ApplicationStatus applicationStatus = appContext.getApplicationRepo().getApplicationStatus(user.getUserID(), project.getProjectID());
                        if (applicationStatus==ApplicationStatus.PENDING || // We dont let him see it if withdrawn
                            applicationStatus==ApplicationStatus.SUCCESSFUL || 
                            applicationStatus==ApplicationStatus.BOOKED ) {
                            secondFilteredProjects.add(project);
                        }
                    }
                }
            }
            return secondFilteredProjects;
        }else if(appContext.getAuthService().isManager(user)){ // Manager can see all
            return filteredProjects; // just filter base on users requirement
        }else{
            return null;
        }
    }

    /**
     * This function returns the project ID of the last project in a list of projects.
     * 
     * @return The method `getLastProjectID` returns the project ID of the last project in the list of
     * projects.
     */
    public int getLastProjectID() {
        return this.projects.getLast().getProjectID();
    }
}
