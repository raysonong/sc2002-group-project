package com.sc2002.controllers;

import com.sc2002.repositories.OfficerRegistrationRepo;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;
import com.sc2002.services.InitializationService;

/**
 * Controller responsible for initializing the application state, such as
 * loading data from CSV files into repositories.
 */
public class InitializationController {

    /**
     * The service layer handling the core initialization logic.
     */
    private InitializationService initializationService;

    /**
     * Constructor for InitializationController. Initializes the
     * InitializationService.
     */
    public InitializationController() {
        this.initializationService = new InitializationService();
    }

    /**
     * Constructor for InitializationController with an existing
     * InitializationService. Useful for testing or specific dependency
     * injection scenarios.
     *
     * @param initializationService The initialization service to use
     */
    public InitializationController(InitializationService initializationService) {
        this.initializationService = initializationService;
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
     * @param userList The user repository to use for project initialization
     * @param officerRegistrationList The officer registration repository to use
     * for project initialization
     * @param authController The authentication controller to manage user
     * authentication during initialization
     */
    public void initializeProjects(ProjectRepo projectList, UserRepo userList, OfficerRegistrationRepo officerRegistrationList, AuthController authController) {
        initializationService.initializeProjects(projectList, userList, officerRegistrationList, authController);
    }
}
