package com.sc2002.controllers;

import java.util.ArrayList;
import java.util.List;

import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;
import com.sc2002.services.InitializationService;

/**
 * Controller for handling initialization operations.
 * Acts as an intermediary between the view and the InitializationService.
 */
public class InitializationController {
    
    private InitializationService initializationService;
    
    /**
     * Constructor for InitializationController with an existing InitializationService.
     * 
     * @param initializationService The initialization service to use
     */
    public InitializationController() {
        this.initializationService = new InitializationService();
    }
    
    /**
     * Initializes the user repository with default users.
     * 
     * @param userList The user repository to initialize
     */
    public void initializeUsers(UserRepo userList) {
        initializationService.initializeUsers(userList);
    }
    
    /**
     * Initializes the project repository with default projects.
     * 
     * @param projectList The project repository to initialize
     * @param userList The user repository
     * @param authController The authentication controller
     */
    public void initializeProjects(ProjectRepo projectList, UserRepo userList, AuthController authController) {
        initializationService.initializeProjects(projectList, userList, authController);
    }
}
