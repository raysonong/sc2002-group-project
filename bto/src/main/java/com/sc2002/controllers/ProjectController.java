package com.sc2002.controllers;

import com.sc2002.config.AppContext;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.services.ProjectService;

/**
 * Controller for handling project-related operations.
 * Acts as an intermediary between the view and the ProjectService.
 * In case there needs to be user input validation.
 */
public class ProjectController {
    
    private ProjectService projectService;
    
    /**
     * Constructor for ProjectController.
     * 
     * @param appContext The application context
     */
    public ProjectController(AppContext appContext) {
        this.projectService = new ProjectService(appContext);
    }
    
    
    /**
     * Displays project details based on the project ID.
     * Access rules are enforced based on the current user's role and the project's visibility.
     * 
     * @param projectID The ID of the project to view
     */
    public void viewProjectByID(int projectID) {
        projectService.viewProjectByID(projectID);
    }
    
    /**
     * Retrieves the active project currently managed by the logged-in user.
     * Returns null if the user doesn't manage any project or there are no active projects.
     * 
     * @return BTOProjectModel if an active managed project is found, null otherwise
     */
    public BTOProjectModel viewManagingProject() {
        return projectService.viewManagingProject();
    }
}
