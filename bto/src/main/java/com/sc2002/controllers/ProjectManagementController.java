package com.sc2002.controllers;

import com.sc2002.config.AppContext;
import com.sc2002.services.ProjectManagementService;

/**
 * Controller for handling project management operations.
 * Acts as an intermediary between the view and the ProjectManagementService.
 */
public class ProjectManagementController {
    
    private ProjectManagementService projectManagementService;
    
    /**
     * Constructor for ProjectManagementController.
     * 
     * @param appContext The application context
     */
    public ProjectManagementController(AppContext appContext) {
        this.projectManagementService = new ProjectManagementService(appContext);
    }
    
    /**
     * Creates a new BTO project with user input.
     */
    public void createProject() {
        projectManagementService.createProject();
    }
    
    /**
     * Edits an existing project.
     * 
     * @param userOption The option to edit
     * @param valueToChange The new value
     * @param projectID The ID of the project to edit
     * @param managingProject Whether the user is currently managing the project
     */
    public void editProject(String userOption, String valueToChange, int projectID, boolean managingProject) {
        projectManagementService.editProject(userOption, valueToChange, projectID, managingProject);
    }
    
    /**
     * Deletes a project.
     * 
     * @param projectID The ID of the project to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteProject(int projectID) {
        return projectManagementService.deleteProject(projectID);
    }
    
    /**
     * Toggles the visibility of a project.
     * 
     * @param projectID The ID of the project to toggle visibility for
     */
    public void toggleProjectVisibility(Integer projectID) {
        projectManagementService.toggleProjectVisibility(projectID);
    }
}
