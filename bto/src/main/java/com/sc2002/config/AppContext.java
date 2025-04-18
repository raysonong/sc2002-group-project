package com.sc2002.config;

import java.util.Scanner;

import com.sc2002.controllers.AuthController;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.EnquiryRepo;
import com.sc2002.repositories.OfficerRegistrationRepo;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;

/**
 * Provides a centralized context for accessing shared resources and services throughout the application.
 * This class acts as a simple service locator or dependency injection container, holding references
 * to repositories, controllers, the current user session, and the global scanner instance.
 */
public class AppContext {
    /** Scanner instance for handling user input globally. */
    private final Scanner scanner;
    /** Controller responsible for authentication logic. */
    private final AuthController authController;
    /** The currently logged-in user. Null if no user is logged in. */
    private UserModel currentUser; // only this guy will need to keep changing
    /** Repository for managing user data. */
    private final UserRepo userRepo;
    /** Repository for managing BTO project data. */
    private final ProjectRepo projectRepo;
    /** Repository for managing enquiry data. */
    private final EnquiryRepo enquiryRepo;
    /** Repository for managing BTO application data. */
    private final ApplicationRepo applicationRepo;
    /** Repository for managing officer registration data. */
    private final OfficerRegistrationRepo officerRegistrationRepo;

    /**
     * Constructs a new AppContext with all required dependencies.
     *
     * @param scanner               The global Scanner instance.
     * @param authController        The authentication controller.
     * @param currentUser           The initial user (can be null).
     * @param userRepo              The user repository.
     * @param projectRepo           The project repository.
     * @param enquiryRepo           The enquiry repository.
     * @param applicationRepo       The application repository.
     * @param officerRegistrationRepo The officer registration repository.
     */
    public AppContext(Scanner scanner, AuthController authController, UserModel currentUser, UserRepo userRepo, ProjectRepo projectRepo, EnquiryRepo enquiryRepo, ApplicationRepo applicationRepo, OfficerRegistrationRepo officerRegistrationRepo){
        this.scanner = scanner;
        this.authController = authController;
        this.currentUser = currentUser;
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.enquiryRepo = enquiryRepo;
        this.applicationRepo = applicationRepo;
        this.officerRegistrationRepo = officerRegistrationRepo;
    }

    /**
     * Gets the global Scanner instance.
     * @return The Scanner instance.
     */
    public Scanner getScanner() { return scanner; }

    /**
     * Gets the authentication controller.
     * @return The AuthController instance.
     * @deprecated Renamed to getAuthController for consistency. Use {@link #getAuthController()} instead.
     */
    @Deprecated
    public AuthController getAuthService() { return authController; }

    /**
     * Gets the authentication controller.
     * @return The AuthController instance.
     */
    public AuthController getAuthController() { return authController; }

    /**
     * Gets the currently logged-in user.
     * @return The current UserModel instance, or null if not logged in.
     */
    public UserModel getCurrentUser(){ return currentUser; }

    /**
     * Gets the user repository.
     * @return The UserRepo instance.
     */
    public UserRepo getUserRepo() { return userRepo; }

    /**
     * Gets the project repository.
     * @return The ProjectRepo instance.
     */
    public ProjectRepo getProjectRepo() { return projectRepo; }

    /**
     * Gets the enquiry repository.
     * @return The EnquiryRepo instance.
     */
    public EnquiryRepo getEnquiryRepo() { return enquiryRepo; }

    /**
     * Gets the application repository.
     * @return The ApplicationRepo instance.
     */
    public ApplicationRepo getApplicationRepo() { return applicationRepo; }

    /**
     * Gets the officer registration repository.
     * @return The OfficerRegistrationRepo instance.
     */
    public OfficerRegistrationRepo getOfficerRegistrationRepo() { return officerRegistrationRepo; }

    /**
     * Sets the currently logged-in user.
     * @param currentUser The UserModel instance representing the logged-in user, or null to log out.
     */
    public void setCurrentUser(UserModel currentUser){ this.currentUser=currentUser; }

}
